package com.chqiuu.cgp.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 生成器上下文数据
 *
 * @author chqiu
 */
@Data
public class GeneratorDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 表的名称
     */
    private String tableName;
    /**
     * 表的备注
     */
    private String comment;
    /**
     * 表的主键
     */
    private ColumnDto pk;
    /**
     * 表的列名(不包含主键)
     */
    private List<ColumnDto> columns;
    /**
     * 类名(第一个字母大写)，如：sys_user => SysUser
     */
    private String classNameUpperCase;
    /**
     * 类名(第一个字母小写)，如：sys_user => sysUser
     */
    private String classNameLowerCase;
    /**
     * 表名首字母缩写小写
     */
    private String acronymLowerCase;
    /**
     * 表名首字母大写
     */
    private String acronymUpperCase;
    /**
     * 包名
     */
    private String codePackage = "com.chqiuu";
    /**
     * URI修改为缩写
     */
    private String pathName;
    /**
     * 作者
     */
    private String author = "chqiuu";
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 是否为MyBatis-Plus
     */
    private int plusEnabled;
    /**
     * 是否启用map查询功能
     */
    private int mapQueryEnabled;
    /**
     * 是否启用@Data注解
     */
    private int lombokDataEnabled;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
