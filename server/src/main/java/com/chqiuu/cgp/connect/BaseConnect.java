package com.chqiuu.cgp.connect;

import com.chqiuu.cgp.db.enums.DriverClassEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 数据库连接抽象类
 *
 * @author chqiu
 */
@Slf4j
public abstract class BaseConnect {
    /**
     * 根据类型获取数据源
     *
     * @return druid数据源
     */
    public abstract DataSource getDataSource();

    /**
     * 数据库枚举
     *
     * @return 数据库枚举
     */
    public abstract DriverClassEnum getDriverClassEnum();

    /**
     * 执行增、删、改语句
     *
     * @param typeSql sql语句
     * @return 影响行数
     */
    public int exeSql(String typeSql) {
        int intReturn = 0;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            log.debug("executeUpdate SQL: {}", typeSql);
            intReturn = stmt.executeUpdate(typeSql);
        } catch (SQLException e) {
            log.error("Message: {} ,sql: {}", e.getMessage(), typeSql);
        } finally {
            close(conn, stmt);
        }
        return intReturn;
    }

    /**
     * 批量执行SQL
     *
     * @param sqls SQL列表
     */
    public void exeBatchSql(List<String> sqls) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            for (String sql : sqls) {
                log.debug("executeUpdate SQL: {}", sql);
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, stmt);
        }
    }

    /**
     * 根据查询条件获取查询列表
     *
     * @param querySql 查询语句
     * @return 结果集，通用对象
     */
    public List<LinkedHashMap<String, Object>> queryList(String querySql) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            log.debug("executeQuery SQL: {}", querySql);
            resultSet = stmt.executeQuery(querySql);
            // 获得结果集结构信息,元数据
            ResultSetMetaData md = resultSet.getMetaData();
            // 获得列数
            int columnCount = md.getColumnCount();
            while (resultSet.next()) {
                LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnLabel(i), resultSet.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            close(conn, stmt);
        }
        return list;
    }

    /**
     * 根据查询语句获取查询结果集
     *
     * @param querySql 查询语句
     * @param clazz    对象
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(String querySql, Class<T> clazz) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        List<T> list = new ArrayList<T>();
        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            log.debug("executeQuery SQL: {}", querySql);
            resultSet = stmt.executeQuery(querySql);
            list = convertMetaData(resultSet, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Message：{} ；SQL: {}", e.getMessage(), querySql);
        } finally {
            close(conn, stmt);
        }
        return list;
    }

    /**
     * 根据查询语句获取单条查询结果
     *
     * @param querySql 查询语句
     * @param clazz    对象
     * @param <T>
     * @return
     */
    public <T> T queryOne(String querySql, Class<T> clazz) {
        List<T> list = queryList(querySql, clazz);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private void close(Connection conn, Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 将查询结果集转换为指定对象
     *
     * @param rs    ResultSet
     * @param clazz 对象
     * @param <T>   泛型
     * @return 指定对象
     * @throws SQLException 异常
     */
    private <T> List<T> convertMetaData(ResultSet rs, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException {
        List<T> tList = new ArrayList<T>();
        T t = null;
        while (rs.next()) {
            t = clazz.newInstance();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String name = rsmd.getColumnName(i);
                Field field = getDeclaredFieldIgnoreCase(clazz, StringUtils.uncapitalize(WordUtils.capitalizeFully(name, new char[]{'_'}).replace("_", "")));
                if (null == field) {
                    continue;
                }
                field.setAccessible(true);
                if (field.getType().equals(String.class)) {
                    field.set(t, rs.getString(name));
                } else if (field.getType().equals(Integer.class)) {
                    field.set(t, rs.getInt(name));
                } else if (field.getType().equals(Long.class)) {
                    field.set(t, rs.getLong(name));
                } else if (field.getType().equals(Double.class)) {
                    field.set(t, rs.getDouble(name));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(t, rs.getBigDecimal(name));
                } else if (field.getType().equals(Date.class)) {
                    field.set(t, rs.getDate(name));
                } else {
                    field.set(t, rs.getObject(name));
                }
            }
            tList.add(t);
        }
        return tList;
    }

    /**
     * 查找属性且不区分大小写，忽略不匹配的字段
     *
     * @param clazz     对象
     * @param fieldName 属性
     * @return Field
     */
    private Field getDeclaredFieldIgnoreCase(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return field;
            }
        }
        log.warn("没有找到对应的属性: {}", fieldName);
        //没有找到对应的属性返回空
        return null;
    }

}
