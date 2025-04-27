package ${codePackage}.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
<#if apiVersion == 3>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
<#else>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
</#if>
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ${commonPackage}.common.base.BaseController;
import ${commonPackage}.common.domain.Result;
import ${commonPackage}.common.domain.ResultEnum;
import ${codePackage}.service.${classNameUpperCase}Service;
import ${codePackage}.entity.${classNameUpperCase}Entity;

<#if mapstructEnabled == 1>import ${codePackage}.mapstruct.${classNameUpperCase}MapStruct;</#if>

<#if plusEnabled == 1>
import ${codePackage}.vo.${classNameUpperCase}InputVO;
import ${codePackage}.dto.${classNameUpperCase}BriefDTO;
import ${codePackage}.dto.${classNameUpperCase}DetailDTO;
import ${codePackage}.dto.${classNameUpperCase}ListDTO;
import ${codePackage}.query.${classNameUpperCase}ListQuery;
import ${codePackage}.query.${classNameUpperCase}PageQuery;
import ${commonPackage}.common.validator.group.Default;
import ${commonPackage}.common.validator.group.Create;
import ${commonPackage}.common.validator.group.Update;
import javax.validation.constraints.NotNull;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
</#if>
/**
 * ${comment}请求处理层
 *
 * @author ${author}
 * @date ${createTime?date("yyyy-MM-dd")}
 */
@Validated
@RestController
@RequestMapping("${mappingName}")
<#if apiVersion == 3>
@Tag(name = "${commentEscape}")
<#else>
@Api(value = "${commentEscape}", tags = "${commentEscape}")
</#if>
@RequiredArgsConstructor
public class ${classNameUpperCase}Controller extends BaseController {

    private final ${classNameUpperCase}Service ${classNameLowerCase}Service;
<#if mapstructEnabled == 1>    private final ${classNameUpperCase}MapStruct ${classNameLowerCase}MapStruct;</#if>
<#if plusEnabled == 1>
    <#if generalMethod??>
        <#if generalMethod.getDetailByIdEnabled==1>
            <#if apiVersion == 3>
    @Operation(summary = "根据唯一ID获取详细信息")
            <#else>
    @ApiOperation(value = "根据唯一ID获取详细信息", notes = "根据唯一ID获取详细信息")
            </#if>
    @GetMapping("/detail/{${pk.attrNameLowerCase}}")
    public Result${r'<'}${classNameUpperCase}DetailDTO> detail(@PathVariable("${pk.attrNameLowerCase}") @NotNull(message = "唯一ID不能为空") ${pk.attrType} ${pk.attrNameLowerCase}) {
        return Result.ok(${classNameLowerCase}Service.getDetailById(${pk.attrNameLowerCase}));
    }
        </#if>
        <#if generalMethod.getListEnabled==1>

            <#if apiVersion == 3>
    @Operation(summary = "${commentEscape}列表查询")
            <#else>
    @ApiOperation(value = "${commentEscape}列表查询", notes = "${commentEscape}列表查询")
            </#if>
    @GetMapping("/list")
    public Result${r'<List<'}${classNameUpperCase}ListDTO>> list(${classNameUpperCase}ListQuery query) {
        return Result.ok(${classNameLowerCase}Service.getList(query));
    }
        </#if>
        <#if generalMethod.getPageEnabled==1>

            <#if apiVersion == 3>
    @Operation(summary = "${commentEscape}分页查询")
            <#else>
    @ApiOperation(value = "${commentEscape}分页查询", notes = "${commentEscape}分页查询")
            </#if>
    @GetMapping("/page")
    public Result${r'<IPage<'}${classNameUpperCase}ListDTO>> page(${classNameUpperCase}PageQuery query) {
        return Result.ok(${classNameLowerCase}Service.getPage(query));
    }
        </#if>
        <#if generalMethod.addEnabled==1>

            <#if apiVersion == 3>
    @Operation(summary = "新建${commentEscape}，返回ID")
            <#else>
    @ApiOperation(value = "新建${commentEscape}", notes = "新建${commentEscape}，返回ID")
            </#if>
    @PostMapping("/add")
    public Result${r'<'}${pk.attrType}> add(@Validated({Create.class}) @RequestBody ${classNameUpperCase}InputVO vo) {
        <#if mapstructEnabled == 1>${classNameUpperCase}Entity entity = ${classNameLowerCase}MapStruct.fromInputVO(vo);<#else>${classNameUpperCase}Entity entity = vo.convertToEntity();</#if>
        entity.set${pk.attrNameUpperCase}(null);
        ${classNameLowerCase}Service.save(entity);
        return Result.ok(entity.get${pk.attrNameUpperCase}());
    }
        </#if>
        <#if generalMethod.updateEnabled==1>

            <#if apiVersion == 3>
    @Operation(summary = "更新${commentEscape}")
            <#else>
    @ApiOperation(value = "更新${commentEscape}", notes = "更新${commentEscape}")
            </#if>
    @PostMapping("/update")
    public Result${r'<'}String> update(@Validated({Update.class}) @RequestBody ${classNameUpperCase}InputVO vo) {
        ${classNameUpperCase}BriefDTO briefDTO = ${classNameLowerCase}Service.getBriefById(vo.get${pk.attrNameUpperCase}());
        if (null == briefDTO) {
            return Result.failed(ResultEnum.NOT_FOUND, "没有找到需要更新的记录");
        }
        <#if mapstructEnabled == 1>${classNameLowerCase}Service.updateById(${classNameLowerCase}MapStruct.fromInputVO(vo));<#else>${classNameLowerCase}Service.updateById(vo.convertToEntity());</#if>
        return Result.ok();
    }
        </#if>
    </#if>

            <#if apiVersion == 3>
    @Operation(summary = "根据唯一ID删除${commentEscape}")
    @Parameters({
        @Parameter(name = "${pk.attrNameLowerCase}",description = "${pk.comment}",in = ParameterIn.PATH)
    })
            <#else>
    @ApiOperation(value = "根据唯一ID删除${commentEscape}", notes = "根据唯一ID删除${commentEscape}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "${pk.attrNameLowerCase}", value = "${pk.comment}", paramType = "path"),
    })
            </#if>
    @PostMapping("/delete/{${pk.attrNameLowerCase}}")
    public Result${r'<'}Boolean> delete(@PathVariable("${pk.attrNameLowerCase}") @NotNull(message = "${pk.comment}不能为空") ${pk.attrType} ${pk.attrNameLowerCase}) {
        UserOnlineDTO user = getOnlineUser();
        ${classNameUpperCase}BriefDTO briefDTO = ${classNameLowerCase}Service.getBriefById(${pk.attrNameLowerCase});
        if (null == briefDTO) {
            return Result.failed(ResultEnum.NOT_FOUND, "没有找到需要删除的记录");
        }
        //TODO 其他限制删除条件
    <#if logicDelete == 1>
        return Result.ok(${classNameLowerCase}Service.delete(${pk.attrNameLowerCase}, user.getUserId()));
    <#else>
        return Result.ok(${classNameLowerCase}Service.removeById(${pk.attrNameLowerCase}));
    </#if>
    }
</#if>
}
