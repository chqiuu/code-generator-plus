package com.chqiuu.cgp.connect;

import com.chqiuu.cgp.db.enums.DriverClassEnum;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * Hikari 连接器
 *
 * @author chqiu
 */
@Slf4j
public class HikariConnect extends BaseConnect {
    private DataSource dataSource;
    private DriverClassEnum driverClass;
    private Properties properties = null;

    public HikariConnect() {
        properties = new Properties();
        InputStream inputStream = null;
        try {
            // java应用
            String confile = Objects.requireNonNull(HikariConnect.class.getClassLoader().getResource("")).getPath() + "hikari.properties";
            File file = new File(confile);
            inputStream = new BufferedInputStream(new FileInputStream(file));
            properties.load(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public HikariConnect(DataSource dataSource) {
        try {
            this.setDataSource(dataSource);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public HikariConnect(DriverClassEnum driverClass, String url, String username, String password) {
        this.setDataSource(driverClass, url, username, password);
    }

    public HikariConnect(String driverClassName, String url, String username, String password) {
        DriverClassEnum driverClass = DriverClassEnum.getByDriverCLass(driverClassName);
        assert driverClass != null;
        this.setDataSource(driverClass, url, username, password);
    }

    /**
     * 数据库连接池
     *
     * @param driverClass 数据库驱动
     * @param server      数据库服务器IP
     * @param port        端口
     * @param database    数据库名
     * @param username    数据库用户名
     * @param password    数据库登录密码
     */
    public HikariConnect(DriverClassEnum driverClass, String server, Integer port, String database, String username, String password) {
        assert driverClass != null;
        this.setDataSource(driverClass, driverClass.getUrl(server, port, database), username, password);
    }

    private void setDataSource(DriverClassEnum driverClass, String url, String username, String password) {
        this.driverClass = driverClass;
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(5);
        config.setDriverClassName(driverClass.getDriverCLass());
        config.setJdbcUrl(url);
        if (null != username) {
            config.setUsername(username);
        }
        if (null != password) {
            config.setPassword(password);
        }
        if (driverClass.getValidationQuery() != null) {
            config.setConnectionTestQuery(driverClass.getValidationQuery());
        }
        this.setDataSource(new HikariDataSource(config));
    }


    /**
     * 根据类型获取数据源
     *
     * @return druid数据源
     */
    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public DriverClassEnum getDriverClassEnum() {
        return driverClass;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource != null) {
            // 参数不为空加载自定义配置
            this.dataSource = dataSource;
        } else {
            // 连接为空加载默认配置
            this.dataSource = new HikariDataSource(new HikariConfig(properties));
        }
    }
}
