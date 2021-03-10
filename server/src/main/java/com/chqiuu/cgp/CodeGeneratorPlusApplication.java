package com.chqiuu.cgp;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

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

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(CodeGeneratorPlusApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = "80".equals(env.getProperty("server.port")) ? "" : ":" + env.getProperty("server.port");
        String path = null == env.getProperty("server.servlet.context-path") ? "" : env.getProperty("server.servlet.context-path");
        String applicationName = env.getProperty("spring.application.name");
        log.info("\n----------------------------------------------------------\n\t" +
                applicationName + "启动完成，访问地址:\n\t" +
                "Local: \t\thttp://localhost" + port + path + "\n\t" +
                "Api: \t\thttp://localhost" + port + path + "/swagger\n\t" +
                "External: \thttp://" + ip + port + path + "\n" +
                "----------------------------------------------------------");
    }
}
