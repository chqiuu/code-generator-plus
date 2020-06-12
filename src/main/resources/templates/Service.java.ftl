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
</#if>
/**
 * ${comment}
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
public interface ${classNameUpperCase}Service <#if plusEnabled == 1> extends IService${r'<'}${classNameUpperCase}Entity></#if> ${r'{'}

<#if plusEnabled == 0>
    <#if pk.extra == 'auto_increment'>
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

 /**
 * 根据唯一ID获取详细信息
 *
 * @param ${pk.attrNameLowerCase} ${pk.comment}
 * @return 详细信息
 */
 ${classNameUpperCase}DetailDTO getDetailById(${pk.attrType} ${pk.attrNameLowerCase});

 /**
 * ${comment}分页查询
 * @param current       当前页
 * @param size          每页显示条数
 <#list columns as column>
 <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
  * @param ${column.attrNameLowerCase} ${column.comment}
 </#if>
</#list>
 * @return ${comment}列表
 */
 IPage${r'<'}${classNameUpperCase}ListDTO> getPage(Integer current, Integer size, <#assign paramsStr = ''><#list columns as column><#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')><#assign paramsStr>${paramsStr}${column.attrType} ${column.attrNameLowerCase},</#assign></#if></#list>${paramsStr?trim?substring(0,paramsStr?trim?length-1)});

  <#if logicDelete == 1>
 /**
 * 删除
 *
 * @param ${pk.attrNameLowerCase} ${pk.comment}
 * @param userId    当前操作人员ID
 * @return 是否成功
 */
 boolean delete(${pk.attrType} ${pk.attrNameLowerCase}, Long userId);
  </#if>

</#if>
${r'}'}