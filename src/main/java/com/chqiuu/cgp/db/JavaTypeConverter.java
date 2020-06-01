package com.chqiuu.cgp.db;

import jdk.nashorn.internal.objects.annotations.Getter;

/**
 * jdbc类型转java类型转换器
 *
 * @author chqiu
 */
public class JavaTypeConverter {

    /**
     * 将JDBC类型转换为java数据类型
     *
     * @param type 类型
     * @return java数据类型
     */
    public static String jdbcTypeToJavaType(String type) {
        if (type == null) {
            return "";
        }

        if (isDate(type)) {
            return "java.time.Instant";
        } else if (isInteger(type)) {
            return "Integer";
        } else if (isLong(type)) {
            return "Long";
        } else if (isDouble(type)) {
            return "Double";
        } else if (isString(type)) {
            return "String";
        } else if (isJson(type)) {
            return "JsonObject";
        } else {
            return "Object";
        }
    }

    /**
     * 判断是否为时间类型
     *
     * @param type 时间类型
     * @return
     */
    public static boolean isDate(String type) {
        return type.equalsIgnoreCase("DATE") || type.equalsIgnoreCase("DATETIME")
                || type.equalsIgnoreCase("TIMESTAMP")
                || type.equalsIgnoreCase("INTERVAL") || type.equalsIgnoreCase("TIME")
                || type.equalsIgnoreCase("DATETIME2");
    }

    /**
     * 判断是否可为String类型
     *
     * @param type String类型
     * @return
     */
    public static boolean isString(String type) {
        return type.equalsIgnoreCase("NCHAR") || type.equalsIgnoreCase("CHAR") || type.equalsIgnoreCase("NVARCHAR2")
                || type.equalsIgnoreCase("VARCHAR2") || type.equalsIgnoreCase("NVARCHAR") || type.equalsIgnoreCase("VARCHAR")
                || type.equalsIgnoreCase("MEDIUMBLOB") || type.equalsIgnoreCase("MEDIUMTEXT") || type.equalsIgnoreCase("CLOB")
                || type.equalsIgnoreCase("NCLOB") || type.equalsIgnoreCase("BLOB") || type.equalsIgnoreCase("NBLOB") || type.equalsIgnoreCase("BFILE")
                || type.equalsIgnoreCase("XML") || type.equalsIgnoreCase("IMAGE") || type.equalsIgnoreCase("TEXT");
    }

    /**
     * 判断类型是否为JSON格式
     *
     * @param type JSON格式
     * @return
     */
    public static boolean isJson(String type) {
        return type.equals("JSON");
    }

    /**
     * 判断是否为Integer类型
     *
     * @param type Integer类型
     * @return
     */
    public static boolean isInteger(String type) {
        return type.equalsIgnoreCase("NUMBER") || type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("INT") || type.equalsIgnoreCase("TINYINT")
                || type.equalsIgnoreCase("SMALLINT") || type.equalsIgnoreCase("BIT") || type.equalsIgnoreCase("Int UNSIGNED")
                || type.equalsIgnoreCase("TINYINT UNSIGNED");
    }

    /**
     * 判断是否为Long类型
     *
     * @param type Long类型
     * @return
     */
    public static boolean isLong(String type) {
        return type.equalsIgnoreCase("Long") || type.equalsIgnoreCase("LONG UNSIGNED") || type.equalsIgnoreCase("BIGINT")
                || type.equalsIgnoreCase("unsigned") || type.equalsIgnoreCase("BIGINT UNSIGNED");
    }

    /**
     * 判断是否为Double类型
     *
     * @param type Double类型
     * @return
     */
    public static boolean isDouble(String type) {
        return type.equalsIgnoreCase("BINARY_DOUBLE") || type.equalsIgnoreCase("BINARY_FLOAT") || type.equalsIgnoreCase("FLOAT")
                || type.equalsIgnoreCase("DECIMAL") || type.equalsIgnoreCase("MONEY") || type.equalsIgnoreCase("NUMERIC")
                || type.equalsIgnoreCase("REAL") || type.equalsIgnoreCase("DOUBLE");
    }
}
