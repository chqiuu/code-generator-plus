package com.chqiuu.cgp.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer; // 来自 Spring

import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.chqiuu.cgp.common.constant.Constant.Public.FTL_TEMPLATE_NAMES;

@Component
public class FtlScanner {

    @Autowired
    private ApplicationContext applicationContext; // 或者注入 ResourceLoader

    @Autowired(required = false) // 如果 FreeMarkerConfigurer 可能不存在，设为 false
    private FreeMarkerConfigurer freeMarkerConfigurer;



    @PostConstruct // 在 Bean 初始化后执行扫描
    public void scanFtlFiles() {
        if (freeMarkerConfigurer == null) {
            System.out.println("FreeMarkerConfigurer not found, skipping FTL scan.");
            return;
        }

        // 1. 确定基础搜索路径 (从 FreeMarkerConfigurer 获取)
        // 注意: FreeMarkerConfigurer 没有直接获取 templateLoaderPaths 的公共方法
        // 你可能需要检查你的配置类或者直接硬编码你知道的路径
        // 这里假设你知道路径是 "classpath:/templates/"
        // 如果配置了多个路径，需要对每个路径进行扫描
        String baseSearchPath = "classpath:/templates/"; // ****** 需要根据你的实际配置修改 ******

        // 2. 构建扫描模式
        // classpath*: 表示搜索所有 classpath 根路径
        // **/: 表示递归搜索所有子目录
        // *.ftl: 表示匹配所有以 .ftl 结尾的文件
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                baseSearchPath.substring("classpath:".length()) + // 去掉 "classpath:" 前缀
                "**/*.ftl";

        System.out.println("Scanning for FTL files with pattern: " + pattern);

        // 3. 执行扫描
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(applicationContext);
        try {
            Resource[] resources = resolver.getResources(pattern);
            System.out.println("Found " + resources.length + " FTL resources.");

            // 4. 处理结果 (获取相对于模板加载路径的名称)
            String basePathUri = resolver.getResource(baseSearchPath).getURI().toString();
            if (!basePathUri.endsWith("/")) {
                basePathUri += "/";
            }

            for (Resource resource : resources) {
                String resourceUri = resource.getURI().toString();
                if (resource.isReadable() && resourceUri.startsWith(basePathUri)) {
                    // 计算相对于 baseSearchPath 的路径，这通常是 FreeMarker 加载模板时使用的名称
                    String templateName = resourceUri.substring(basePathUri.length());
                    if (templateName.startsWith("common/")) {
                        continue;
                    }
                    FTL_TEMPLATE_NAMES.add(templateName);
                    System.out.println("Found FTL template: " + templateName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error scanning for FTL files: " + e.getMessage());
            // 处理异常
        }
    }
}