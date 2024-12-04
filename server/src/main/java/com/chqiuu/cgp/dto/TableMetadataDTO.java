package com.chqiuu.cgp.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 表生成器元数据
 *
 * @author chqiu
 */
@Data
public class TableMetadataDTO implements Serializable {

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
     * 表的备注转义 ，转义特殊字符防止生成后代码报错
     */
    private String commentEscape;
    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * 表的主键
     */
    private ColumnDto pk;
    /**
     * 表的列信息(不包含主键)
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
     * 表名首字母缩写小写 如：sys_user => su
     */
    private String acronymLowerCase;
    /**
     * 表名首字母大写 如：sys_user => SU
     */
    private String acronymUpperCase;
    /**
     * 项目主包名
     */
    private String rootPackage = "com.chqiuu";
    /**
     * 项目公共类包名 如：com.chqiuu.common 主包名去掉.modules
     * 若主包名为com.chqiuu.modules 公共类包名 com.chqiuu.common
     * 若主包名为com.chqiuu 公共类包名 com.chqiuu.common
     */
    private String commonPackage;
    /**
     * 所属模块名称
     */
    private String moduleName;
    /**
     * 模块对应包名
     */
    private String codePackage;
    /**
     * Controller中URL映射名称，如：/admin/user。用于 Controller中@RequestMapping注解
     */
    private String mappingName;
    /**
     * 作者
     */
    private String author = "chqiuu";
    /**
     * 是否存在逻辑删除字段 is_deleted
     */
    private Integer logicDelete = 0;
    /**
     * 是否存在 BigDecimal 字段
     */
    private Integer hasBigDecimalAttr = 0;
    /**
     * 是否存在 JSON 格式字段
     */
    private Integer hasJsonAttr = 0;
    /**
     * 排除显示的字段
     */
    private String exclusionShowColumns = "is_deleted,del_time,del_user,create_time,update_time,gmt_create,gmt_modified";
    /**
     * 是否生成通用方法
     */
    private GeneralMethodEnabledDTO generalMethod;
    /**
     * 是否生成Service接口
     */
    private Integer serviceInterfaceEnabled = 0;
    /**
     * 是否支持MyBatis-Plus
     */
    private Integer plusEnabled = 0;
    /**
     * 是否启用map查询功能
     */
    private Integer mapQueryEnabled = 0;
    /**
     * 是否启用@Data注解
     */
    private Integer lombokDataEnabled = 0;
    /**
     * 是否启用mapstruct对象转换工具
     */
    private Integer mapstructEnabled = 0;
    /**
     * 是否启用Vue后台管理功能
     */
    private Integer vueEnabled = 0;
    /**
     * 是否启用layuimini后台管理功能
     */
    private Integer layuiminiEnabled = 0;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
