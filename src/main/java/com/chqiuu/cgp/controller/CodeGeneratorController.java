package com.chqiuu.cgp.controller;

import com.chqiuu.cgp.common.constant.R;
import com.chqiuu.cgp.common.constant.ResultConstant;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.chqiuu.cgp.exception.UserException;
import com.chqiuu.cgp.service.CodeGeneratorService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/generator")
@Api(value = "/generator", tags = "代码生成器控制类")
public class CodeGeneratorController {

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
    @PostMapping("/connectDatabase")
    public R<String> connectDatabase(HttpSession httpSession, @RequestParam(value = "dbType", defaultValue = "mysql") String dbType,
                                     String server, Integer port, String database, String username, String password) {
        DriverClassEnum driverClassEnum = DriverClassEnum.getByDbType(dbType);
        if (null == driverClassEnum) {
            return R.failed(ResultConstant.PARAM_EMPTY_ERROR, "数据库类型无效！");
        }
        BaseConnect connect = null;
        try {
            connect = codeGeneratorService.connectDatabase(driverClassEnum, server, port, database, username, password);
        } catch (Exception e) {
            return R.failed(ResultConstant.PARAM_EMPTY_ERROR, e.getMessage());
        }
        //连接数据库
        if (null != connect && null != connect.getDataSource()) {
            httpSession.setAttribute("dbConnect", connect);
            return R.ok();
        } else {
            return R.failed(ResultConstant.PARAM_EMPTY_ERROR, "数据库连接失败！");
        }
    }

    /**
     * 数据库表列表模糊查询
     */
    @ApiOperation(value = "数据库表列表模糊查询", notes = "数据库表列表模糊查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName", value = "表名", paramType = "query", required = false),
    })
    @PostMapping("/queryTableList")
    public R<List<TableEntity>> queryList(HttpSession httpSession, String tableName) {
        BaseConnect connect = (BaseConnect) httpSession.getAttribute("dbConnect");
        if (null == connect) {
            return R.failed(ResultConstant.PARAM_EMPTY_ERROR, "请先连接数据库！");

        }
        //查询列表数据
        return R.ok(codeGeneratorService.queryTableList(connect, tableName));
    }

    /**
     * 生成代码
     */
    @ApiOperation(value = "生成代码", notes = "生成代码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "table", value = "表名", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/code")
    public void code(HttpSession httpSession, String rootPackage, String moduleName, String author, String table, boolean isPlus, HttpServletResponse response) {
        BaseConnect connect = (BaseConnect) httpSession.getAttribute("dbConnect");
        if (null == connect) {
            throw new UserException(ResultConstant.FAILED, "还未连接数据库，请先连接数据库！");
        }
        if (null == moduleName) {
            moduleName = rootPackage.substring(rootPackage.lastIndexOf(".") + 1);
            rootPackage = rootPackage.substring(0, rootPackage.lastIndexOf("."));
        }
        String[] tableNames = new String[1];
        tableNames[0] = table;
        byte[] data = new byte[0];
        try {
            data = codeGeneratorService.generatorCode(connect, rootPackage, moduleName, author, tableNames, isPlus);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=code-" + table + ".zip");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-typeeam; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成代码
     */
    @ApiOperation(value = "生成代码，批量", notes = "生成代码，批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "tables", value = "表名数组", defaultValue = "a,b,c", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/codes")
    public void codes(HttpSession httpSession, String rootPackage, String moduleName, String author, String tables, boolean isPlus, HttpServletResponse response) throws IOException {
        BaseConnect connect = (BaseConnect) httpSession.getAttribute("dbConnect");
        if (null == connect) {
            throw new UserException(ResultConstant.FAILED, "还未连接数据库，请先连接数据库！");
        }
        if (null == moduleName) {
            moduleName = rootPackage.substring(rootPackage.lastIndexOf(".") + 1);
            rootPackage = rootPackage.substring(0, rootPackage.lastIndexOf("."));
        }
        String[] tableNames = tables.replaceAll(" ", "").replaceAll("   ", "").split(",");

        byte[] data = codeGeneratorService.generatorCode(connect, rootPackage, moduleName, author, tableNames, isPlus);
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s-%s.zip", rootPackage, new Date().toLocaleString()));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-typeeam; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 生成代码，全部生成
     */
    @ApiOperation(value = "生成代码，全部表", notes = "生成代码，全部表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootPackage", value = "包名。如：com.chqiuu", defaultValue = "com.chqiuu", paramType = "query", required = true),
            @ApiImplicitParam(name = "moduleName", value = "模块名。如：user，最终生成代码，包名为com.chqiuu.user", paramType = "query", required = true),
            @ApiImplicitParam(name = "author", value = "创建人。用于注解", paramType = "query", required = true),
            @ApiImplicitParam(name = "isPlus", value = "是否为MyBatis-Plus", paramType = "query", required = true),
    })
    @GetMapping("/codeAll")
    public void codeAll(HttpSession httpSession, String rootPackage, String moduleName, String author, boolean isPlus, HttpServletResponse response) throws Exception {
        BaseConnect connect = (BaseConnect) httpSession.getAttribute("dbConnect");
        if (null == connect) {
            throw new UserException(ResultConstant.FAILED, "还未连接数据库，请先连接数据库！");
        }
        if (null == moduleName) {
            moduleName = rootPackage.substring(rootPackage.lastIndexOf(".") + 1);
            rootPackage = rootPackage.substring(0, rootPackage.lastIndexOf("."));
        }
        byte[] data = codeGeneratorService.generatorCodeAll(connect, rootPackage, moduleName, author, isPlus);
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
