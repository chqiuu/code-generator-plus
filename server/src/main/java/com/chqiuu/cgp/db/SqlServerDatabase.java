package com.chqiuu.cgp.db;

import cn.hutool.core.util.StrUtil;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.SchemataEntity;
import com.chqiuu.cgp.db.entity.TableEntity;

import java.util.List;

/**
 * SQLServer数据库
 *
 * @author chqiu
 */
public class SqlServerDatabase extends BaseDatabase {

    @Override
    public List<SchemataEntity> queryDatabaseList(BaseConnect connect) {
        return connect.queryList("Select name AS schema_name FROM Master..SysDatabases order by Name", SchemataEntity.class);
    }

    @Override
    public List<TableEntity> queryTableList(BaseConnect connect, String tableName) {
        String sql = String.format("select * from ( select cast(so.name as varchar(500)) as tableName, cast(sep.value as varchar(500)) as\n" +
                        "        tableComment, getDate() as createTime\n" +
                        "        from sysobjects so left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0\n" +
                        "        where (xtype='U' or xtype='v') ) t where 1=1 %s" +
                        "        order by t.tableName"
                , StrUtil.isBlank(tableName) ? "" : " and t.tableName like '%" + tableName + "%'");
        return connect.queryList(sql, TableEntity.class);
    }

    @Override
    public TableEntity queryTable(BaseConnect connect, String tableName) {
        String sql = String.format("select * from (\n" +
                "   select cast(so.name as varchar(500)) as tableName, 'mssql' as engine,cast(sep.value as varchar(500)) as tableComment, getDate() as createTime\n" +
                "   from sysobjects so\n" +
                "   left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0\n" +
                "   where (xtype='U' or xtype='v')\n" +
                "  ) t where t.tableName='%s'", tableName);
        List<TableEntity> list = connect.queryList(sql, TableEntity.class);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ColumnEntity> queryColumns(BaseConnect connect, String tableName) {
        String sql = String.format("SELECT cast(b.NAME AS VARCHAR(500)) AS columnName, cast(sys.types.NAME AS VARCHAR(500)) AS dataType, cast(c.VALUE AS VARCHAR(500)) AS columnComment,\n" +
                "  (SELECT CASE count(1)WHEN 1 then 'PRI' ELSE '' END\n" +
                "   FROM syscolumns, sysobjects, sysindexes, sysindexkeys, systypes\n" +
                "   WHERE syscolumns.xusertype = systypes.xusertype\n" +
                "    AND syscolumns.id = object_id(A.NAME)\n" +
                "    AND sysobjects.xtype = 'PK'\n" +
                "    AND sysobjects.parent_obj = syscolumns.id\n" +
                "    AND sysindexes.id = syscolumns.id\n" +
                "    AND sysobjects.NAME = sysindexes.NAME\n" +
                "    AND sysindexkeys.id = syscolumns.id\n" +
                "    AND sysindexkeys.indid = sysindexes.indid\n" +
                "    AND syscolumns.colid = sysindexkeys.colid\n" +
                "    AND syscolumns.NAME = B.NAME ) as columnKey, '' as extra\n" +
                "  FROM ( select name, object_id from sys.tables UNION all select name, object_id from sys.views ) a\n" +
                "  INNER JOIN sys.COLUMNS b ON b.object_id = a.object_id\n" +
                "  LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id\n" +
                "  LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id\n" +
                "  WHERE a.NAME = '%s' and sys.types.NAME != 'sysname'", tableName);
        return connect.queryList(sql, ColumnEntity.class);
    }

    @Override
    public List<TableEntity> getTableList(String createTableSqls) {
        return null;
    }
}
