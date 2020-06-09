package ${codePackage}.service.impl;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
<#if plusEnabled == 1>
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${codePackage}.mapper.${classNameUpperCase}Mapper;
    import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
    import ${codePackage}.dto.${classNameUpperCase}ListDTO;
    import com.baomidou.mybatisplus.core.metadata.IPage;
    import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
    import java.time.LocalDateTime;
    import java.time.LocalDate;
<#else>
import org.springframework.beans.factory.annotation.Autowired;
import ${codePackage}.dao.${classNameUpperCase}Dao;
</#if>
import ${codePackage}.entity.${classNameUpperCase}Entity;
import ${codePackage}.service.${classNameUpperCase}Service;

/**
 * ${comment}业务层
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Service
@AllArgsConstructor
public class ${classNameUpperCase}ServiceImpl <#if plusEnabled == 1> extends ServiceImpl${r'<'}${classNameUpperCase}Mapper, ${classNameUpperCase}Entity> </#if> implements ${classNameUpperCase}Service ${r'{'}

<#if plusEnabled == 0>
    private final ${classNameUpperCase}Dao ${classNameLowerCase}Dao;

    @Autowired
    public ${classNameUpperCase}ServiceImpl(${classNameUpperCase}Dao ${classNameLowerCase}Dao) {
        this.${classNameLowerCase}Dao = ${classNameLowerCase}Dao;
    }
    <#if pk.extra == 'auto_increment'>
    /**
     * 插入数据
     *
     * @param entity 实体类
     * @return 主键值
     */
    @Override
    public long insert(${classNameUpperCase}Entity entity) {
        ${classNameLowerCase}Dao.insert(entity);
        return entity.get${pk.attrNameUpperCase}();
    }
    <#else>
     /**
     * 插入数据
     *
     * @param entity 实体类
     */
        @Override
        public void insert(${classNameUpperCase}Entity entity) {
                ${classNameLowerCase}Dao.insert(entity);
        }
    </#if>

    /**
     * 根据主键更新数据
     *
     * @param entity 新数据
     */
    @Override
    public Integer updateByPrimary(${classNameUpperCase}Entity entity) {
        return ${classNameLowerCase}Dao.updateByPrimary(entity);
    }

    /**
     * 根据主键获取数据
     *
     * @param entity 查询条件
     * @return 查询结果
     */
    @Override
    public ${classNameUpperCase}Entity getByPrimary(${classNameUpperCase}Entity entity) {
        return ${classNameLowerCase}Dao.getByPrimary(entity);
    }
    <#if mapQueryEnabled == 1>
    /**
     * 根据查询条件获取一条记录
     *
     * @param conditions 查询条件
     * @return 查询结果
     */
    @Override
    public ${classNameUpperCase}Entity getOne(Map${r'<'}String, Object> conditions) {
        return ${classNameLowerCase}Dao.getOne(conditions);
    }

    /**
     * 根据查询条件查询
     *
     * @param conditions 查询条件
     * @return 结果集
     */
    @Override
    public List${r'<'}${classNameUpperCase}Entity> queryList(Map${r'<'}String, Object> conditions) {
        return ${classNameLowerCase}Dao.queryList(conditions);
    }
    </#if>

    /**
     * 查询所有数据
     *
     * @return 结果集
     */
    @Override
    public List${r'<'}${classNameUpperCase}Entity> getAll() {
        return ${classNameLowerCase}Dao.getAll();
    }

<#elseif plusEnabled == 1>

    @Override
    public ${classNameUpperCase}DetailDTO getDetailById(${pk.attrType} ${pk.attrNameLowerCase}) {
        return this.baseMapper.getDetailById(${pk.attrNameLowerCase});
    }

    @Override
    public IPage${r'<'}${classNameUpperCase}ListDTO> getPage(Integer current, Integer size, <#assign paramsStr = ''>
    <#list columns as column>
        <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')><#assign paramsStr>${paramsStr}${column.attrType} ${column.attrNameLowerCase}, </#assign></#if>
    </#list>${paramsStr?trim?substring(0,paramsStr?trim?length-1)}){
        Page${r'<'}${classNameUpperCase}ListDTO> pageInfo = new Page<>(current, size);
        return this.baseMapper.getPage(pageInfo,
    <#assign paramsStr = ''>
    <#list columns as column>
        <#if column.columnName != pk.columnName && !exclusionShowColumns?contains(column.columnName) && !column.dataType?contains('text')>
            <#assign paramsStr>${paramsStr}${column.attrNameLowerCase}, </#assign>
        </#if>
    </#list>${paramsStr?trim?substring(0,paramsStr?trim?length-1)});
    }

    <#if logicDelete == 1>
    @Override
    public boolean delete(${pk.attrType} ${pk.attrNameLowerCase}, Long userId){
        ${classNameUpperCase}Entity updateEntity = new ${classNameUpperCase}Entity();
        updateEntity.set${pk.attrNameUpperCase}(${pk.attrNameLowerCase});
        updateEntity.setIsDeleted(true);
    <#list columns as column>
        <#if column.columnName == 'del_time'>
            updateEntity.setDelTime(LocalDateTime.now());
        <#elseif column.columnName == 'del_user'>
            updateEntity.setDelUser(userId);
        </#if>
    </#list>
        return baseMapper.updateById(updateEntity) > 0;
    }
    </#if>
</#if>
${r'}'}