<#if plusEnabled == 1>
package ${codePackage}.mapper;
import ${codePackage}.entity.${classNameUpperCase}Entity;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.time.LocalDate;
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
  * @param ${pk.attrNameLowerCase} ${pk.comment}
  * @return 详细信息
  */
  ${classNameUpperCase}DetailDTO getDetailById(@Param("${pk.attrNameLowerCase}") ${pk.attrType} ${pk.attrNameLowerCase});

  /**
  * ${comment}分页查询
  * @param pageInfo      分页控件
  <#list columns as column>
   <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    * @param ${column.attrNameLowerCase} ${column.comment}
   </#if>
  </#list>
  * @return ${comment}列表
  */
  IPage${r'<'}${classNameUpperCase}ListDTO> getPage(@Param("pg") Page${r'<'}${classNameUpperCase}ListDTO> pageInfo, <#assign paramsStr = ''>
  <#list columns as column>
   <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
    <#assign paramsStr>${paramsStr}@Param("${column.attrNameLowerCase}") ${column.attrType} ${column.attrNameLowerCase},</#assign>
   </#if>
  </#list>${paramsStr?trim?substring(0,paramsStr?trim?length-1)});
}
<#else>
    package ${codePackage}.dao;
    import ${codePackage}.entity.${classNameUpperCase}Entity;
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
