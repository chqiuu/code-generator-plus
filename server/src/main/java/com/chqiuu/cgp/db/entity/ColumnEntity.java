package com.chqiuu.cgp.db.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 字段信息
 *
 * @author chqiu
 */
@Data
public class ColumnEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * MySQL官方文档中说，这个字段值永远是def，但没写这个字段是干嘛用的。
     * 网上有把这个叫表限定符的，有叫登记目录的。作用疑似是和其他种类的数据库做区分。
     * `TABLE_CATALOG` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableCatalog;
    /**
     * 表格所属的库
     * `TABLE_SCHEMA` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableSchema;
    /**
     * 表名
     * `TABLE_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableName;
    /**
     * 字段名
     * `COLUMN_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String columnName;
    /**
     * 字段标识。
     * 其实就是字段编号，从1开始往后排。
     * `ORDINAL_POSITION` bigint(21) UNSIGNED NOT NULL DEFAULT 0,
     */
    private Long ordinalPosition;
    /**
     * 字段默认值
     * `COLUMN_DEFAULT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
     */
    private String columnDefault;
    /**
     * 字段是否可以是NULL。
     * 该列记录的值是YES或者NO。
     * `IS_NULLABLE` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String isNullable;
    /**
     * 数据类型
     * 里面的值是字符串，比如varchar，float，int。
     * `DATA_TYPE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String dataType;
    /**
     * 字段的最大字符数。
     * 假如字段设置为varchar(50)，那么这一列记录的值就是50。
     * 该列只适用于二进制数据，字符，文本，图像数据。其他类型数据比如int，float，datetime等，在该列显示为NULL。
     * `CHARACTER_MAXIMUM_LENGTH` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long characterMaximumLength;
    /**
     * 字段的最大字节数。
     * 和最大字符数一样，只适用于二进制数据，字符，文本，图像数据，其他类型显示为NULL。
     * 和最大字符数的数值有比例关系，和字符集有关。比如UTF8类型的表，最大字节数就是最大字符数的3倍。
     * `CHARACTER_OCTET_LENGTH` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long characterOctetLength;
    /**
     * 数字精度。
     * 适用于各种数字类型比如int，float之类的。
     * 如果字段设置为int(10)，那么在该列保存的数值是9，少一位，还没有研究原因。
     * 如果字段设置为float(10,3)，那么在该列报错的数值是10。
     * 非数字类型显示为在该列NULL。
     * `NUMERIC_PRECISION` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long numericPrecision;
    /**
     * 小数位数。
     * 和数字精度一样，适用于各种数字类型比如int，float之类。
     * 如果字段设置为int(10)，那么在该列保存的数值是0，代表没有小数。
     * 如果字段设置为float(10,3)，那么在该列报错的数值是3。
     * 非数字类型显示为在该列NULL。
     * `NUMERIC_SCALE` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long numericScale;
    /**
     * datetime类型和SQL-92interval类型数据库的子类型代码。
     * 我本地datetime类型的字段在该列显示为0。
     * 其他类型显示为NULL。
     * `DATETIME_PRECISION` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long datetimePrecision;
    /**
     * 字段字符集名称。比如utf8。
     * `CHARACTER_SET_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
     */
    private String characterSetName;
    /**
     * 字符集排序规则。
     * 比如utf8_general_ci，是不区分大小写一种排序规则。utf8_general_cs，是区分大小写的排序规则。
     * `COLLATION_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
     */
    private String collationName;
    /**
     * 字段类型。比如float(9,3)，varchar(50)。
     * `COLUMN_TYPE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
     */
    private String columnType;
    /**
     * 索引类型。
     * 可包含的值有PRI，代表主键，UNI，代表唯一键，MUL，可重复。
     * `COLUMN_KEY` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String columnKey;
    /**
     * 其他信息。
     * 比如主键的auto_increment。
     * `EXTRA` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String extra;
    /**
     * 权限
     * 多个权限用逗号隔开，比如 select,insert,update,references
     * `PRIVILEGES` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String privileges;
    /**
     * 字段注释
     * `COLUMN_COMMENT` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String columnComment;
    /**
     * 组合字段的公式。
     * `GENERATION_EXPRESSION` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
     */
    private String generationExpression;

    /**
     * 脚本语句
     */
    private String ddl;
}
