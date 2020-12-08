package com.chqiuu.cgp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "连接数据库录入信息")
public class ConnectDatabaseInputVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "数据库类型不能为空")
    @ApiModelProperty(value = "数据库类型")
    private String dbType;
    @NotNull(message = "服务器地址不能为空")
    @ApiModelProperty(value = "服务器地址")
    private String server;
    @NotNull(message = "端口号不能为空")
    @ApiModelProperty(value = "端口号")
    private Integer port;
    @NotNull(message = "数据库名不能为空")
    @ApiModelProperty(value = "数据库名")
    private String database;
    @NotNull(message = "登录名不能为空")
    @ApiModelProperty(value = "登录名")
    private String username;
    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}
