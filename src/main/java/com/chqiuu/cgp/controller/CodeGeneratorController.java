package com.chqiuu.cgp.controller;

import cn.hutool.core.util.StrUtil;
import com.chqiuu.cgp.common.base.BaseController;
import com.chqiuu.cgp.common.domain.Result;
import com.chqiuu.cgp.common.domain.ResultEnum;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.BaseDatabase;
import com.chqiuu.cgp.db.DatabaseFactory;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.chqiuu.cgp.dto.CodePreviewDTO;
import com.chqiuu.cgp.exception.UserException;
import com.chqiuu.cgp.service.CodeGeneratorService;
import com.chqiuu.cgp.vo.GeneratorVO;
import com.chqiuu.cgp.vo.SqlVO;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 代码生成器控制类
 *
 * @author chqiu
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/generator")
@Api(value = "/generator", tags = "代码生成器控制类")
public class CodeGeneratorController extends BaseController {

    private final CodeGeneratorService codeGeneratorService;

    /**
     * 连接数据库
     *
     * @param dbType   数据库类型
     * @param server   服务器地址
     * @param port     端口号
     * @param database 数据库名
     * @param username 登录名
     * @param password 密码
     * @return 连接是否成功
     */
    @ApiOperation(value = "第一步：连接数据库", notes = "连接数据库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dbType", value = "数据库类型", paramType = "query", required = true),
            @ApiImplicitParam(name = "server", value = "服务器地址", paramType = "query", required = true),
            @ApiImplicitParam(name = "port", value = "端口号", paramType = "query", required = true),
            @ApiImplicitParam(name = "database", value = "数据库名", paramType = "query", required = true),
            @ApiImplicitParam(name = "username", value = "登录名", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true),
    })
    @GetMapping("/connectDatabase")
    public Result<String> connectDatabase(@RequestParam(value = "dbType", defaultValue = "mysql") String dbType,
                                          String server, Integer port, String database, String username, String password) {
        DriverClassEnum driverClassEnum = DriverClassEnum.getByDbType(dbType);
        if (null == driverClassEnum) {
            return Result.failed(ResultEnum.PARAM_EMPTY_ERROR, "数据库类型无效！");
        }
        BaseConnect connect;
        try {
            connect = codeGeneratorService.connectDatabase(driverClassEnum, server, port, database, username, password);
        } catch (Exception e) {
            return Result.failed(ResultEnum.PARAM_EMPTY_ERROR, e.getMessage());
        }
        //连接数据库
        if (null != connect && null != connect.getDataSource()) {
            HttpSession httpSession = getSession();
            httpSession.setAttribute("dbConnect", connect);
            httpSession.setAttribute("allTables", codeGeneratorService.queryTableList(connect, null));
            httpSession.setAttribute("dbType", dbType);
            return Result.ok("数据库连接成功");
        } else {
            return Result.failed(ResultEnum.PARAM_EMPTY_ERROR, "数据库连接失败");
        }
    }

    @ApiOperation(value = "导入数据库脚本", notes = "导入数据库脚本")
    @ApiImplicitParams({@ApiImplicitParam(name = "sql", value = "数据库脚本", paramType = "query", required = true),
    })
    @PostMapping("/importSql")
    public Result<String> importSql(@RequestBody SqlVO vo) {
        DriverClassEnum driverClassEnum = DriverClassEnum.getByDbType(vo.getDbType());
        if (null == driverClassEnum) {
            return Result.failed(ResultEnum.PARAM_EMPTY_ERROR, "数据库类型有误");
        }
        BaseDatabase database = new DatabaseFactory().create(driverClassEnum);
        List<TableEntity> tableList = database.getTableList(vo.getDdl());
        if (tableList.isEmpty()) {
            return Result.failed(ResultEnum.PARAM_EMPTY_ERROR, "从脚本中没有获取到数据库表");
        }
        HttpSession httpSession = getSession();
        // 将数据库表结构保存到Session中
        httpSession.setAttribute("allTables", tableList);
        httpSession.setAttribute("dbType", vo.getDbType());
        return Result.ok();
    }

    @ApiOperation(value = "获取数据库中的所有表", notes = "获取数据库中的所有表")
    @GetMapping("/getAllTables")
    public Result<List<TableEntity>> getAllTables() {
        List<TableEntity> list = (List<TableEntity>) getSession().getAttribute("allTables");
        if (null == list) {
            return Result.failed(ResultEnum.PARAM_EMPTY_ERROR, "请先连接数据库！");
        }
        //查询列表数据
        return Result.ok(list);
    }

    @ApiOperation(value = "多表批量生成代码", notes = "多表批量生成代码")
    @PostMapping("/generatorCodes")
    public void generatorCodes(@RequestBody GeneratorVO vo) throws IOException {
        List<TableEntity> list = (List<TableEntity>) getSession().getAttribute("allTables");
        if (null == list) {
            throw new UserException(ResultEnum.FAILED, "未找到对应都表！");
        }
        DriverClassEnum driverClassEnum = DriverClassEnum.getByDbType((String) getSession().getAttribute("dbType"));
        if (null == driverClassEnum) {
            throw new UserException(ResultEnum.FAILED, "数据库类型有误！");
        }
        byte[] data = codeGeneratorService.generatorCodes(driverClassEnum, vo.getRootPackage(), vo.getAuthor(), vo.getIsPlus(), vo.getTables(), list);
        HttpServletResponse response = getResponse();
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s-%s.zip", vo.getRootPackage(), new Date().toLocaleString()));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-typeeam; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }


    /**
     * 数据库表列表模糊查询
     */
    @ApiOperation(value = "数据库表列表模糊查询", notes = "数据库表列表模糊查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName", value = "表名", paramType = "query", required = false),
    })
    @GetMapping("/queryTableList")
    public Result<List<TableEntity>> queryList(String tableName) {
        //查询列表数据
        return Result.ok(codeGeneratorService.queryTableList(getConnect(), tableName));
    }

    /**
     * 生成代码
     */
    @ApiOperation(value = "单表生成代码", notes = "单表生成代码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "table", value = "表名", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/code")
    public void code(String codePackage, String rootPackage, String moduleName, String author, String table,
                     boolean isPlus) {
        if (StrUtil.isNotBlank(codePackage)) {
            moduleName = codePackage.substring(codePackage.lastIndexOf(".") + 1);
            rootPackage = codePackage.substring(0, codePackage.lastIndexOf("."));
        }
        String[] tableNames = new String[1];
        tableNames[0] = table;
        byte[] data = new byte[0];
        try {
            data = codeGeneratorService.generatorCode(getConnect(), rootPackage, moduleName, author, tableNames, isPlus);
            HttpServletResponse response = getResponse();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=code-" + table + ".zip");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-typeeam; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "预览生成的代码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "table", value = "表名", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/preview")
    public Result<List<CodePreviewDTO>> preview(String codePackage, String rootPackage, String moduleName, String author, String table, boolean isPlus) {
        if (StrUtil.isNotBlank(codePackage)) {
            moduleName = codePackage.substring(codePackage.lastIndexOf(".") + 1);
            rootPackage = codePackage.substring(0, codePackage.lastIndexOf("."));
        }
        return Result.ok(codeGeneratorService.preview(getConnect(), rootPackage, moduleName, author, table, isPlus));
    }

    @ApiOperation(value = "多表批量生成代码", notes = "多表批量生成代码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "tables", value = "表名数组", defaultValue = "a,b,c", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/codes")
    public void codes(String codePackage, String rootPackage, String moduleName, String author, String tables,
                      boolean isPlus) throws IOException {
        if (StrUtil.isNotBlank(codePackage)) {
            moduleName = codePackage.substring(codePackage.lastIndexOf(".") + 1);
            rootPackage = codePackage.substring(0, codePackage.lastIndexOf("."));
        }
        String[] tableNames = tables.replaceAll(" ", "").replaceAll("   ", "").split(",");
        byte[] data = codeGeneratorService.generatorCode(getConnect(), rootPackage, moduleName, author, tableNames, isPlus);
        HttpServletResponse response = getResponse();
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s-%s.zip", rootPackage, new Date().toLocaleString()));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-typeeam; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }


    /**
     * 从Session中获取数据库连接
     *
     * @return 数据库连接
     */
    private BaseConnect getConnect() {
        BaseConnect connect = (BaseConnect) getSession().getAttribute("dbConnect");
        if (null == connect) {
            throw new UserException(ResultEnum.FAILED, "还未连接数据库，请先连接数据库！");
        }
        return connect;
    }

    /**
     * 生成代码，全部生成
     */
    @ApiOperation(value = "批量生成所有表代码", notes = "批量生成所有表代码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/codeAll")
    public void codeAll(String codePackage, String rootPackage, String moduleName, String author,
                        boolean isPlus) throws Exception {
        if (StrUtil.isNotBlank(codePackage)) {
            moduleName = codePackage.substring(codePackage.lastIndexOf(".") + 1);
            rootPackage = codePackage.substring(0, codePackage.lastIndexOf("."));
        }
        byte[] data = codeGeneratorService.generatorCodeAll(getConnect(), rootPackage, moduleName, author, isPlus);
        HttpServletResponse response = getResponse();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"code-" + new Date().toLocaleString() + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-typeeam; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @PostMapping(value = "upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public void upload(@ApiParam(value = "上传文件", required = true) MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        System.out.println(fileName);
    }
}
