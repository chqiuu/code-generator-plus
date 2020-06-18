package com.chqiuu.cgp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
     * 是否生成MyBatis-Plus模式代码
     */
    @NotNull(message = "MyBatis-Plus支持不能为空")
    @ApiModelProperty(value = "MyBatis-Plus支持")
    private Boolean isPlus;
    /**
     * 带生成的数据库表列表
     */
    @Valid
    @ApiModelProperty(value = "带生成的数据库表列表")
    private List<GeneratorTableVO> tables;
}
