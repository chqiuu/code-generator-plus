<#if plusEnabled == 1>
package ${codePackage}.mapper;
import ${codePackage}.entity.${classNameUpperCase}Entity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${codePackage}.dto.${classNameUpperCase}BriefDTO;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
import ${codePackage}.query.${classNameUpperCase}ListQuery;
import ${codePackage}.query.${classNameUpperCase}PageQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
/**
 * ${comment}数据持久层
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Mapper
public interface ${classNameUpperCase}Mapper extends BaseMapper<${classNameUpperCase}Entity> {
    <#if generalMethod??>
        <#if generalMethod.insertIgnoreEnabled==1>

    /**
    * 插入数据，如果中已经存在相同的记录，则忽略当前新数据
    *
    * @param entity 实体类对象
    * @return 影响条数
    */
    @Insert("${r'<'}script>INSERT IGNORE INTO `${tableName}` <trim prefix='(' suffix=')' suffixOverrides=','><#list columns as column><if test='${column.attrNameLowerCase} != null'>`${column.columnName}`, </if></#list></trim><trim prefix='values (' suffix=')' suffixOverrides=','><#list columns as column><if test='${column.attrNameLowerCase} != null'>${r'#{'}${column.attrNameLowerCase}<#if column.attrType == 'JSONObject'>, typeHandler="com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler"</#if>${r'}'}, </if></#list></trim>${r'<'}/script>")
    int insertIgnore(${classNameUpperCase}Entity entity);
        </#if>
        <#if generalMethod.replaceEnabled==1>

    /**
    * 替换数据，如果中已经存在相同的记录，则覆盖旧数据
    *
    * @param entity 实体类对象
    * @return 影响条数
    */
    @Insert("${r'<'}script>REPLACE INTO `${tableName}` <trim prefix='(' suffix=')' suffixOverrides=','><#list columns as column><if test='${column.attrNameLowerCase} != null'>`${column.columnName}`, </if></#list></trim><trim prefix='values (' suffix=')' suffixOverrides=','><#list columns as column><if test='${column.attrNameLowerCase} != null'>${r'#{'}${column.attrNameLowerCase}<#if column.attrType == 'JSONObject'>, typeHandler="com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler"</#if>${r'}'}, </if></#list></trim>${r'<'}/script>")
    int replace(${classNameUpperCase}Entity entity);
        </#if>

    /**
    * 根据唯一ID获取简要信息
    *
    * @param ${pk.attrNameLowerCase} ${pk.comment}
    * @return 简要信息
    */
    ${classNameUpperCase}BriefDTO getBriefById(@Param("${pk.attrNameLowerCase}") ${pk.attrType} ${pk.attrNameLowerCase});
        <#if generalMethod.getDetailByIdEnabled==1>

    /**
    * 根据唯一ID获取详细信息
    *
    * @param ${pk.attrNameLowerCase} ${pk.comment}
    * @return 详细信息
    */
    ${classNameUpperCase}DetailDTO getDetailById(@Param("${pk.attrNameLowerCase}") ${pk.attrType} ${pk.attrNameLowerCase});
        </#if>
        <#if generalMethod.getListEnabled==1>

    /**
    * ${comment}列表查询
    * @param query       查询对象
    * @return ${comment}列表
    */
    List${r'<'}${classNameUpperCase}ListDTO> getList(@Param("query") ${classNameUpperCase}ListQuery query);
        </#if>
        <#if generalMethod.getPageEnabled==1>

    /**
    * ${comment}分页查询
    * @param pageInfo      分页控件
    * @param query       分页查询对象
    * @return ${comment}列表
    */
    IPage${r'<'}${classNameUpperCase}ListDTO> getPage(@Param("pg") Page${r'<'}${classNameUpperCase}ListDTO> pageInfo, @Param("query") ${classNameUpperCase}PageQuery query);
        </#if>
    </#if>
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
