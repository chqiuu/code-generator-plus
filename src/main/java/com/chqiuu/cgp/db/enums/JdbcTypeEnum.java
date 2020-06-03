package com.chqiuu.cgp.db.enums;

import lombok.Getter;

/**
 * JDBC对象枚举
 *
 * @author chqiu
 */
@Getter
public enum JdbcTypeEnum {

    /**
     * 日期类型
     */
    DATE("DATE", "LocalDate"),
    /**
     * 时间类型
     */
    DATETIME("DATETIME", "LocalDateTime"),
    TIMESTAMP("TIMESTAMP", "LocalDateTime"),
    INTERVAL("INTERVAL", "LocalDateTime"),
    TIME("TIME", "LocalDateTime"),
    DATETIME2("DATETIME2", "LocalDateTime"),

    /**
     * 字符串类型
     */
    NCHAR("NCHAR", "String"),
    CHAR("CHAR", "String"),
    NVARCHAR2("NVARCHAR2", "String"),
    VARCHAR2("VARCHAR2", "String"),
    NVARCHAR("NVARCHAR", "String"),
    VARCHAR("VARCHAR", "String"),
    MEDIUMBLOB("MEDIUMBLOB", "String"),
    LONGBLOB("LONGBLOB", "String"),
    MEDIUMTEXT("MEDIUMTEXT", "String"),
    LONGTEXT("LONGTEXT", "String"),
    CLOB("CLOB", "String"),
    NCLOB("NCLOB", "String"),
    BLOB("BLOB", "String"),
    NBLOB("NBLOB", "String"),
    BFILE("BFILE", "String"),
    XML("XML", "String"),
    IMAGE("IMAGE", "String"),
    TEXT("TEXT", "String"),

    /**
     * JSON格式
     */
    JSON("JSON", "JsonObject"),

    /**
     * Integer类型
     */
    NUMBER("NUMBER", "Integer"),
    INTEGER("INTEGER", "Integer"),
    INT("INT", "Integer"),
    TINYINT("TINYINT", "Integer"),
    SMALLINT("SMALLINT", "Integer"),
    BIT("BIT", "Integer"),
    INT_UNSIGNED("INT UNSIGNED", "Integer"),
    TINYINT_UNSIGNED("TINYINT UNSIGNED", "Integer"),

    /**
     * Long类型
     */
    LONG("LONG", "Long"),
    LONG_UNSIGNED("LONG UNSIGNED", "Long"),
    BIGINT("BIGINT", "Long"),
    UNSIGNED("UNSIGNED", "Long"),
    BIGINT_UNSIGNED("BIGINT UNSIGNED", "Long"),
    /**
     * BigDecimal类型
     */
    MONEY("MONEY", "BigDecimal"),
    NUMERIC("NUMERIC", "BigDecimal"),
    /**
     * Double类型
     */
    BINARY_DOUBLE("BINARY DOUBLE", "Double"),
    NUBINARY_FLOATMBER("NUBINARY FLOATMBER", "Double"),
    FLOAT("FLOAT", "Double"),
    REAL("REAL", "Double"),
    DOUBLE("DOUBLE", "Double");

    private final String jdbcType;
    private final String javaType;

    JdbcTypeEnum(String jdbcType, String javaType) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
    }

    public static JdbcTypeEnum getByJdbcType(String jdbcType) {
        if (null == jdbcType) {
            return null;
        }
        for (JdbcTypeEnum e : values()) {
            if (e.getJdbcType().equals(jdbcType.toUpperCase())) {
                return e;
            }
        }
        return null;
    }

    public static JdbcTypeEnum getByJavaType(String javaType) {
        if (null == javaType) {
            return null;
        }
        for (JdbcTypeEnum e : values()) {
            if (e.getJavaType().equals(javaType)) {
                return e;
            }
        }
        return null;
    }
}
