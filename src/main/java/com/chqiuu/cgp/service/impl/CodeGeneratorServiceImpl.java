package com.chqiuu.cgp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chqiuu.cgp.common.domain.ResultEnum;
import com.chqiuu.cgp.config.GeneratorProperties;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.connect.HikariConnect;
import com.chqiuu.cgp.db.BaseDatabase;
import com.chqiuu.cgp.db.DatabaseFactory;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.chqiuu.cgp.db.enums.JdbcTypeEnum;
import com.chqiuu.cgp.dto.CodePreviewDTO;
import com.chqiuu.cgp.dto.ColumnDto;
import com.chqiuu.cgp.dto.TableMetadataDTO;
import com.chqiuu.cgp.exception.UserException;
import com.chqiuu.cgp.service.CodeGeneratorService;
import com.chqiuu.cgp.vo.GeneratorTableVO;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
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

import static com.chqiuu.cgp.common.constant.Constant.Public.CODE_FILE_TEMPLATES;

@Slf4j
@Service
@AllArgsConstructor
public class CodeGeneratorServiceImpl implements CodeGeneratorService {
    private final GeneratorProperties properties;
    private final FreeMarkerConfigurer configurer;

    @Override
    public BaseConnect connectDatabase(DriverClassEnum driverClassEnum, String server, Integer port, String database, String username, String password) throws Exception {
        BaseConnect connect = new HikariConnect(driverClassEnum, server, port, database, username, password);
        if (null == connect.getDataSource()) {
            return null;
        } else {
            return connect;
        }
    }

