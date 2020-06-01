package com.chqiuu.cgp.db;

import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.TableEntity;

import java.util.List;

/**
 * 数据库抽象类
 *
 * @author chqiu
 */
public abstract class BaseDatabase {

    /**
     * 根据创建数据库表脚本生成表对象列表
     *
     * @param connect   数据库连接
     * @param tableName 表名
     * @return 数据库表列表
     */
    public abstract List<TableEntity> queryTableList(BaseConnect connect, String tableName);

    /**
     * 查询表信息
     *
     * @param connect   数据库连接
     * @param tableName 待查询表
     * @return 表信息
     */
    public abstract TableEntity queryTable(BaseConnect connect, String tableName);

    /**
     * 查询表字段信息
     *
     * @param connect   数据库连接
     * @param tableName 待查询表
     * @return 表字段信息
     */
    public abstract List<ColumnEntity> queryColumns(BaseConnect connect, String tableName);
    /**
     * 通过表名模糊查询
     *
     * @param createTableSqls 创建数据库表脚本
     * @return 数据库表列表
     */
    public abstract List<TableEntity> getTableList(String createTableSqls);
}
