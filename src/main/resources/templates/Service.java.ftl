package ${codePackage}.service;

import ${codePackage}.entity.${classNameUpperCase}Entity;

import java.util.List;
import java.util.Map;

<#if plusEnabled == 1>
import com.baomidou.mybatisplus.extension.service.IService;
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
</#if>
${r'}'}