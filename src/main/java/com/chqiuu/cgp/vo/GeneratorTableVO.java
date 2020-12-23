package com.chqiuu.cgp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 脚本生成录入表信息
 *
 * @author chqiu
 */
@Data
@ApiModel(value = "脚本生成录入表信息")
public class GeneratorTableVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表名称
     */
    @NotNull(message = "表名称不能为空")
    @ApiModelProperty(value = "表名称")
    private String tableName;
    /**
     * 表的注释、备注
     */
    @NotNull(message = "表的注释、备注不能为空")
    @ApiModelProperty(value = "表的注释、备注")
    private String tableComment;
    /**
     * 表所属模块
     */
    @NotNull(message = "表所属模块不能为空")
    @ApiModelProperty(value = "表所属模块")
    private String module;
    /**
     * Controller中URL映射名称，如：/admin/user。用于 Controller中@RequestMapping注解
     */
    @NotNull(message = "Controller中URL映射名称不能为空")
    @ApiModelProperty(value = "Controller中URL映射名称")
    private String mappingName;
}
