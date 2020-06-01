package ${codePackage}.mapper;

import ${codePackage}.entity.${classNameUpperCase}Entity;
import org.springframework.stereotype.Repository;

<#if plusEnabled == 1>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * ${comment}
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Repository
public interface ${classNameUpperCase}Mapper extends BaseMapper<${classNameUpperCase}Entity> {

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
