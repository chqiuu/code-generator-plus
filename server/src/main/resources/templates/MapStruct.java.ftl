package ${codePackage}.mapstruct;
import ${codePackage}.entity.${classNameUpperCase}Entity;
import ${codePackage}.vo.${classNameUpperCase}InputVO;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
import org.mapstruct.Mapper;
/**
 * ${comment}对象转换器
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Mapper(componentModel = "spring")
public interface ${classNameUpperCase}MapStruct {
<#if generalMethod??>
        <#if generalMethod.addEnabled==1 || generalMethod.updateEnabled==1>
   /**
   * VO 转 Entity
   *
   * @param vo InputVO
   * @return Entity
   */
   ${classNameUpperCase}Entity toEntity(${classNameUpperCase}InputVO vo);
        </#if>
</#if>
${r'}'}