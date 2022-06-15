package ${codePackage}.service;

import ${codePackage}.entity.${classNameUpperCase}Entity;

import java.util.List;
import java.util.Map;

<#if plusEnabled == 1>
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.time.LocalDateTime;
import java.time.LocalDate;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
import ${codePackage}.query.${classNameUpperCase}ListQuery;
import ${codePackage}.query.${classNameUpperCase}PageQuery;
</#if>
/**
 * ${commentEscape}业务逻辑层接口
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
public interface ${classNameUpperCase}Service <#if plusEnabled == 1>extends IService${r'<'}${classNameUpperCase}Entity></#if> ${r'{'}
<#if plusEnabled == 0>
    <#if pk.extra?? && pk.extra == 'auto_increment'>

    /**
     * 插入数据
     *
     * @param entity 实体类
     * @return 主键值
     */
    long insert(${classNameUpperCase}Entity entity);
    <#else>

    /**
     * 插入数据
     *
     * @param entity 实体类
     */
    void insert(${classNameUpperCase}Entity entity);
   </#if>

    /**
     * 根据主键更新数据 ${pk.attrNameLowerCase}
     *
     * @param entity 新数据
     */
    Integer updateByPrimary(${classNameUpperCase}Entity entity);

    /**
     * 根据主键获取数据 ${pk.attrNameLowerCase}
     *
     * @param entity 查询条件
     * @return 查询结果
     */
    ${classNameUpperCase}Entity getByPrimary(${classNameUpperCase}Entity entity);
    <#if mapQueryEnabled == 1>

    /**
     * 根据查询条件获取一条记录
     *
     * @param conditions 查询条件
     * @return 查询结果
     */
    ${classNameUpperCase}Entity getOne(Map${r'<'}String, Object> conditions);

    /**
     * 根据查询条件查询
     *
     * @param conditions 查询条件
     * @return 结果集
     */
    List${r'<'}${classNameUpperCase}Entity> queryList(Map${r'<'}String, Object> conditions);
    </#if>

    /**
     * 查询所有数据
     *
     * @return 结果集
     */
    List<${classNameUpperCase}Entity> getAll();
<#elseif plusEnabled == 1>
 <#if generalMethod??>
  <#if generalMethod.getDetailByIdEnabled==1>

    /**
     * 根据唯一ID获取详细信息
     *
     * @param ${pk.attrNameLowerCase} ${pk.comment}
     * @return 详细信息
     */
    ${classNameUpperCase}DetailDTO getDetailById(${pk.attrType} ${pk.attrNameLowerCase});
</#if>
        <#if generalMethod.getListEnabled==1>

    /**
    * ${comment}列表查询
    * @param query       查询对象
    * @return ${comment}列表
    */
    List${r'<'}${classNameUpperCase}ListDTO> getList(${classNameUpperCase}ListQuery query);
 </#if>
        <#if generalMethod.getPageEnabled==1>

    /**
     * ${comment}分页查询
     * @param query       分页查询对象
     * @return ${comment}列表（带分页）
     */
    IPage${r'<'}${classNameUpperCase}ListDTO> getPage(${classNameUpperCase}PageQuery query);
        </#if>
 </#if>
  <#if logicDelete == 1>

    /**
     * 删除
     *
     * @param ${pk.attrNameLowerCase} ${pk.comment}
     * @param operatorId    当前操作人员ID
     * @return 是否成功
     */
    boolean delete(${pk.attrType} ${pk.attrNameLowerCase}, Long operatorId);
  </#if>
</#if>
${r'}'}