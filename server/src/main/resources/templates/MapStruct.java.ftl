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
   ${classNameUpperCase}Entity fromInputVO(${classNameUpperCase}InputVO vo);
        </#if>
   /**
   * Entity 转 DetailDTO
   *
   * @param entity Entity
   * @return DetailDTO
   */
   // ${classNameUpperCase}DetailDTO toDetailDTO(${classNameUpperCase}Entity entity);

   /**
   * Entity 转 ListDTO
   *
   * @param entity Entity
   * @return ListDTO
   */
   // ${classNameUpperCase}ListDTO toListDTO(${classNameUpperCase}Entity entity);
</#if>
${r'}'}