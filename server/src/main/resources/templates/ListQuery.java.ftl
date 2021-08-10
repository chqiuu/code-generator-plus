package ${codePackage}.query;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import com.alibaba.fastjson.JSONObject;

<#if lombokDataEnabled == 1>
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.time.LocalDate;
</#if>

/**
 * ${comment}列表查询对象
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
<#if lombokDataEnabled == 1>
@Data
</#if>
@ApiModel(value = "${commentEscape}列表查询对象")
public class ${classNameUpperCase}ListQuery implements Serializable${r'{'}

    private static final long serialVersionUID = 1L;
    /** 排序参数 */
    @ApiModelProperty(value = "排序参数")
    private String sortParam;
    /** 排序方式：正序asc，倒序desc，默认为desc */
    @ApiModelProperty(value = "排序方式：正序asc，倒序desc，默认为desc")
    private String sortord;

//TODO 当您看到这个后您应该自己修改模板增减规则
<#list columns as column>
    <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    /** ${column.commentEscape} ${column.columnDetail} */
    @ApiModelProperty(value = "${column.commentEscape}")
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