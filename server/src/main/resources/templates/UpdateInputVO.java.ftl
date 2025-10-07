package ${codePackage}.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
<#if apiVersion == 3>
import io.swagger.v3.oas.annotations.media.Schema;
<#else>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if hasBigDecimalAttr==1 >
import java.math.BigDecimal;
</#if>
<#if hasJsonAttr==1 >
import com.alibaba.fastjson.JSONObject;
</#if>
import ${codePackage}.entity.${classNameUpperCase}Entity;

<#if lombokDataEnabled == 1>
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

<#if apiVersion == 3>
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
<#else>
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
</#if>
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>

/**
 * 修改${comment}录入信息
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
<#if lombokDataEnabled == 1>
@Data
</#if>
<#if apiVersion == 3>
@Schema(description = "${commentEscape}录入信息")
<#else>
@ApiModel(value = "${commentEscape}录入信息")
</#if>
public class ${classNameUpperCase}UpdateInputVO implements Serializable ${r'{'}

    @Serial
    private static final long serialVersionUID = 1L;
    //TODO 当您看到这个后您应该自己修改模板增减规则
    <#list columns as column>
        <#if exclusionShowColumns?contains(column.columnName)>
        <#else>
    /** ${column.comment} ${column.columnDetail} */
    @NotNull(message = "${column.commentEscape}不能为空")
            <#if column.attrType == 'String' && column.charlength?? && column.charlength < 10000>
    @Size(max = ${column.charlength}, message = "${column.commentEscape}不能超过{max}个字符")
            <#elseif column.attrType == 'Integer'>
    @Max(value = Integer.MAX_VALUE, message = "${column.commentEscape}不能超过{value}")
            <#elseif column.attrType == 'Long'>
    // @Max(value = Long.MAX_VALUE, message = "${column.commentEscape}不能超过{max}")
            <#elseif column.attrType == 'LocalDateTime'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            </#if>
<#if apiVersion == 3>
    @Schema(description = "${column.commentEscape}")
<#else>
    @ApiModelProperty(value = "${column.commentEscape}")
</#if>
    private ${column.attrType} ${column.attrNameLowerCase};
        </#if>
    </#list>
    <#if lombokDataEnabled == 0>
        <#list columns as column>
            <#if exclusionShowColumns?contains(column.columnName)>
            <#else>
    /**
     * 获取：${column.comment}
     *
     * @return ${column.attrNameLowerCase} ${column.comment}
     */
    public ${column.attrType} get${column.attrNameUpperCase}() {
        return ${column.attrNameLowerCase};
    }

    /**
     * 设置：${column.comment}
     *
     * @param ${column.attrNameLowerCase} ${column.comment}
     */
    public void set${column.attrNameUpperCase}(${column.attrType} ${column.attrNameLowerCase}) {
        this.${column.attrNameLowerCase} = ${column.attrNameLowerCase};
    }
            </#if>
        </#list>
    </#if>
    <#if mapstructEnabled == 0>
    /**
     * 输入对象转为实体类
     *
     * @return 实体类
     */
    public ${classNameUpperCase}Entity convertToEntity() {
        ${classNameUpperCase}Entity entity = new ${classNameUpperCase}Entity();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }
    </#if>
${r'}'}