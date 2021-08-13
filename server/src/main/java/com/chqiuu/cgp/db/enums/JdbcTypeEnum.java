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
    YEAR("YEAR", "LocalDate"),

    /**
     * 字符串类型
     */
    NCHAR("NCHAR", "String"),
    CHAR("CHAR", "String"),
    NVARCHAR2("NVARCHAR2", "String"),
    VARCHAR2("VARCHAR2", "String"),
    NVARCHAR("NVARCHAR", "String"),
    VARCHAR("VARCHAR", "String"),
    MEDIUMTEXT("MEDIUMTEXT", "String"),
    TINYTEXT("TINYTEXT", "String"),
    LONGTEXT("LONGTEXT", "String"),
    BFILE("BFILE", "String"),
    XML("XML", "String"),
    IMAGE("IMAGE", "String"),
    TEXT("TEXT", "String"),

    ENUM("ENUM", "String"),
    SET("SET", "String"),

    TINYBLOB("TINYBLOB", "byte[]"),
    MEDIUMBLOB("MEDIUMBLOB", "byte[]"),
    LONGBLOB("LONGBLOB", "byte[]"),
    CLOB("CLOB", "byte[]"),
    NCLOB("NCLOB", "byte[]"),
    BLOB("BLOB", "byte[]"),
    NBLOB("NBLOB", "byte[]"),
    BINARY("BINARY", "byte[]"),
    VARBINARY("VARBINARY", "byte[]"),
    /**
     * JSON格式
     */
    JSON("JSON", "JSONObject"),

    BOOL("BOOL", "Boolean"),
    BOOLEAN("BOOLEAN", "Boolean"),

    /**
     * Integer类型
     */
    NUMBER("NUMBER", "Integer"),
    INTEGER("INTEGER", "Integer"),
    INT("INT", "Integer"),
    TINYINT("TINYINT", "Integer"),
    SMALLINT("SMALLINT", "Integer"),
    MEDIUMINT("MEDIUMINT", "Integer"),
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
     * BigDecimal类型 decimal
     */
    MONEY("MONEY", "BigDecimal"),
    NUMERIC("NUMERIC", "BigDecimal"),
    DECIMAL("DECIMAL", "BigDecimal"),
    /**
     * Double类型
     */
    BINARY_DOUBLE("BINARY DOUBLE", "Double"),
    NUBINARY_FLOATMBER("NUBINARY FLOATMBER", "Double"),
    FLOAT("FLOAT", "Float"),
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
