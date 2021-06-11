package com.chqiuu.cgp.db;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.util.JdbcConstants;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.SchemataEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.JdbcTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Oracle数据库
 *
 * @author chqiu
 */
public class OracleDatabase extends BaseDatabase {

    @Override
    public List<SchemataEntity> queryDatabaseList(BaseConnect connect) {
        return connect.queryList("select name AS schema_name from v$database", SchemataEntity.class);
    }

    @Override
    public List<TableEntity> queryTableList(BaseConnect connect, String tableName) {
        String sql = String.format("select dt.table_name tableName, dtc.comments tableComment from user_tables dt,user_tab_comments dtc,user_objects uo where dt.table_name = dtc.table_name and dt.table_name = uo.object_name and uo.object_type='TABLE' %s order by uo.CREATED desc"
                , StrUtil.isBlank(tableName) ? "" : " and dt.table_name like concat('%', UPPER('" + tableName + "'))");
        return connect.queryList(sql, TableEntity.class);
    }

    @Override
    public TableEntity queryTable(BaseConnect connect, String tableName) {
        String sql = String.format("select dt.table_name tableName,dtc.comments tableComment,dt.last_analyzed createTime from user_tables dt,user_tab_comments dtc where dt.table_name=dtc.table_name and dt.table_name = UPPER('%s')", tableName);
        List<TableEntity> list = connect.queryList(sql, TableEntity.class);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ColumnEntity> queryColumns(BaseConnect connect, String tableName) {
        String sql = String.format("select temp.column_name columnname,\n" +
                "        temp.data_type dataType,\n" +
                "        temp.comments columnComment,\n" +
                "        case temp.constraint_type when 'P' then 'PRI' when 'C' then 'UNI' else '' end \"COLUMNKEY\",\n" +
                "        '' \"EXTRA\"\n" +
                "        from (select col.column_id,col.column_name,col.data_type,colc.comments,uc.constraint_type,\n" +
                "        row_number() over (partition by col.column_name order by uc.constraint_type desc) as row_flg\n" +
                "        from user_tab_columns col left join user_col_comments colc\n" +
                "        on colc.table_name = col.table_name and colc.column_name = col.column_name\n" +
                "        left join user_cons_columns ucc on ucc.table_name = col.table_name\n" +
                "        and ucc.column_name = col.column_name\n" +
                "        left join user_constraints uc on uc.constraint_name = ucc.constraint_name\n" +
                "        where col.table_name = upper('%s')) temp\n" +
                "        where temp.row_flg = 1 order by temp.column_id", tableName);
        return connect.queryList(sql, ColumnEntity.class);
    }

    @Override
    public List<TableEntity> getTableList(String createTableSqls) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(createTableSqls, JdbcConstants.MYSQL);
        List<TableEntity> tableList = new ArrayList<>();
        for (SQLStatement sqlStatement : stmtList) {
            if (sqlStatement instanceof OracleCreateTableStatement) {
                // 获取表对象
                OracleCreateTableStatement stmt = (OracleCreateTableStatement) sqlStatement;
                TableEntity tableEntity = new TableEntity();
                tableEntity.setTableName(stmt.getTableSource().getExpr().toString().replace("`", ""));
                tableEntity.setTableComment(stmt.getComment().toString().replace("'", ""));
                List<ColumnEntity> columns = new ArrayList<>();
                for (SQLTableElement column : stmt.getTableElementList()) {
                    if (column instanceof SQLColumnDefinition) {
                        ColumnEntity columnEntity = new ColumnEntity();
                        // 获取字段对象
                        SQLColumnDefinition columnDefinition = (SQLColumnDefinition) column;
                        columnEntity.setColumnName(columnDefinition.getName().getSimpleName().replace("`", ""));
                        columnEntity.setColumnComment(columnDefinition.getComment().toString().replace("'", ""));
                        columnEntity.setDataType(columnDefinition.getDataType().getName());
                        String columnType = columnDefinition.getDataType().toString();
                        columnEntity.setColumnType(columnType);
                        if (columnType.contains("(")) {
                            String length = columnType.substring(columnType.indexOf("(") + 1, columnType.indexOf(")"));
                            if (length.contains(",")) {
                                String[] numerics = length.split(",");
                                // 设置Numeric类型整数部分长度
                                columnEntity.setNumericPrecision(Long.parseLong(numerics[0].trim()));
                                // 设置Numeric类型小数部分长度
                                columnEntity.setNumericScale(Long.parseLong(numerics[1].trim()));
                            } else {
                                JdbcTypeEnum jdbcTypeEnum = JdbcTypeEnum.getByJdbcType(columnEntity.getDataType());
                                if (null == jdbcTypeEnum) {
                                    columnEntity.setCharacterMaximumLength(Long.parseLong(length));
                                } else if ("String".equals(jdbcTypeEnum.getJavaType())) {
                                    // 设置字符串字段限制长度
                                    columnEntity.setCharacterMaximumLength(Long.parseLong(length));
                                } else {
                                    // 设置Integer、Long等类型长度
                                    columnEntity.setNumericPrecision(Long.parseLong(length));
                                    columnEntity.setNumericScale(0L);
                                }
                            }
                        }
                        columnEntity.setDdl(column.toString());
                        columns.add(columnEntity);
                    } else if (column instanceof OraclePrimaryKey) {
                        // 获取表内主键
                        OraclePrimaryKey primaryKey = (OraclePrimaryKey) column;
                        for (SQLSelectOrderByItem item : primaryKey.getColumns()) {
                            for (ColumnEntity columnEntity : columns) {
                                if (item.getExpr().toString().replace("`", "").equals(columnEntity.getColumnName())) {
                                    // 设置字段主键
                                    columnEntity.setColumnKey("PRI");
                                }
                            }
                        }
                    }
                }
                tableEntity.setColumns(columns);
                tableList.add(tableEntity);
            }
        }
        return tableList;
    }
}
