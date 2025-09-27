package com.chqiuu.cgp.service;

import cn.hutool.core.util.StrUtil;
import com.chqiuu.cgp.common.domain.ResultEnum;
import com.chqiuu.cgp.common.enums.GeneralMethodEnum;
import com.chqiuu.cgp.config.GeneratorProperties;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.connect.HikariConnect;
import com.chqiuu.cgp.db.BaseDatabase;
import com.chqiuu.cgp.db.DatabaseFactory;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.SchemataEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.chqiuu.cgp.db.enums.JdbcTypeEnum;
import com.chqiuu.cgp.dto.CodePreviewDTO;
import com.chqiuu.cgp.dto.ColumnDto;
import com.chqiuu.cgp.dto.GeneralMethodEnabledDTO;
import com.chqiuu.cgp.dto.TableMetadataDTO;
import com.chqiuu.cgp.exception.UserException;
import com.chqiuu.cgp.vo.GeneratorTableVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.chqiuu.cgp.common.constant.Constant.Public.FTL_TEMPLATE_NAMES;

/**
 * 代码生成业务层
 *
 * @author chqiu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final GeneratorProperties properties;
    private final FreeMarkerConfigurer configurer;

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
    public BaseConnect connectDatabase(DriverClassEnum driverClassEnum, String server, Integer port, String database, String username, String password) throws Exception {
        BaseConnect connect = new HikariConnect(driverClassEnum, server, port, database, username, password);
        if (null == connect.getDataSource()) {
            return null;
        } else {
            return connect;
        }
    }

    /**
     * 获取数据库列表
     *
     * @param connect 数据库连接
     * @return 数据库列表
     */
    public List<SchemataEntity> queryDatabaseList(BaseConnect connect) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        return database.queryDatabaseList(connect);
    }

    /**
     * 通过表名模糊查询
     *
     * @param connect   数据库连接
     * @param tableName 表名
     * @return 数据库表列表
     */
    public List<TableEntity> queryTableList(BaseConnect connect, String tableName) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        List<TableEntity> tables = database.queryTableList(connect, tableName);
        tables.forEach(table -> {
            String tableJavaName = WordUtils.capitalizeFully(table.getTableName(), new char[]{'_'}).replace("_", "");
            tableJavaName = tableJavaName.substring(0, 1).toLowerCase() + tableJavaName.substring(1);
            table.setTableJavaName(tableJavaName);
            // 定义警告信息
            List<String> warningMessages = new ArrayList<>();
            if (StrUtil.isEmpty(table.getTableComment())) {
                warningMessages.add("表注释为空，不利于生成代码注释！");
            }
            List<ColumnEntity> columns = database.queryColumns(connect, table.getTableName());
            columns.forEach(column -> {
                if (StrUtil.isEmpty(column.getColumnComment())) {
                    warningMessages.add(String.format("字段【%s】注释为空，不利于属性注释及参数校验提示信息的生成！", column.getColumnName()));
                }
            });
            table.setColumns(columns);
            table.setWarningMessages(warningMessages);
        });
        return tables;
    }

    /**
     * 查询表信息
     *
     * @param connect   数据库连接
     * @param tableName 待查询表
     * @return 表信息
     */
    public TableEntity queryTable(BaseConnect connect, String tableName) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        return database.queryTable(connect, tableName);
    }

    /**
     * 查询表字段信息
     *
     * @param connect   数据库连接
     * @param tableName 待查询表
     * @return 表字段信息
     */
    public List<ColumnEntity> queryColumns(BaseConnect connect, String tableName) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        return database.queryColumns(connect, tableName);
    }

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
    public List<CodePreviewDTO> preview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, String tableName, String mappingName, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, String[] genMethods, List<TableEntity> allTables) {
        return preview(driverClassEnum, rootPackage, moduleName, author, tableName, mappingName, isServiceInterface, isMyBatisPlus, isLayuimini, false, 2, genMethods, allTables);
    }

    /**
     * 生成代码预览
     *
     * @param driverClassEnum    数据库类型
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建人
     * @param tableName          生成代码的表
     * @param mappingName        Controller中URL映射名称
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否支持 MyBatis-Plus
     * @param isLayuimini        是否生成Layuimini后台管理代码
     * @param isMapstructEnabled 是否启用mapstruct对象转换工具
     * @param allTables          所有表
     * @return 生成代码预览
     */
    public List<CodePreviewDTO> preview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, String tableName, String mappingName, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods, List<TableEntity> allTables) {
        for (TableEntity table : allTables) {
            if (table.getTableName().equals(tableName)) {
                //生成代码
                return getTableCodePreview(driverClassEnum, rootPackage, moduleName, author, mappingName, table, table.getColumns(), isServiceInterface, isMyBatisPlus, isMapstructEnabled, apiVersion, genMethods, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
            }
        }
        return null;
    }

    private List<CodePreviewDTO> getTableCodePreview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, String mappingName, TableEntity table, List<ColumnEntity> columns, boolean isServiceInterface, boolean isPlus, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods, boolean mapQueryEnabled, boolean lombokDataEnabled) {
        // 获取表生成代码元数据信息
        TableMetadataDTO tableMetadata = getTableMetadata(driverClassEnum, rootPackage, moduleName, author, mappingName, table, columns, isPlus, isServiceInterface, false, isMapstructEnabled, apiVersion, genMethods, mapQueryEnabled, lombokDataEnabled);
        List<CodePreviewDTO> list = new ArrayList<>();
        // 模板列表
        for (String templateName : FTL_TEMPLATE_NAMES) {
            if (tableMetadata.getPlusEnabled() == 0) {
                if (templateName.contains("DTO.") || templateName.contains("VO.")) {
                    continue;
                }
            }
            // 排出不必要生成的文件
            if (checkGenerateFile(tableMetadata, templateName)) {
                continue;
            }
            try (StringWriter writer = new StringWriter()) {
                CodePreviewDTO codePreview = new CodePreviewDTO();
                codePreview.setPackageName(tableMetadata.getCodePackage());
                codePreview.setFileName(templateName);
                codePreview.setShowName(templateName.replace(".ftl", ""));
                Template template = configurer.getConfiguration().getTemplate(templateName);
                template.process(tableMetadata, writer);
                codePreview.setContent(writer.toString());
                codePreview.setLanguage(templateName.split("\\.")[1]);
                list.add(codePreview);
            } catch (Exception e) {
                e.printStackTrace();
                throw new UserException(ResultEnum.FAILED, String.format("渲染模板失败，表名：%s ；templateName：%s", tableMetadata.getTableName(), templateName), e);
            }
        }
        return list;
    }

    private boolean checkGenerateFile(TableMetadataDTO tableMetadata, String templateName) {
        if (tableMetadata.getLayuiminiEnabled() == 0) {
            if (templateName.startsWith("Layui") || templateName.contains(".vue.")) {
                return true;
            }
        }
        if ("MapStruct.java.ftl".equals(templateName) && tableMetadata.getMapstructEnabled() == 0) {
            return true;
        }
        if ("DetailDTO.java.ftl".equals(templateName) && tableMetadata.getGeneralMethod().getGetDetailByIdEnabled() == 0) {
            return true;
        }
        if ("ListQuery.java.ftl".equals(templateName) && tableMetadata.getGeneralMethod().getGetListEnabled() == 0) {
            return true;
        }
        if ("PageQuery.java.ftl".equals(templateName) && tableMetadata.getGeneralMethod().getGetPageEnabled() == 0) {
            return true;
        }
        if (tableMetadata.getGeneralMethod().getGetListEnabled() == 0 && tableMetadata.getGeneralMethod().getGetPageEnabled() == 0) {
            if ("ListDTO.java.ftl".equals(templateName)) {
                return true;
            }
        }
        if (tableMetadata.getGeneralMethod().getGetListEnabled() == 0 && tableMetadata.getGeneralMethod().getGetPageEnabled() == 0 && tableMetadata.getGeneralMethod().getGetDetailByIdEnabled() == 0) {
            if ("BriefDTO.java.ftl".equals(templateName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成代码
     *
     * @param connect            数据库连接
     * @param rootPackage        包名
     * @param author             创建人
     * @param tables             生成代码的表
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否支持 MyBatis-Plus
     * @param isLayuimini        是否生成Layuimini后台管理代码
     * @return 字节码
     */
    public byte[] generatorCode(BaseConnect connect, String rootPackage, String author, List<GeneratorTableVO> tables, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            for (GeneratorTableVO tableVO : tables) {
                //查询表信息
                TableEntity table = queryTable(connect, tableVO.getTableName().trim());
                if (table == null) {
                    throw new UserException(ResultEnum.FAILED, "未找到指定数据库表，" + tableVO.getTableName());
                }
                //查询列信息
                List<ColumnEntity> columns = queryColumns(connect, tableVO.getTableName());
                //生成代码
                generatorCode(connect.getDriverClassEnum(), rootPackage, tableVO.getModule(), author, tableVO.getMappingName(), table, columns, zip, isServiceInterface, isMyBatisPlus, isLayuimini, isMapstructEnabled, apiVersion, genMethods, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    /**
     * 代码生成
     *
     * @param driverClassEnum    数据库驱动类型
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建者
     * @param table              数据库表名
     * @param columns            字段列表
     * @param zip                压缩
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否为MyBatis-Plus
     * @param mapQueryEnabled    是否启用map查询功能
     * @param lombokDataEnabled  是否启用@Data注解
     */
    private void generatorCode(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, String mappingName, TableEntity table, List<ColumnEntity> columns, ZipOutputStream zip, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods, boolean mapQueryEnabled, boolean lombokDataEnabled) throws NullPointerException {
        // 获取表生成代码元数据信息
        TableMetadataDTO dto = getTableMetadata(driverClassEnum, rootPackage, moduleName, author, mappingName, table, columns, isServiceInterface, isMyBatisPlus, isLayuimini, isMapstructEnabled, apiVersion, genMethods, mapQueryEnabled, lombokDataEnabled);
        // 根据模型数据生成代码文件
        generateFiles(dto, zip);
    }

    /**
     * 获取表生成代码元数据信息
     *
     * @param driverClassEnum    数据库驱动类型
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建者
     * @param table              数据库表名
     * @param columns            字段列表
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否为MyBatis-Plus
     * @param mapQueryEnabled    是否启用map查询功能
     * @param lombokDataEnabled  是否启用@Data注解
     * @return 元数据信息
     */
    private TableMetadataDTO getTableMetadata(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, String mappingName, TableEntity table, List<ColumnEntity> columns, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods, boolean mapQueryEnabled, boolean lombokDataEnabled) {
        TableMetadataDTO dto = new TableMetadataDTO();
        dto.setTableName(table.getTableName());
        dto.setComment(StrUtil.isEmpty(table.getTableComment()) ? table.getTableName() : (table.getTableComment().endsWith("表") ? table.getTableComment().substring(0, table.getTableComment().length() - 1) : table.getTableComment()));
        if (StrUtil.isNotBlank(dto.getComment())) {
            dto.setCommentEscape(dto.getComment().replaceAll("\"", "\\\\\"").replaceAll("\r", " ").replaceAll("\n", " "));
        }
        dto.setDbType(driverClassEnum.getDbType());
        // 表名转换成Java类名
        String className = tableToJava(dto.getTableName(), "");
        dto.setClassNameUpperCase(className);
        dto.setClassNameLowerCase(className.substring(0, 1).toLowerCase() + className.substring(1));
        //列信息
        List<ColumnDto> columsList = new ArrayList<ColumnDto>();
        for (ColumnEntity column : columns) {
            ColumnDto columnDto = new ColumnDto();
            // 列名
            columnDto.setColumnName(column.getColumnName());
            columnDto.setDataType(column.getDataType());
            // 列描述
            columnDto.setComment(StrUtil.isEmpty(column.getColumnComment()) ? column.getColumnName() : column.getColumnComment());
            if (StrUtil.isNotBlank(columnDto.getComment())) {
                columnDto.setCommentEscape(columnDto.getComment().replaceAll("\"", "\\\\\"").replaceAll("\r", " ").replaceAll("\n", " "));
            }
            columnDto.setExtra(column.getExtra());
            columnDto.setColumnDetail(column.getDdl());
            // 字段长度
            columnDto.setCharlength(column.getCharacterMaximumLength() == null ? column.getNumericPrecision() : column.getCharacterMaximumLength());
            // 列名转换成Java属性名
            String attrName = columnToJava(columnDto.getColumnName());
            // 大写开头属性
            columnDto.setAttrNameUpperCase(attrName);
            // 小写开头属性
            columnDto.setAttrNameLowerCase(attrName.substring(0, 1).toLowerCase() + attrName.substring(1));
            // 列的数据类型，转换成Java类型
            columnDto.setAttrType(getJavaType(columnDto.getColumnName(), columnDto.getDataType()));
            // 是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && dto.getPk() == null) {
                dto.setPk(columnDto);
            }
            // 设置逻辑删除字段 is_deleted 或 delete_flag
            if ("is_deleted".equalsIgnoreCase(column.getColumnName()) || "delete_flag".equalsIgnoreCase(column.getColumnName())) {
                dto.setLogicDelete(column.getColumnName());
            }
            // 是否存在 BigDecimal 字段
            if ("BigDecimal".equalsIgnoreCase(columnDto.getAttrType())) {
                dto.setHasBigDecimalAttr(1);
            }
            // 是否存在 JSON 格式字段
            if ("JSONObject".equalsIgnoreCase(columnDto.getAttrType())) {
                dto.setHasJsonAttr(1);
            }
            columsList.add(columnDto);
        }
        dto.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (dto.getPk() == null) {
            dto.setPk(dto.getColumns().get(0));
        }
        // 表名首字母小写
        dto.setAcronymLowerCase(getAcronym(dto.getClassNameUpperCase()));
        // 表名首字母大写
        dto.setAcronymUpperCase(getAcronym(dto.getClassNameUpperCase()).toUpperCase());
        if (StrUtil.isBlank(rootPackage)) {
            rootPackage = "com.chqiuu";
        }
        dto.setRootPackage(rootPackage);
        dto.setCommonPackage(rootPackage.endsWith(".modules") ? rootPackage.substring(0, rootPackage.length() - 8) : rootPackage);
        dto.setModuleName(moduleName);
        dto.setCodePackage(String.format("%s.%s", rootPackage, moduleName));
        if (StrUtil.isEmpty(mappingName)) {
            // URI修改为缩写 Controller 中 @RequestMapping("${mappingName}")
            dto.setMappingName(String.format("/%s/%s", moduleName, getLastWord(dto.getTableName())));
        } else {
            dto.setMappingName(mappingName);
        }
        dto.setAuthor(author);
        dto.setServiceInterfaceEnabled(isServiceInterface ? 1 : 0);
        dto.setPlusEnabled(isMyBatisPlus ? 1 : 0);
        dto.setLayuiminiEnabled(isLayuimini ? 1 : 0);
        dto.setMapstructEnabled(isMapstructEnabled ? 1 : 0);
        GeneralMethodEnabledDTO generalMethod = new GeneralMethodEnabledDTO();
        for (String genMethod : genMethods) {
            GeneralMethodEnum generalMethodEnum = GeneralMethodEnum.getByMethod(genMethod);
            switch (generalMethodEnum != null ? generalMethodEnum : GeneralMethodEnum.ADD) {
                case UPDATE:
                    generalMethod.setUpdateEnabled(1);
                    break;
                case INSERT_IGNORE:
                    generalMethod.setInsertIgnoreEnabled(1);
                    break;
                case REPLACE:
                    generalMethod.setReplaceEnabled(1);
                    break;
                case GET_BRIEF_BY_ID:
                    generalMethod.setGetBriefByIdEnabled(1);
                    break;
                case GET_DETAIL_BY_ID:
                    generalMethod.setGetDetailByIdEnabled(1);
                    break;
                case GET_LIST:
                    generalMethod.setGetListEnabled(1);
                    break;
                case GET_PAGE:
                    generalMethod.setGetPageEnabled(1);
                    break;
                default:
                    generalMethod.setAddEnabled(1);
                    break;
            }
        }
        dto.setGeneralMethod(generalMethod);
        dto.setMapQueryEnabled(mapQueryEnabled ? 1 : 0);
        dto.setLombokDataEnabled(lombokDataEnabled ? 1 : 0);
        dto.setApiVersion(apiVersion == null ? 2 : apiVersion);
        dto.setCreateTime(LocalDateTime.now());
        return dto;
    }


    /**
     * 根据模型数据批量生成代码文件
     *
     * @param tableMetadata 表原数据
     * @param zip           压缩文件流
     */
    private void generateFiles(TableMetadataDTO tableMetadata, ZipOutputStream zip) {
        // 模板列表
        for (String templateStr : FTL_TEMPLATE_NAMES) {
            if (tableMetadata.getPlusEnabled() == 0) {
                if (templateStr.contains("DTO.") || templateStr.contains("VO.")) {
                    continue;
                }
            }
            // 排出不必要生成的文件
            if (checkGenerateFile(tableMetadata, templateStr)) {
                continue;
            }
            generateFile(tableMetadata, templateStr, zip, getFileName(templateStr, tableMetadata.getClassNameUpperCase(), tableMetadata.getRootPackage(), tableMetadata.getModuleName(), tableMetadata.getServiceInterfaceEnabled(), tableMetadata.getPlusEnabled()));
        }
    }

    /**
     * 根据模型数据生成代码文件
     *
     * @param generator    模型数据
     * @param templateName 模板文件
     * @param zip          压缩文件流
     * @param fileName     生成的文件名
     */
    private void generateFile(TableMetadataDTO generator, String templateName, ZipOutputStream zip, String fileName) {
        if (fileName == null) {
            return;
        }
        try (StringWriter writer = new StringWriter()) {
            Template template = configurer.getConfiguration().getTemplate(templateName);
            template.process(generator, writer);
            // 添加到zip
            zip.putNextEntry(new ZipEntry(fileName));
            IOUtils.write(writer.toString(), zip, "UTF-8");
            zip.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException(ResultEnum.FAILED, String.format("渲染模板失败，表名：%s ；文件名：%s", generator.getTableName(), fileName), e);
        }
    }

    /**
     * 获取文件名
     *
     * @param template                模板
     * @param className               类名
     * @param packageName             包名
     * @param moduleName              模块名
     * @param serviceInterfaceEnabled 是否生成Service接口
     * @param plusEnabled             是否支持plus
     * @return 文件名
     */
    private String getFileName(String template, String className, String packageName, String moduleName, int serviceInterfaceEnabled, int plusEnabled) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StrUtil.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        if (template.contains("Controller.java.ftl")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        } else if (template.contains("MapStruct.java.ftl")) {
            return packagePath + "mapstruct" + File.separator + className + "MapStruct.java";
        } else if (template.contains("Service.java.ftl")) {
            if (serviceInterfaceEnabled == 1) {
                return packagePath + "service" + File.separator + className + "Service.java";
            } else {
                return null;
            }
        } else if (template.contains("ServiceImpl.java.ftl")) {
            if (serviceInterfaceEnabled == 1) {
                return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
            } else {
                return packagePath + "service" + File.separator + className + "Service.java";
            }
        } else if (template.contains("Entity.java.ftl")) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        } else if (template.contains("BriefDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + className + "BriefDTO.java";
        } else if (template.contains("DetailDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + className + "DetailDTO.java";
        } else if (template.contains("ListDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + className + "ListDTO.java";
        } else if (template.contains("ListQuery.java.ftl")) {
            return packagePath + "query" + File.separator + className + "ListQuery.java";
        } else if (template.contains("PageQuery.java.ftl")) {
            return packagePath + "query" + File.separator + className + "PageQuery.java";
        } else if (template.contains("InputVO.java.ftl")) {
            return packagePath + "vo" + File.separator + className + "InputVO.java";
        } else if (template.contains("Dao.java.ftl")) {
            if (plusEnabled == 1) {
                return packagePath + "mapper" + File.separator + className + "Mapper.java";
            } else {
                return packagePath + "dao" + File.separator + className + "Dao.java";
            }
        } else if (template.contains("Mapper.xml.ftl")) {
            packagePath = "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName;
            return packagePath + File.separator + className + "Mapper.xml";
        }
        if (template.contains("menu.sql.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "menu" + File.separator + "modules" + File.separator + moduleName + File.separator + className + ".vue";
        }
        if (template.contains("index.vue.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" + File.separator + moduleName + File.separator + className + ".vue";
        }
        if (template.contains("add-or-update.vue.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" + File.separator + moduleName + File.separator + className + "-add-or-update.vue";
        }
        if (template.contains("LayuiAdd.html.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "page" + File.separator + moduleName + File.separator + className + "Add.html";
        }
        if (template.contains("LayuiDetail.html.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "page" + File.separator + moduleName + File.separator + className + "Detail.html";
        }
        if (template.contains("LayuiEdit.html.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "page" + File.separator + moduleName + File.separator + className + "Edit.html";
        }
        if (template.contains("LayuiTable.html.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "page" + File.separator + moduleName + File.separator + className + "Table.html";
        }
        return packagePath;
    }

    /**
     * 根据数据库数据类型获取java对象类型
     *
     * @param columnName 字段名称
     * @param dataType   数据库数据类型
     * @return java对象类型
     */
    private String getJavaType(String columnName, String dataType) {
        String javaType = "String";
        JdbcTypeEnum jdbcTypeEnum = JdbcTypeEnum.getByJdbcType(dataType);
        if (jdbcTypeEnum != null) {
            javaType = jdbcTypeEnum.getJavaType();
            // 若字段是is_开头的表示为boolean类型的字段
            if (StrUtil.isNotBlank(columnName) && columnName.startsWith("is_")) {
                javaType = "Boolean";
            }
            // 获取字段为date结束且为时间类型则转为LocalDate
            if (StrUtil.isNotBlank(columnName) && columnName.endsWith("Date")) {
                javaType = "LocalDate";
            }
        }
        return javaType;
    }

    /**
     * 获取首字母缩写
     *
     * @param type 字符串
     * @return 首字母缩写
     */
    private String getAcronym(String type) {
        StringBuilder builder = new StringBuilder();
        // 转为char数组
        char[] ch = type.toCharArray();
        // 得到大写字母
        for (char c : ch) {
            if (c >= 'A' && c <= 'Z') {
                builder.append(c);
            }
        }
        return builder.toString().toLowerCase();
    }

    /**
     * 获取字符串中最后一个单词，若最好单词为info则忽略，向前移一个单词
     *
     * @param tableName 表名
     * @return 字符串中最后一个单词
     */
    private String getLastWord(String tableName) {
        if (StrUtil.isBlank(tableName)) {
            return "";
        }
        tableName = tableName.replace("_info", "");
        if (tableName.contains("_")) {
            return tableName.substring(tableName.lastIndexOf("_") + 1);
        }
        return tableName;
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName   表名
     * @param tablePrefix 前缀
     * @return Java类名
     */
    private String tableToJava(String tableName, String tablePrefix) {
        if (StrUtil.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     */
    private String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 生成全部表代码
     *
     * @param connect            数据库连接
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建人
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否支持 MyBatis-Plus
     * @param isLayuimini        是否生成Layuimini后台管理代码
     * @return 字节码
     */
    public byte[] generatorCodeAll(BaseConnect connect, String rootPackage, String moduleName, String author, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, String[] genMethods) {
        return generatorCodeAll(connect, rootPackage, moduleName, author, isServiceInterface, isMyBatisPlus, isLayuimini, false, 2, genMethods);
    }

    /**
     * 生成全部表代码
     *
     * @param connect            数据库连接
     * @param rootPackage        包名
     * @param moduleName         模块名
     * @param author             创建人
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否支持 MyBatis-Plus
     * @param isLayuimini        是否生成Layuimini后台管理代码
     * @param isMapstructEnabled 是否启用mapstruct对象转换工具
     * @return 字节码
     */
    public byte[] generatorCodeAll(BaseConnect connect, String rootPackage, String moduleName, String author, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods) {
        List<TableEntity> list = queryTableList(connect, null);
        List<GeneratorTableVO> tables = new ArrayList<>();
        for (TableEntity entity : list) {
            GeneratorTableVO tableVO = new GeneratorTableVO();
            tableVO.setTableName(entity.getTableName());
            tableVO.setModule(moduleName);
            tables.add(tableVO);
        }
        return generatorCode(connect, rootPackage, author, tables, isServiceInterface, isMyBatisPlus, isLayuimini, isMapstructEnabled, apiVersion, genMethods);
    }

    /**
     * 多表批量生成代码
     *
     * @param driverClassEnum    数据库类型
     * @param rootPackage        项目主包名
     * @param author             创建人
     * @param isServiceInterface 是否生成Service接口
     * @param isMyBatisPlus      是否支持 MyBatis-Plus
     * @param isLayuimini        是否生成Layuimini后台管理代码
     * @param isMapstructEnabled 是否启用mapstruct对象转换工具
     * @param tables             需要生成的表
     * @param genMethods         需要生成的方法
     * @param allTables          所有表
     * @return 字节码
     */
    public byte[] generatorCodes(DriverClassEnum driverClassEnum, String rootPackage, String author, boolean isServiceInterface, boolean isMyBatisPlus, boolean isLayuimini, boolean isMapstructEnabled, Integer apiVersion, String[] genMethods, List<GeneratorTableVO> tables, List<TableEntity> allTables) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            tables.forEach(t -> {
                for (TableEntity table : allTables) {
                    if (table.getTableName().equals(t.getTableName())) {
                        //生成代码
                        generatorCode(driverClassEnum, rootPackage, t.getModule(), author, t.getMappingName(), table, table.getColumns(), zip, isServiceInterface, isMyBatisPlus, isLayuimini, isMapstructEnabled, apiVersion, genMethods, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}