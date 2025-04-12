package ${codePackage}.query;

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

<#if lombokDataEnabled == 1>
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.time.LocalDate;
</#if>

/**
 * ${comment}分页查询对象
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
<#if lombokDataEnabled == 1>
@Data
</#if>
<#if apiVersion == 3>
@Schema(description = "${commentEscape}分页查询对象")
<#else>
@ApiModel(value = "${commentEscape}分页查询对象")
</#if>
public class ${classNameUpperCase}PageQuery implements Serializable${r'{'}

    private static final long serialVersionUID = 1L;
    /** 排序参数 */
<#if apiVersion == 3>
    @Schema(description = "排序参数")
<#else>
    @ApiModelProperty(value = "排序参数")
</#if>
    private String sortParam;
    /** 排序方式：正序asc，倒序desc，默认为desc */
<#if apiVersion == 3>
    @Schema(description = "排序方式：正序asc，倒序desc，默认为desc")
<#else>
    @ApiModelProperty(value = "排序方式：正序asc，倒序desc，默认为desc")
</#if>
    private String sortord;
    /** 当前页 */
<#if apiVersion == 3>
    @Schema(description = "当前页")
<#else>
    @ApiModelProperty(value = "当前页")
</#if>
    private Integer current = 1;
    /** 每页显示条数 */
<#if apiVersion == 3>
    @Schema(description = "每页显示条数")
<#else>
    @ApiModelProperty(value = "每页显示条数")
</#if>
    private Integer size = 10;

//TODO 当您看到这个后您应该自己修改模板增减规则
<#list columns as column>
    <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    /** ${column.commentEscape} ${column.columnDetail} */
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
${r'}'}