package ${codePackage}.query;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

import ${codePackage}.entity.${classNameUpperCase}Entity;

<#if lombokDataEnabled == 1>
import lombok.Data;
import lombok.EqualsAndHashCode;
import ${rootPackage}.common.validator.group.Default;
import ${rootPackage}.common.validator.group.Update;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "${commentEscape}分页查询对象")
public class ${classNameUpperCase}PageQuery implements Serializable${r'{'}

    private static final long serialVersionUID = 1L;
    /**
    * 排序参数
    */
    @ApiModelProperty(value = "排序参数")
    private String sortParam;
    /**
    * 排序方式：正序asc，倒序desc，默认为desc
    */
    @ApiModelProperty(value = "排序方式：正序asc，倒序desc，默认为desc")
    private String sortord;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer current = 1;
    /**
     * 每页显示条数
     */
    @ApiModelProperty(value = "每页显示条数")
    private Integer size = 10;

//TODO 当您看到这个后您应该自己修改模板增减规则
<#list columns as column>
    <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    /**
     * ${column.commentEscape} ${column.columnDetail}
     */
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