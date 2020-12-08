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
    @NotNull(message = "是否为MyBatis-Plus不能为空")
    @ApiModelProperty(value = "是否为MyBatis-Plus")
    private Boolean isPlus;
}
