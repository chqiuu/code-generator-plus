package com.chqiuu.cgp.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 字段
 */
@Data
public class ColumnDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列名类型
     */
    private String dataType;
    /**
     * 列名备注
     */
    private String comment;
    /**
     * 列名描述转义 ，转义特殊字符防止生成后代码报错
     */
    private String commentEscape;
    /**
     * 属性名称(第一个字母大写)，如：user_name => UserName
     * column.attrNameUpperCase
     */
    private String attrNameUpperCase;
    /**
     * 属性名称(第一个字母小写)，如：user_name => userName
     * column.attrNameLowerCase
     */
    private String attrNameLowerCase;
    /**
     * 字符长度
     */
    private Long charlength;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * auto_increment
     */
    private String extra;
    /**
     * 列名详情
     */
    private String columnDetail;
}
