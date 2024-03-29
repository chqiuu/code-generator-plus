package com.chqiuu.cgp.service;

import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.SchemataEntity;
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
     * 获取数据库列表
     *
     * @param connect 数据库连接
     * @return 数据库列表
     */
    List<SchemataEntity> queryDatabaseList(BaseConnect connect);

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
     * @param driverClassEnum 数据库类型
     * @param rootPackage     包名
     * @param moduleName      模块名
     * @param author          创建人
     * @param tableName       生成代码的表
     * @param mappingName     Controller中URL映射名称
     * @param isMyBatisPlus   是否支持 MyBatis-Plus
     * @param isLayuimini     是否生成Layuimini后台管理代码
     * @param allTables       所有表
     * @return 生成代码预览
     */
    List<CodePreviewDTO> preview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author
            , String tableName, String mappingName, boolean isMyBatisPlus, boolean isLayuimini, String[] genMethods, List<TableEntity> allTables);

    /**
     * 生成代码预览
     *
     * @param driverClassEnum    数据库类型
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建人
     * @param tableName          生成代码的表
     * @param mappingName        Controller中URL映射名称
     * @param isMyBatisPlus   是否支持 MyBatis-Plus
     * @param isLayuimini     是否生成Layuimini后台管理代码
     * @param isMapstructEnabled 是否启用mapstruct对象转换工具
     * @param allTables          所有表
     * @return 生成代码预览
     */
    List<CodePreviewDTO> preview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author
            , String tableName, String mappingName,  boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, String[] genMethods, List<TableEntity> allTables);

    /**
     * 生成代码
     *
     * @param connect     数据库连接
     * @param rootPackage 包名
     * @param author      创建人
     * @param tables      生成代码的表
     * @param isMyBatisPlus   是否支持 MyBatis-Plus
     * @param isLayuimini     是否生成Layuimini后台管理代码
     * @return 字节码
     */
    byte[] generatorCode(BaseConnect connect, String rootPackage, String author, List<GeneratorTableVO> tables,  boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, String[] genMethods);

    /**
     * 生成全部表代码
     *
     * @param connect     数据库连接
     * @param rootPackage 包名
     * @param moduleName  模块名
     * @param author      创建人
     * @param isMyBatisPlus   是否支持 MyBatis-Plus
     * @param isLayuimini     是否生成Layuimini后台管理代码
     * @return 字节码
     */
    byte[] generatorCodeAll(BaseConnect connect, String rootPackage, String moduleName, String author, boolean isMyBatisPlus, boolean isLayuimini,  String[] genMethods);

    /**
     * 生成全部表代码
     *
     * @param connect            数据库连接
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建人
     * @param isMyBatisPlus   是否支持 MyBatis-Plus
     * @param isLayuimini     是否生成Layuimini后台管理代码
     * @param isMapstructEnabled 是否启用mapstruct对象转换工具
     * @return 字节码
     */
    byte[] generatorCodeAll(BaseConnect connect, String rootPackage, String moduleName, String author, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, String[] genMethods);

    /**
     * 多表批量生成代码
     *
     * @param driverClassEnum    数据库类型
     * @param rootPackage        项目主包名
     * @param author             创建人
     * @param isMyBatisPlus   是否支持 MyBatis-Plus
     * @param isLayuimini     是否生成Layuimini后台管理代码
     * @param isMapstructEnabled 是否启用mapstruct对象转换工具
     * @param tables             需要生成的表
     * @param genMethods         需要生成的方法
     * @param allTables          所有表
     * @return 字节码
     */
    byte[] generatorCodes(DriverClassEnum driverClassEnum, String rootPackage, String author,boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, String[] genMethods, List<GeneratorTableVO> tables, List<TableEntity> allTables);


}
