package com.chqiuu.cgp.config;

import com.chqiuu.cgp.common.constant.Constant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

/**
 * @author chqiu
 */
@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {
    /**
     * 是否启用API接口
     * swagger-enable
     */
    private boolean swaggerEnable = false;
    /**
     * 本机静态资源根路径，默认文件JAR包相对路径
     * local-resource-path
     */
    private String localResourcePath;
    /**
     * 是否启用map查询功能
     */
    private boolean mapQueryEnabled = false;
    /**
     * 是否启用@Data注解
     */
    private boolean lombokDataEnabled = true;

    public GeneratorProperties() {
        ApplicationHome h = new ApplicationHome(getClass());
        String basePath = h.getSource().getParentFile().toString();
        if (localResourcePath == null) {
            this.localResourcePath = basePath;
        } else if (!localResourcePath.contains(":")) {
            this.localResourcePath = basePath + localResourcePath;
        }
        // 将绝对路径存储到公用变量中
        Constant.ResourcePath.LOCAL_RESOURCE_PATH = this.localResourcePath;
    }
}
