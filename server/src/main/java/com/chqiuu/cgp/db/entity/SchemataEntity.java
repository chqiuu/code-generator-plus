package com.chqiuu.cgp.db.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据库实例名
 * SCHEMATA表：提供了当前mysql实例中所有数据库的信息。是show databases的结果取之此表。
 * select * from information_schema.SCHEMATA
 * @author chqiu
 */
@Data
public class SchemataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务目录名称
     */
    private String catalogName;
    /**
     * 数据库名称
     */
    private String schemaName;
    /**
     * 数据库编码
     */
    private String defaultCharacterSetName;
    /**
     * 数据库排序规则
     */
    private String defaultCollationName;
    private String sqlPath;
}
