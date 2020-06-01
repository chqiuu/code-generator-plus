package com.chqiuu.cgp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chqiuu.cgp.common.constant.ResultConstant;
import com.chqiuu.cgp.config.GeneratorProperties;
import com.chqiuu.cgp.connect.BaseConnect;
import com.chqiuu.cgp.connect.HikariConnect;
import com.chqiuu.cgp.db.BaseDatabase;
import com.chqiuu.cgp.db.DatabaseFactory;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.chqiuu.cgp.db.enums.JdbcTypeEnum;
import com.chqiuu.cgp.dto.ColumnDto;
import com.chqiuu.cgp.dto.GeneratorDto;
import com.chqiuu.cgp.exception.UserException;
import com.chqiuu.cgp.service.CodeGeneratorService;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        return database.queryTableList(connect, tableName);
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
    public byte[] generatorCode(BaseConnect connect, String codePackage, String moduleName, String author, String[] tableNames, boolean isPlus) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            for (String tableName : tableNames) {
                //查询表信息
                TableEntity table = queryTable(connect, tableName.trim());
                if (table == null) {
                    throw new UserException(ResultConstant.FAILED, "未找到指定数据库表，" + tableName);
                }
                //查询列信息
                List<ColumnEntity> columns = queryColumns(connect, tableName);
                //生成代码
                generatorCode(codePackage, moduleName, author, table, columns, zip, isPlus, properties.isMapQueryEnabled(), properties.isLombokDataEnabled());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    /**
     * 代码生成
     *
     * @param table             数据库表名
     * @param columns           字段列表
     * @param zip               压缩
     * @param isPlus            是否为MyBatis-Plus
     * @param mapQueryEnabled   是否启用map查询功能
     * @param lombokDataEnabled 是否启用@Data注解
     */
    private void generatorCode(TableEntity table, List<ColumnEntity> columns
            , ZipOutputStream zip, boolean isPlus, boolean mapQueryEnabled, boolean lombokDataEnabled) {
        generatorCode(null, null, null, table, columns, zip, isPlus, mapQueryEnabled, lombokDataEnabled);
    }

    /**
     * 代码生成
     *
     * @param codePackage       包名
     * @param moduleName        模块名
     * @param author            创建者
     * @param table             数据库表名
     * @param columns           字段列表
     * @param zip               压缩
     * @param isPlus            是否为MyBatis-Plus
     * @param mapQueryEnabled   是否启用map查询功能
     * @param lombokDataEnabled 是否启用@Data注解
     */
    private void generatorCode(String codePackage, String moduleName, String author, TableEntity table
            , List<ColumnEntity> columns, ZipOutputStream zip, boolean isPlus, boolean mapQueryEnabled
            , boolean lombokDataEnabled) throws NullPointerException {
        //表信息
        GeneratorDto dto = new GeneratorDto();
        dto.setTableName(table.getTableName());
        dto.setComment(table.getTableComment());
        // 表名转换成Java类名
        String className = tableToJava(dto.getTableName(), "");
        dto.setClassNameUpperCase(className);
        dto.setClassNameLowerCase(className.substring(0, 1).toLowerCase() + className.substring(1));

        //列信息
        List<ColumnDto> columsList = new ArrayList<ColumnDto>();
        for (ColumnEntity column : columns) {
            ColumnDto columnDto = new ColumnDto();
            //列名
            columnDto.setColumnName(column.getColumnName());
            columnDto.setDataType(column.getDataType());
            columnDto.setComment(column.getColumnComment());
            columnDto.setExtra(column.getExtra());
            // 字段详情
            String columnDetail = String.format("`%s` %s%s%s%s%s"
                    , column.getColumnName()
                    , column.getColumnType()
                    , "NO".equals(column.getIsNullable()) ? " NOT NULL" : ""
                    , null == column.getColumnDefault() ? "" : " DEFAULT " + (column.getColumnDefault().equals("") ? "''" : column.getColumnDefault())
                    , null == column.getExtra() ? "" : " " + column.getExtra()
                    , null == column.getColumnComment() ? "" : " COMMENT '" + column.getColumnComment() + "'");
            columnDto.setColumnDetail(columnDetail);

            //列名转换成Java属性名
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
        dto.setCodePackage(codePackage);
        // URI修改为缩写
        dto.setPathName(getPathName(codePackage, dto.getClassNameUpperCase()));
        dto.setAuthor(author);
        dto.setPlusEnabled(isPlus ? 1 : 0);
        dto.setMapQueryEnabled(mapQueryEnabled ? 1 : 0);
        dto.setLombokDataEnabled(lombokDataEnabled ? 1 : 0);
        dto.setCreateTime(LocalDateTime.now());
        dto.setModuleName(moduleName);


        //获取模板列表
        List<String> templates = getTemplates();
        for (String templateStr : templates) {
            try (StringWriter writer = new StringWriter()) {
                Template template = configurer.getConfiguration().getTemplate(templateStr);
                template.process(dto, writer);
                String fileName = getFileName(templateStr, dto.getClassNameUpperCase(), codePackage, moduleName, isPlus);
                // 添加到zip
                zip.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(writer.toString(), zip, "UTF-8");
                zip.closeEntry();
            } catch (Exception e) {
                throw new UserException(ResultConstant.FAILED, "渲染模板失败，表名：" + dto.getTableName(), e);
            }
        }
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
     * 获取URL路径名称，用于生成Controller @RequestMapping
     *
     * @param codePackage 包名
     * @param classname   对象名称
     * @return
     */
    private String getPathName(String codePackage, String classname) {
        String builder = "/" +
                codePackage.substring(codePackage.lastIndexOf('.') + 1, codePackage.lastIndexOf('.') + 2) +
                "/" +
                getAcronym(classname);
        return builder.toLowerCase() + "c";
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
     * 获取FTL模板文件路径
     *
     * @return FTL模板文件路径
     * @throws IOException
     */
    private String getTemplateLoaderPath() throws IOException {
        MultiTemplateLoader multiTemplateLoader = (MultiTemplateLoader) configurer.getConfiguration().getTemplateLoader();
        if (multiTemplateLoader != null) {
            if (multiTemplateLoader.getTemplateLoader(0) instanceof FileTemplateLoader) {
                return ((FileTemplateLoader) multiTemplateLoader.getTemplateLoader(0)).baseDir.getCanonicalPath();
            }
        }
        return null;
    }

    private List<String> getTemplates() {
        List<String> templateNameItems = new ArrayList<>();
        log.debug("执行加载模板文件夹里面所有模板的名字...");
        try {
            templateNameItems = Files.list(Paths.get(Objects.requireNonNull(getTemplateLoaderPath()))).filter(f -> f.getFileName().toString().endsWith(".ftl"))
                    .map(p -> p.getFileName().toString()).collect(Collectors.toList());
            log.debug("执行加载模板文件夹里面所有模板的名字-->成功!");
        } catch (IOException e) {
            log.error("执行加载模板文件夹里面所有模板的名字-->失败:", e);
        }
        return templateNameItems;
    }

    /**
     * 获取文件名
     *
     * @param template    模板
     * @param className
     * @param packageName
     * @param isPlus
     * @return
     */
    private String getFileName(String template, String className, String packageName, String moduleName, boolean isPlus) {
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
        } else if (template.contains("Dao.java.ftl")) {
            if (isPlus) {
                return packagePath + "mapper" + File.separator + className + "Mapper.java";
            } else {
                return packagePath + "dao" + File.separator + className + "Dao.java";
            }
        } else if (template.contains("Mapper.xml.ftl")) {
            packagePath = "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + packageName.substring(packageName.lastIndexOf(".") + 1);
            return packagePath + File.separator + className + "Mapper.xml";
        }
        if (template.contains("index.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        }

        if (template.contains("add-or-update.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
        }
        return null;
    }

    @Override
    public byte[] generatorCodeAll(BaseConnect connect, String codePackage, String moduleName, String author, boolean isPlus) {
        List<TableEntity> list = queryTableList(connect, null);
        List<String> tableNameList = new ArrayList<>();
        for (TableEntity entity : list) {
            tableNameList.add(entity.getTableName());
        }
        String[] tableNames = new String[tableNameList.size()];
        tableNameList.toArray(tableNames);
        return generatorCode(connect, codePackage, moduleName, author, tableNames, isPlus);
    }
}
