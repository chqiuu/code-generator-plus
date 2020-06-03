package ${codePackage}.vo;

import com.cspm.sbims.base.entity.BannerInfoEntity;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

<#if plusEnabled == 1>
    import ${codePackage}.dto.${classNameUpperCase}ListDTO;
</#if>

<#if lombokDataEnabled == 1>
import lombok.Data;
import lombok.EqualsAndHashCode;
</#if>

/**
* ${comment}查询参数
*
* @author ${author}
* @date ${createTime?date("yyyy-MM-dd")}
*/

<#if lombokDataEnabled == 1>
@Data
</#if>
@ApiModel(value = "${comment}查询参数")

public class ${classNameUpperCase}PageParamVo implements Serializable
${r'{'}

    private static final long serialVersionUID = 1L;

/** 当前页 */
@ApiModelProperty(value = "当前页")
private Integer current;
/** 每页显示条数 */
@ApiModelProperty(value = "每页显示条数")
private Integer size;

//TODO 当您看到这个后您应该自己修改模板增减规则
    <#list columns as column>
        <#if column.columnName == pk.columnName || exclusionShowColumns?contains(column.columnName) || column.dataType?contains('text')>
            <#--主键、删除标识、创建时间、更新时间、长字段不进入查询列-->
            <#else>
        /** ${column.comment} ${column.columnDetail} */
        @ApiModelProperty(value = "${column.comment}")
        private ${column.attrType} ${column.attrNameLowerCase};
        </#if>
    </#list>

    <#if lombokDataEnabled == 0>
        <#list columns as column>
        <#if column.columnName == pk.columnName || exclusionShowColumns?contains(column.columnName) || column.dataType?contains('text')>
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