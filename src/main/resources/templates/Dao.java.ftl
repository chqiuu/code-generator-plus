package ${codePackage}.mapper;

import ${codePackage}.entity.${classNameUpperCase}Entity;
import org.springframework.stereotype.Repository;

<#if plusEnabled == 1>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
/**
 * ${comment}
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Repository
public interface ${classNameUpperCase}Mapper extends BaseMapper<${classNameUpperCase}Entity> {

  /**
  * 根据唯一ID获取详细信息
  *
  * @param ${pk.attrNameLowerCase} ${column.comment}
  * @return 详细信息
  */
  ${classNameUpperCase}DetailDTO getDetailById(@Param("${pk.attrNameLowerCase}") ${pk.attrType} ${pk.attrNameLowerCase});

  /**
  * 分页查询
  * @param pageInfo      分页控件
  <#list columns as column>
   <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    * @param ${column.attrNameLowerCase} ${column.comment}
   </#if>
  </#list>
  * @return 列表
  */
  IPage${r'<'}${classNameUpperCase}ListDto> getPage(@Param("pg") Page${r'<'}${classNameUpperCase}ListDto> pageInfo, <#assign paramsStr = ''>
  <#list columns as column>
   <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    <#assign paramsStr>${paramsStr}@Param("${column.attrNameLowerCase}") ${column.attrType} ${column.attrNameLowerCase},</#assign>
   </#if>
  </#list>${paramsStr?trim?substring(0,paramsStr?trim?length-1)});
}
<#else>
import org.springframework.stereotype.Repository;
/**
 * ${comment}
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Repository
public interface ${classNameUpperCase}Dao extends BaseDao<${classNameUpperCase}Entity> {

}
</#if>
