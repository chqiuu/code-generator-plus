package com.chqiuu.cgp;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.chqiuu.cgp.util.BrowseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 项目启动主入口
 *
 * @author chqiu
 */
@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class CodeGeneratorPlusApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext application = SpringApplication.run(CodeGeneratorPlusApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = null == env.getProperty("server.port") || "80".equals(env.getProperty("server.port")) ? "" : ":" + env.getProperty("server.port");
        String path = null == env.getProperty("server.servlet.context-path") ? "" : env.getProperty("server.servlet.context-path");
        String portPath = port + path;
        String delimiter = String.format("%100s", "").replaceAll("\\s", "=");
        log.info("\n{}\n【{}】项目已启动完成\n访问地址:\n\t" +
                        "Local: \t\thttp://localhost{}\n\t" +
                        "External: \thttp://{}{}\n\t" +
                        "Api: \t\thttp://localhost{}/swagger/doc.html\n{}"
                , delimiter
                , env.getProperty("spring.application.name"), portPath, ip, portPath, portPath
                , delimiter);
        BrowseUtil.openBrowseByUrl(String.format("http://localhost%s/static/page/index.html", portPath));
        BrowseUtil.openBrowseByUrl(String.format("http://localhost%s/static/ant-vue/index.html", portPath));
    }
}
