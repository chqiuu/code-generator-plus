package ${codePackage}.vo;

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
* ${comment}录入信息
*
* @author ${author}
* @date ${createTime?date("yyyy-MM-dd")}
*/

<#if lombokDataEnabled == 1>
@Data
</#if>
@ApiModel(value = "${comment}录入信息")
public class ${classNameUpperCase}InputVO implements Serializable${r'{'}

    private static final long serialVersionUID = 1L;
    //TODO 当您看到这个后您应该自己修改模板增减规则
    <#list columns as column>
        <#if exclusionShowColumns?contains(column.columnName)>
        <#else>
            /** ${column.comment} ${column.columnDetail} */
            <#if column.columnName == pk.columnName>
                @NotNull(message = "${column.comment}不能为空", groups = Update.class)
            <#else>
                @NotNull(message = "${column.comment}不能为空", groups = Default.class)
            </#if>
            <#if column.attrType == 'String' && column.charlength??>
                @Length(max = ${column.charlength}, message = "${column.comment}不能超过{max}个字符", groups = Default.class)
            <#elseif column.attrType == 'Integer'>
                @Max(value = Integer.MAX_VALUE, message = "${column.comment}不能超过{value}", groups = Default.class)
            <#elseif column.attrType == 'Long'>
                @Max(value = Long.MAX_VALUE, message = "${column.comment}不能超过{max}", groups = Default.class)
            </#if>
                @ApiModelProperty(value = "${column.comment}")
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
        /**
        * 输入对象转为实体类
        *
        * @return 实体类
        */
        public ${classNameUpperCase}Entity convertToEntity() {
            ${classNameUpperCase}Entity entity = new ${classNameUpperCase}Entity();
            <#list columns as column>
                <#if exclusionShowColumns?contains(column.columnName)>
                <#else>
                    entity.set${column.attrNameUpperCase}(${column.attrNameLowerCase});
                </#if>
            </#list>
        return entity;
        }
${r'}'}