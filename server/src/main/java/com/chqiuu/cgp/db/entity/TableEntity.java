package com.chqiuu.cgp.db.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据库表信息
 *
 * @author chqiu
 */
@Data
public class TableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 数据表登记目录
     * `TABLE_CATALOG` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableCatalog;
    /**
     * 数据表所属的数据库名
     * `TABLE_SCHEMA` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ''
     */
    private String tableSchema;
    /**
     * 表名称
     * `TABLE_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableName;
    /**
     * 表名称
     * `TABLE_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableJavaName;
    /**
     * 表类型[system view|base table]
     * `TABLE_TYPE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    private String tableType;
    /**
     * 使用的数据库引擎[MyISAM|CSV|InnoDB]
     * `ENGINE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
     */
    private String engine;
    /**
     * 版本，默认值10
     * `VERSION` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long version;
    /**
     * 行格式[Compact|Dynamic|Fixed]
     * `ROW_FORMAT` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
     */
    private String rowFormat;
    /**
     * 表里所存多少行数据
     * `TABLE_ROWS` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long tableRows;
    /**
     * 平均行长度
     * `AVG_ROW_LENGTH` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long avgRowLength;
    /**
     * 数据长度
     * `DATA_LENGTH` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long dataLength;
    /**
     * 最大数据长度
     * `MAX_DATA_LENGTH` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long maxDataLength;
    /**
     * 索引长度
     * `INDEX_LENGTH` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long indexLength;
    /**
     * 自由数据？
     * `DATA_FREE` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long dataFree;
    /**
     * 做自增主键的自动增量当前值
     * `AUTO_INCREMENT` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long autoIncrement;
    /**
     * 表的创建时间
     * `CREATE_TIME` datetime(0) DEFAULT NULL,
     */
    private LocalDateTime createTime;
    /**
     * 表的更新时间
     * `UPDATE_TIME` datetime(0) DEFAULT NULL,
     */
    private LocalDateTime updateTime;
    /**
     * 表的检查时间
     * `CHECK_TIME` datetime(0) DEFAULT NULL,
     */
    private LocalDateTime checkTime;
    /**
     * 表的字符校验编码集
     * `TABLE_COLLATION` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
     */
    private String tableCollation;
    /**
     * 校验和
     * `CHECKSUM` bigint(21) UNSIGNED DEFAULT NULL,
     */
    private Long checksum;
    /**
     * 创建选项
     * `CREATE_OPTIONS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
     */
    private String createOptions;
    /**
     * 表的注释、备注
     * `TABLE_COMMENT` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ''
     */
    private String tableComment;
    /**
     * 警告信息列表
     */
    private List<String> warningMessages;
    /**
     * 字段列表
     */
    private List<ColumnEntity> columns;
}
