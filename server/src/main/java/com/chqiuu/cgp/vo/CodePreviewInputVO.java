package com.chqiuu.cgp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 预览代码录入信息
 *
 * @author chqiu
 */
@Data
@ApiModel(value = "预览代码录入信息")
public class CodePreviewInputVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "完整包名。如：com.chqiuu.user")
    private String codePackage;

    @NotNull(message = "项目主体包名不能为空")
    @ApiModelProperty(value = "项目主体包名。如：com.chqiuu")
    private String rootPackage;

    @NotNull(message = "模块名不能为空")
    @ApiModelProperty(value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user")
    private String moduleName;

    @NotNull(message = "创建人不能为空")
    @ApiModelProperty(value = "创建人。用于注解")
    private String author;

    @NotNull(message = "表名不能为空")
    @ApiModelProperty(value = "表名")
    private String table;
    /**
     * Controller中URL映射名称，如：/admin/user。用于 Controller中@RequestMapping注解
     */
    @NotNull(message = "Controller中URL映射名称不能为空")
    @ApiModelProperty(value = "Controller中URL映射名称")
    private String mappingName;

    /**
     * 是否生成Service接口
     */
    @NotNull(message = "是否生成Service接口不能为空")
    @ApiModelProperty(value = "是否生成Service接口")
    private Boolean isServiceInterface;
    @NotNull(message = "是否为MyBatis-Plus不能为空")
    @ApiModelProperty(value = "是否为MyBatis-Plus")
    private Boolean isPlus;
    @NotNull(message = "是否为Layuimini不能为空")
    @ApiModelProperty(value = "是否为Layuimini")
    private Boolean isLayuimini;
    /**
     * 是否启用mapstruct对象转换工具
     */
    @NotNull(message = "是否启用mapstruct对象转换支持不能为空")
    @ApiModelProperty(value = "是否启用mapstruct对象转换")
    private Boolean isMapstructEnabled;

    @NotNull(message = "请设置需要生成的方法列表")
    @ApiModelProperty(value = "需要生成的方法列表")
    private String[] genMethods;
}
