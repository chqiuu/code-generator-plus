<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper <#if plusEnabled == 1>namespace="${codePackage}.mapper.${classNameUpperCase}Mapper" <#else> namespace="${codePackage}.dao.${classNameUpperCase}Dao" </#if>>
    <!-- ${comment} 表名：`${tableName}` -->
    <sql id="Base_Column_List">
        <#list columns as column>`${column.columnName}`<#if column?has_next>,</#if></#list>
    </sql>
    <sql id="Base_${acronymUpperCase}_Column_List">
        <#list columns as column>${acronymLowerCase}.`${column.columnName}`<#if column?has_next>,</#if></#list>
    </sql>
<#if plusEnabled == 1>
    <#else>
        <!-- 可根据自己的需求，是否要使用 -->
        <resultMap id="BaseResultMap" type="${codePackage}.entity.${classNameUpperCase}Entity">
            <#list columns as column>
                <result property="${column.attrNameLowerCase}" column="${column.columnName}"/>
            </#list>
        </resultMap>

        <!--插入数据-->
        <insert id="insert" parameterType="${codePackage}.entity.${classNameUpperCase}Entity"<#if pk.extra == 'auto_increment'> useGeneratedKeys="true" keyProperty="${pk.attrNameLowerCase}"</#if>>
        INSERT INTO `${tableName}`
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list columns as column>
                <if test="${column.attrNameLowerCase} != null">`${column.columnName}`,</if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#list columns as column>
                <if test="${column.attrNameLowerCase} != null">${r'#{'}${column.attrNameLowerCase}${r'}'},</if>
            </#list>
        </trim>
        </insert>

        <!--根据主键更新数据-->
        <update id="updateByPrimary" parameterType="${codePackage}.entity.${classNameUpperCase}Entity">
            UPDATE `${tableName}`
            <set>
                <#list columns as column>
                    <if test="${column.attrNameLowerCase} != null and ${column.attrNameLowerCase} != ''">`$column.columnName` = ${r'#{'}${column.attrNameLowerCase}${r'}'}<#if column?has_next>,</#if></if>
                </#list>
            </set>
            WHERE `${pk.columnName}` = ${r'#{'}${pk.attrNameLowerCase}${r'}'}
        </update>

        <!--根据主键获取数据-->
        <select id="getByPrimary" parameterType="${codePackage}.entity.${classNameUpperCase}Entity"
                resultType="${codePackage}.entity.${classNameUpperCase}Entity">
            SELECT
            <include refid="Base_Column_List"/>
            FROM `${tableName}` AS ${acronymLowerCase} where ${acronymLowerCase}.`${pk.columnName}` =  ${r'#{'}${pk.attrNameLowerCase}${r'}'}
        </select>

    <#if mapQueryEnabled == 1>
        <!--根据查询条件获取一条记录-->
        <select id="getOne" parameterType="${codePackage}.entity.${classNameUpperCase}Entity" resultType="${codePackage}.entity.${classNameUpperCase}Entity">
            SELECT
            <include refid="Base_Column_List"/>
            FROM `${tableName}` AS ${acronymLowerCase} where 1=1
            <#list columns as column>
                <if test="${column.attrNameLowerCase} != null and ${column.attrNameLowerCase} != ''">AND ${acronymLowerCase}.`${column.columnName}` = ${r'#{'}${column.attrNameLowerCase}${r'}'}</if>
            </#list>
            LIMIT 1;
        </select>

        <!--根据查询条件查询-->
        <select id="queryList" parameterType="${codePackage}.entity.${classNameUpperCase}Entity" resultType="${codePackage}.entity.${classNameUpperCase}Entity">
            SELECT
            <include refid="Base_Column_List"/>
            FROM `${tableName}` AS ${acronymLowerCase} where 1=1
            <#list columns as column>
                <if test="${column.attrNameLowerCase} != null and ${column.attrNameLowerCase} != ''">AND ${acronymLowerCase}.`${column.columnName}` = ${r'#{'}${column.attrNameLowerCase}${r'}'}</if>
            </#list>
        </select>
    </#if>

        <!--查询所有数据-->
        <select id="getAll" resultType="${codePackage}.entity.${classNameUpperCase}Entity">
            SELECT
            <include refid="Base_Column_List"/>
            FROM `${tableName}` AS ${acronymLowerCase}
        </select>
    </#if>
</mapper>