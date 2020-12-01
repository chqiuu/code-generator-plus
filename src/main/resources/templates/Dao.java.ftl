<#if plusEnabled == 1>
package ${codePackage}.mapper;
import ${codePackage}.entity.${classNameUpperCase}Entity;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
import ${codePackage}.query.${classNameUpperCase}ListQuery;
import ${codePackage}.query.${classNameUpperCase}PageQuery;
import org.apache.ibatis.annotations.Param;
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
    * ${comment}列表查询
    * @param query       查询对象
    * @return ${comment}列表
    */
    List${r'<'}${classNameUpperCase}ListDTO> getList(@Param("query") ${classNameUpperCase}ListQuery query);

    /**
     * ${comment}分页查询
     * @param pageInfo      分页控件
     * @param query       分页查询对象
     * @return ${comment}列表
     */
    IPage${r'<'}${classNameUpperCase}ListDTO> getPage(@Param("pg") Page${r'<'}${classNameUpperCase}ListDTO> pageInfo, @Param("query") ${classNameUpperCase}PageQuery query);
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