    @Override
    public List<TableEntity> queryTableList(BaseConnect connect, String tableName) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        List<TableEntity> list = database.queryTableList(connect, tableName);
        list.forEach(t -> {
            t.setColumns(database.queryColumns(connect, t.getTableName()));
        });
        return list;
    }

    @Override
    public TableEntity queryTable(BaseConnect connect, String tableName) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        return database.queryTable(connect, tableName);
    }

    @Override
    public List<ColumnEntity> queryColumns(BaseConnect connect, String tableName) {
        BaseDatabase database = new DatabaseFactory().create(connect.getDriverClassEnum());
        return database.queryColumns(connect, tableName);
    }

    @Override
    public List<CodePreviewDTO> preview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, String tableName, boolean isPlus, List<TableEntity> allTables) {
        for (TableEntity table : allTables) {
            if (table.getTableName().equals(tableName)) {
                //生成代码
                return getTableCodePreview(driverClassEnum, rootPackage, moduleName, author, table, table.getColumns(), isPlus, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
            }
        }
        return null;
    }

    public List<CodePreviewDTO> preview(BaseConnect connect, String rootPackage, String moduleName, String author, String tableName, boolean isPlus) {
        //查询表信息
        TableEntity table = queryTable(connect, tableName.trim());
        if (table == null) {
            throw new UserException(ResultEnum.FAILED, "未找到指定数据库表，" + tableName);
        }
        //查询列信息
        List<ColumnEntity> columns = queryColumns(connect, tableName);
        // 生成代码
        return getTableCodePreview(connect.getDriverClassEnum(), rootPackage, moduleName, author, table, columns, isPlus, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
    }

    private List<CodePreviewDTO> getTableCodePreview(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, TableEntity table, List<ColumnEntity> columns, boolean isPlus, boolean mapQueryEnabled, boolean lombokDataEnabled) {
        // 获取表生成代码元数据信息
        TableMetadataDTO tableMetadata = getTableMetadata(driverClassEnum, rootPackage, moduleName, author, table, columns, isPlus, mapQueryEnabled, lombokDataEnabled);
        List<CodePreviewDTO> list = new ArrayList<>();
        // 模板列表
        for (String templateName : CODE_FILE_TEMPLATES) {
            if (tableMetadata.getPlusEnabled() == 0) {
                if (templateName.contains("DTO.") || templateName.contains("VO.")) {
                    continue;
                }
            }
            try (StringWriter writer = new StringWriter()) {
                CodePreviewDTO codePreview = new CodePreviewDTO();
                codePreview.setPackageName(tableMetadata.getCodePackage());
                codePreview.setFileName(templateName);
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

    @Override
    public byte[] generatorCode(BaseConnect connect, String rootPackage, String moduleName, String author, String[] tableNames, boolean isPlus) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            for (String tableName : tableNames) {
                //查询表信息
                TableEntity table = queryTable(connect, tableName.trim());
                if (table == null) {
                    throw new UserException(ResultEnum.FAILED, "未找到指定数据库表，" + tableName);
                }
                //查询列信息
                List<ColumnEntity> columns = queryColumns(connect, tableName);
                //生成代码
                generatorCode(connect.getDriverClassEnum(), rootPackage, moduleName, author, table, columns, zip, isPlus, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    /**
     * 代码生成
     *
     * @param driverClassEnum   数据库驱动类型
     * @param rootPackage       包名
     * @param moduleName        模块名
     * @param author            创建者
     * @param table             数据库表名
     * @param columns           字段列表
     * @param zip               压缩
     * @param isPlus            是否为MyBatis-Plus
     * @param mapQueryEnabled   是否启用map查询功能
     * @param lombokDataEnabled 是否启用@Data注解
     */
    private void generatorCode(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, TableEntity table
            , List<ColumnEntity> columns, ZipOutputStream zip, boolean isPlus, boolean mapQueryEnabled
            , boolean lombokDataEnabled) throws NullPointerException {
        // 获取表生成代码元数据信息
        TableMetadataDTO dto = getTableMetadata(driverClassEnum, rootPackage, moduleName, author, table, columns, isPlus, mapQueryEnabled, lombokDataEnabled);
        // 根据模型数据生成代码文件
        generateFiles(dto, zip);
    }

    /**
     * 获取表生成代码元数据信息
     *
     * @param driverClassEnum   数据库驱动类型
     * @param rootPackage       包名
     * @param moduleName        模块名
     * @param author            创建者
     * @param table             数据库表名
     * @param columns           字段列表
     * @param isPlus            是否为MyBatis-Plus
     * @param mapQueryEnabled   是否启用map查询功能
     * @param lombokDataEnabled 是否启用@Data注解
     * @return 元数据信息
     */
    private TableMetadataDTO getTableMetadata(DriverClassEnum driverClassEnum, String rootPackage, String moduleName, String author, TableEntity table, List<ColumnEntity> columns, boolean isPlus, boolean mapQueryEnabled, boolean lombokDataEnabled) {
        TableMetadataDTO dto = new TableMetadataDTO();
        dto.setTableName(table.getTableName());
        dto.setComment(StrUtil.isEmpty(table.getTableComment()) ? table.getTableName()
                : (table.getTableComment().endsWith("表") ? table.getTableComment().substring(0, table.getTableComment().length() - 1) : table.getTableComment()));
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
            //是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && dto.getPk() == null) {
                dto.setPk(columnDto);
            }
            //是否存在逻辑删除字段 is_deleted
            if ("is_deleted".equalsIgnoreCase(column.getColumnName())) {
                dto.setLogicDelete(1);
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
        dto.setModuleName(moduleName);
        dto.setCodePackage(String.format("%s.%s", rootPackage, moduleName));
        // URI修改为缩写
        dto.setPathName(String.format("/%s/%s", moduleName, getLastWord(dto.getTableName())));
        dto.setAuthor(author);
        dto.setPlusEnabled(isPlus ? 1 : 0);
        dto.setMapQueryEnabled(mapQueryEnabled ? 1 : 0);
        dto.setLombokDataEnabled(lombokDataEnabled ? 1 : 0);
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
        for (String templateStr : CODE_FILE_TEMPLATES) {
            if (tableMetadata.getPlusEnabled() == 0) {
                if (templateStr.contains("DTO.") || templateStr.contains("VO.")) {
                    continue;
                }
            }
            generateFile(tableMetadata, templateStr, zip, getFileName(templateStr, tableMetadata.getClassNameUpperCase()
                    , tableMetadata.getRootPackage(), tableMetadata.getModuleName(), tableMetadata.getPlusEnabled()));
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
        try (StringWriter writer = new StringWriter()) {
            Template template = configurer.getConfiguration().getTemplate(templateName);
            template.process(generator, writer);
            // 添加到zip
            zip.putNextEntry(new ZipEntry(fileName));
            IOUtils.write(writer.toString(), zip, "UTF-8");
            zip.closeEntry();
        } catch (Exception e) {
            throw new UserException(ResultEnum.FAILED, String.format("渲染模板失败，表名：%s ；文件名：%s", generator.getTableName(), fileName), e);
        }
    }

    /**
     * 获取文件名
     *
     * @param template    模板
     * @param className   类名
     * @param packageName 包名
     * @param moduleName  模块名
     * @param plusEnabled 是否支持plus
     * @return 文件名
     */
    private String getFileName(String template, String className, String packageName, String moduleName, int plusEnabled) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StrUtil.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        if (template.contains("Controller.java.ftl")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        } else if (template.contains("Service.java.ftl")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        } else if (template.contains("ServiceImpl.java.ftl")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        } else if (template.contains("Entity.java.ftl")) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
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
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "menu" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        }
        if (template.contains("index.vue.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        }
        if (template.contains("add-or-update.vue.ftl")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
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
     * 获取字符串中最后一个单词
     *
     * @param tableName 表名
     * @return 字符串中最后一个单词
     */
    private String getLastWord(String tableName) {
        if (StrUtil.isBlank(tableName)) {
            return "";
        }
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

    @Override
    public byte[] generatorCodeAll(BaseConnect connect, String rootPackage, String moduleName, String author, boolean isPlus) {
        List<TableEntity> list = queryTableList(connect, null);
        List<String> tableNameList = new ArrayList<>();
        for (TableEntity entity : list) {
            tableNameList.add(entity.getTableName());
        }
        String[] tableNames = new String[tableNameList.size()];
        tableNameList.toArray(tableNames);
        return generatorCode(connect, rootPackage, moduleName, author, tableNames, isPlus);
    }

    @Override
    public byte[] generatorCodes(DriverClassEnum driverClassEnum, String rootPackage, String author, Boolean isPlus, List<GeneratorTableVO> tables, List<TableEntity> allTables) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            tables.forEach(t -> {
                for (TableEntity table : allTables) {
                    if (table.getTableName().equals(t.getTableName())) {
                        //生成代码
                        generatorCode(driverClassEnum, rootPackage, t.getModule(), author, table, table.getColumns(), zip, isPlus, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
