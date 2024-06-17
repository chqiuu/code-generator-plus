package com.chqiuu.cgp.db;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.SchemataEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.JdbcTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MySql数据库
 *
 * @author chqiu
 */
public class MySqlDatabase extends BaseDatabase {

    @Override
    public List<SchemataEntity> queryDatabaseList(BaseConnect connect) {
        return connect.queryList("SELECT `catalog_name`, `schema_name`, `default_character_set_name`, `default_collation_name`, `sql_path` FROM information_schema.SCHEMATA", SchemataEntity.class);
    }

    @Override
    public List<TableEntity> queryTableList(BaseConnect connect, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select table_catalog,table_schema,table_name,table_type,engine,version,row_format,table_rows,avg_row_length,data_length" +
                " ,max_data_length,index_length,data_free,auto_increment,create_time,update_time,check_time,table_collation,checksum,create_options,table_comment" +
                " from information_schema.tables where table_schema = (select database())");
        if (StringUtils.isNotBlank(tableName)) {
            sql.append(" and `table_name` like '%");
            sql.append(tableName);
            sql.append("%'");
        }
        sql.append(" order by `create_time` desc");
        return connect.queryList(sql.toString(), TableEntity.class);
    }

    @Override
    public TableEntity queryTable(BaseConnect connect, String tableName) {
        String sql = ("select table_catalog,table_schema,table_name,table_type,engine,version,row_format,table_rows,avg_row_length,data_length" +
                " ,max_data_length,index_length,data_free,auto_increment,create_time,update_time,check_time,table_collation,checksum,create_options,table_comment" +
                " from information_schema.tables where table_schema = (select database())") +
                " and `table_name` = '" +
                tableName +
                "'";
        List<TableEntity> list = connect.queryList(sql, TableEntity.class);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ColumnEntity> queryColumns(BaseConnect connect, String tableName) {
        String sql = ("select `table_catalog`,`table_schema`,`table_name`,`column_name`,`ordinal_position`,`column_default`,`is_nullable`" +
                " ,`data_type`,`character_maximum_length`,`character_octet_length`,`numeric_precision`,`numeric_scale`,`datetime_precision`" +
                " ,`character_set_name`,`collation_name`,`column_type`,`column_key`,`extra`,`privileges`,`column_comment`,`generation_expression`" +
                " , CONCAT('`',`column_name`,'` ',`column_type`,CASE WHEN `is_nullable`='NO' THEN ' NOT NULL' ELSE '' END ,CASE WHEN ISNULL(`column_default`) THEN '' ELSE CONCAT(' DEFAULT ',`column_default`) END ,CASE WHEN ISNULL(`extra`) THEN '' ELSE CONCAT(' ',`extra`) END ,CASE WHEN ISNULL(`column_comment`) THEN '' ELSE CONCAT(' COMMENT ',`column_comment`) END) ddl" +
                " from information_schema.columns where table_schema = (select database())") +
                " and `table_name` = '" +
                tableName +
                "'" +
                " order by `ordinal_position`";
        return connect.queryList(sql, ColumnEntity.class);
    }

    @Override
    public List<TableEntity> getTableList(String createTableSqls) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(createTableSqls, JdbcConstants.MYSQL);
        List<TableEntity> tableList = new ArrayList<>();
        // 表名列表，从alter table 中获取表备注
        List<TableEntity> tableNameList = new ArrayList<>();
        for (SQLStatement sqlStatement : stmtList) {
            if (sqlStatement instanceof MySqlCreateTableStatement) {
                // 获取表对象
                MySqlCreateTableStatement stmt = (MySqlCreateTableStatement) sqlStatement;
                TableEntity tableEntity = new TableEntity();
                tableEntity.setTableName(stmt.getTableSource().getExpr().toString().replace("`", ""));
                if (null == stmt.getComment()) {
                    tableEntity.setTableComment(tableEntity.getTableName());
                } else {
                    tableEntity.setTableComment(stmt.getComment().toString().replace("'", ""));
                }
                List<ColumnEntity> columns = new ArrayList<>();
                for (SQLTableElement column : stmt.getTableElementList()) {
                    if (column instanceof SQLColumnDefinition) {
                        ColumnEntity columnEntity = new ColumnEntity();
                        // 获取字段对象
                        SQLColumnDefinition columnDefinition = (SQLColumnDefinition) column;
                        columnEntity.setColumnName(columnDefinition.getName().getSimpleName().replace("`", ""));
                        if (null == columnDefinition.getComment()) {
                            columnEntity.setColumnComment(columnEntity.getColumnName());
                        } else {
                            columnEntity.setColumnComment(columnDefinition.getComment().toString().replace("'", ""));
                        }
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
                    } else if (column instanceof MySqlPrimaryKey) {
                        // 获取表内主键
                        MySqlPrimaryKey primaryKey = (MySqlPrimaryKey) column;
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
            } else if (sqlStatement instanceof SQLAlterTableStatement) {
                //  alter table a comment 'XX表';
                // 从alter table 中获取表comment
                SQLAlterTableStatement alterTableStatement = (SQLAlterTableStatement) sqlStatement;
                TableEntity tableNameEntity = new TableEntity();
                tableNameEntity.setTableName(alterTableStatement.getTableName());
                String comment = "COMMENT";
                for (int i = 0; i < alterTableStatement.getTableOptions().size(); i++) {
                    SQLAssignItem sqlAssignItem = alterTableStatement.getTableOptions().get(i);
                    tableNameEntity.setTableComment(sqlAssignItem.getValue().toString());
                }
                if (StrUtil.isEmpty(tableNameEntity.getTableComment())) {
                    alterTableStatement.getItems().forEach(i -> {
                        if (i instanceof MySqlAlterTableOption) {
                            MySqlAlterTableOption tableOption = (MySqlAlterTableOption) i;
                            if (comment.equals(tableOption.getName())) {
                                tableNameEntity.setTableComment(tableOption.getValue().toString());
                            }
                        }
                    });
                }
                tableNameList.add(tableNameEntity);
            }
        }
        tableList.forEach(t -> {
            if (t.getTableName().equals((t.getTableComment()))) {
                tableNameList.forEach(n -> {
                    if (n.getTableName().equals(t.getTableName())) {
                        if (StrUtil.isEmpty(n.getTableComment())) {
                            t.setTableComment(n.getTableName());
                        } else {
                            t.setTableComment(n.getTableComment().trim().replace("'", ""));
                        }
                    }
                });
            }
        });
        return tableList;
    }
}
