package com.chqiuu.cgp.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * MWC配置
 *
 * @author chqiu
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //设置静态资源
        registry.addResourceHandler("/static/**", "/resource/**").
                addResourceLocations("classpath:/static/", "classpath:/resource/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //设置首页地址
        registry.addViewController("/").setViewName("redirect:/static/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * 文件上传下载配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大 KB,MB
        factory.setMaxFileSize(DataSize.ofMegabytes(512L));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofGigabytes(1L));
        return factory.createMultipartConfig();
    }
}
