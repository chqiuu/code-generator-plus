package com.chqiuu.cgp.db;

import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.SchemataEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * PostgreSQL数据库
 *
 * @author chqiu
 */
public class PostgreSqlDatabase extends BaseDatabase {
    @Override
    public List<SchemataEntity> queryDatabaseList(BaseConnect connect) {
        return connect.queryList("SELECT datname AS schema_name FROM pg_database;", SchemataEntity.class);
    }

    @Override
    public List<TableEntity> queryTableList(BaseConnect connect, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t1.tablename as tableName, obj_description(relfilenode, 'pg_class') as tableComment from pg_tables t1, pg_class t2" +
                " where t1.tablename not like 'pg%' and t1.tablename not like 'sql_%' and t1.tablename = t2.relname");
        if (StringUtils.isNotBlank(tableName)) {
            sql.append(" and t1.tablename like '%");
            sql.append(tableName);
            sql.append("%'");
        }
        sql.append(" order by t1.tablename desc");
        return connect.queryList(sql.toString(), TableEntity.class);
    }

    @Override
    public TableEntity queryTable(BaseConnect connect, String tableName) {
        String sql = "select t1.tablename as tableName, obj_description(relfilenode, 'pg_class') as tableComment, now() as createTime from pg_tables t1, pg_class t2" +
                " where t1.tablename = t2.relname" +
                " and t1.tablename = '" +
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
        String sql = "select t2.attname as columnName, pg_type.typname as dataType, col_description(t2.attrelid,t2.attnum) as columnComment, '' as extra," +
                " (CASE t3.contype WHEN 'p' THEN 'PRI' ELSE '' END) as columnKey" +
                " from pg_class as t1, pg_attribute as t2 inner join pg_type on pg_type.oid = t2.atttypid" +
                " left join pg_constraint t3 on t2.attnum = t3.conkey[1] and t2.attrelid = t3.conrelid" +
                " where and t2.attrelid = t1.oid and t2.attnum > 0" +
                " and t1.relname = '" + tableName + "'";
        return connect.queryList(sql, ColumnEntity.class);
    }

    @Override
    public List<TableEntity> getTableList(String createTableSqls) {
        //TODO
        return null;
    }
}
