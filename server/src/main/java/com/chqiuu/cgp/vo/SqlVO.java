package com.chqiuu.cgp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * SQL脚本录入信息
 *
 * @author chqiu
 */
@Data
@ApiModel(value = "SQL脚本录入信息")
public class SqlVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库类型
     */
    @NotNull(message = "数据库类型不能为空")
    @ApiModelProperty(value = "数据库类型")
    private String dbType;
    /**
     * 数据库脚本语句
     */
    @NotNull(message = "数据库脚本语句不能为空")
    @ApiModelProperty(value = "数据库脚本语句")
    private String ddl;
}
