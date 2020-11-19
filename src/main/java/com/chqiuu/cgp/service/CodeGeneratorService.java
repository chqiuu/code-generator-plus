package com.chqiuu.cgp.service;

import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.chqiuu.cgp.dto.CodePreviewDTO;
import com.chqiuu.cgp.vo.GeneratorTableVO;

import java.util.List;

/**
 * 代码生成业务层
 *
 * @author chqiu
 */
public interface CodeGeneratorService {
    /**
     * 连接数据库
     *
     * @param driverClassEnum 数据库驱动
     * @param server          服务器地址
     * @param port            端口号
     * @param database        数据库名
     * @param username        登录名
     * @param password        密码
     * @return 连接是否成功
     */
    BaseConnect connectDatabase(DriverClassEnum driverClassEnum, String server, Integer port, String database, String username, String password) throws Exception;

    /**
     * 通过表名模糊查询
     *
     * @param connect   数据库连接
     * @param tableName 表名
     * @return 数据库表列表
     */
    List<TableEntity> queryTableList(BaseConnect connect, String tableName);

    /**
     * 查询表信息
     *
     * @param connect   数据库连接
     * @param tableName 待查询表
     * @return 表信息
     */
    TableEntity queryTable(BaseConnect connect, String tableName);

    /**
     * 查询表字段信息
     *
     * @param connect   数据库连接
     * @param tableName 待查询表
     * @return 表字段信息
     */
    List<ColumnEntity> queryColumns(BaseConnect connect, String tableName);

    /**
     * 生成代码预览
     *
     * @param connect     数据库连接
     * @param rootPackage 包名
     * @param moduleName  模块名
     * @param author      创建人
     * @param tableName   生成代码的表
     * @param isPlus      是否为MyBatis-Plus
     * @return 生成代码预览
     */
    List<CodePreviewDTO> preview(BaseConnect connect, String rootPackage, String moduleName, String author, String tableName, boolean isPlus);

    /**
     * 生成代码
     *
     * @param connect     数据库连接
     * @param rootPackage 包名
     * @param moduleName  模块名
     * @param author      创建人
     * @param tableNames  生成代码的表
     * @param isPlus      是否为MyBatis-Plus
     * @return 字节码
     */
    byte[] generatorCode(BaseConnect connect, String rootPackage, String moduleName, String author, String[] tableNames, boolean isPlus);

    /**
     * 生成全部表代码
     *
     * @param connect     数据库连接
     * @param rootPackage 包名
     * @param moduleName  模块名
     * @param author      创建人
     * @param isPlus      是否为MyBatis-Plus
     * @return 字节码
     */
    byte[] generatorCodeAll(BaseConnect connect, String rootPackage, String moduleName, String author, boolean isPlus);

    /**
     * 多表批量生成代码
     *
     * @param driverClassEnum
     * @param rootPackage     项目主包名
     * @param author          创建人
     * @param isPlus          是否为MyBatis-Plus
     * @param tables          需要生成的表
     * @param allTables       所有表
     * @return 字节码
     */
    byte[] generatorCodes(DriverClassEnum driverClassEnum, String rootPackage, String author, Boolean isPlus, List<GeneratorTableVO> tables, List<TableEntity> allTables);

}
