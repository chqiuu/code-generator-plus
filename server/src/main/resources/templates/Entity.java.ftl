package ${codePackage}.entity;

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
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;</#if>

<#if plusEnabled == 1>
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableLogic;
</#if>
<#if lombokDataEnabled == 1>
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.time.LocalDate;
</#if>

/**
 * ${comment}
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
<#if lombokDataEnabled == 1>
@Data
</#if>
<#if apiVersion == 3>
@Schema(description = "${commentEscape}实体类")
<#else>
@ApiModel(value = "${commentEscape}实体类")
</#if>
<#if plusEnabled == 1>
@EqualsAndHashCode(callSuper = true)
@TableName("${tableName}")
public class ${classNameUpperCase}Entity extends Model${r'<'}${classNameUpperCase}Entity><#else>
public class ${classNameUpperCase}Entity implements Serializable</#if> ${r'{'}

    @Serial
    private static final long serialVersionUID = 1L;

    <#list columns as column>
    /** ${column.comment} ${column.columnDetail} */
    <#if plusEnabled == 1><#if column.columnName == pk.columnName>
    <#--设置表主键，并设置ID生成方式-->
    @TableId(value = "${column.columnName}"<#if column.attrType == 'Long'>, type = IdType.ASSIGN_ID<#elseif column.attrType == 'Integer'>, type = IdType.AUTO</#if>)
    </#if>
        <#if column.columnName == 'is_deleted'>
        <#--逻辑删除标识-->
    @TableLogic
        </#if>
    </#if>
<#if apiVersion == 3>
    @Schema(description = "${column.commentEscape}")
<#else>
    @ApiModelProperty(value = "${column.commentEscape}")
</#if><#if column.attrType == 'JSONObject'>
    @TableField(typeHandler = FastjsonTypeHandler.class)</#if>
    private ${column.attrType} ${column.attrNameLowerCase};
    </#list>
    <#if lombokDataEnabled == 0>
        <#list columns as column>
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
        </#list>
    </#if>
${r'}'}