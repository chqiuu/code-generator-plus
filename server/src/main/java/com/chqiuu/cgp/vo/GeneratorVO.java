package com.chqiuu.cgp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author chqiu
 */
@Data
@ApiModel(value = "脚本生成录入信息")
public class GeneratorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目主包名 如：com.chqiuu
     */
    @NotNull(message = "项目主包名不能为空")
    @ApiModelProperty(value = "项目主包名")
    private String rootPackage;
    /**
     * 创建人 如：chqiuu
     */
    @NotNull(message = "创建人不能为空")
    @ApiModelProperty(value = "创建人")
    private String author;
    /**
     * 是否生成Service接口
     */
    @NotNull(message = "是否生成Service接口不能为空")
    @ApiModelProperty(value = "是否生成Service接口")
    private Boolean isServiceInterface;
    /**
     * 是否生成MyBatis-Plus模式代码
     */
    @NotNull(message = "MyBatis-Plus支持不能为空")
    @ApiModelProperty(value = "MyBatis-Plus支持")
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
    /**
     * 带生成的数据库表列表
     */
    @Valid
    @Size(min = 1, message = "必须先选择一张表")
    @ApiModelProperty(value = "带生成的数据库表列表")
    private List<GeneratorTableVO> tables;

    @NotNull(message = "请设置需要生成的方法列表")
    @ApiModelProperty(value = "需要生成的方法列表")
    private String[] genMethods;
}
