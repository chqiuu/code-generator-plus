package com.chqiuu.cgp.connect;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.chqiuu.cgp.db.enums.DriverClassEnum;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 获取Druid数据库连接
 *
 * @author CHQIU
 */
@Slf4j
public class DruidConnect extends BaseConnect {
    private DataSource dataSource;
    private DriverClassEnum driverClass;
    private Properties properties = null;

    public DruidConnect() {
        properties = new Properties();
        InputStream inputStream = null;
        try {
            // java应用
            String confile = Objects.requireNonNull(DruidConnect.class.getClassLoader().getResource("")).getPath() + "druid.properties";
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

    public DruidConnect(DataSource dataSource) {
        try {
            this.setDataSource(dataSource);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public DruidConnect(DriverClassEnum driverClass, String url, String username, String password) throws Exception {
        this.setDataSource(driverClass, url, username, password);
    }

    public DruidConnect(String driverClassName, String url, String username, String password) throws Exception {
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
    public DruidConnect(DriverClassEnum driverClass, String server, Integer port, String database, String username, String password) throws Exception {
        assert driverClass != null;
        this.setDataSource(driverClass, driverClass.getUrl(server, port, database), username, password);
    }

    private void setDataSource(DriverClassEnum driverClass, String url, String username, String password) throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        this.driverClass = driverClass;
        properties.put("driverClassName", driverClass.getDriverCLass());
        properties.put("url", url);
        if (null != username) {
            properties.put("username", username);
        }
        if (null != password) {
            properties.put("password", password);
        }
        properties.put("initialSize", "1");
        properties.put("maxActive", "300");
        properties.put("maxWait", "600000");
        properties.put("timeBetweenEvictionRunsMillis", "600000");
        properties.put("minEvictableIdleTimeMillis", "300000");
        if (driverClass.getValidationQuery() != null) {
            properties.put("validationQuery", driverClass.getValidationQuery());
        }
        properties.put("testWhileIdle", "false");
        properties.put("testOnBorrow", "false");
        properties.put("testOnReturn", "false");
        properties.put("poolPreparedStatements", "false");
        properties.put("maxPoolPreparedStatementPerConnectionSize", "200");
        properties.put("removeAbandoned", "true");
        properties.put("removeAbandonedTimeout", "180");
        properties.put("logAbandoned", "true");
        properties.put("init", "true");

        DruidDataSource dataSource = new DruidDataSource();
        // 失败重连 true为不尝试重连
        dataSource.setBreakAfterAcquireFailure(true);
        DruidDataSourceFactory.config(dataSource, properties);
        this.setDataSource(dataSource);
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

    public void setDataSource(DataSource dataSource) throws Exception {
        if (dataSource != null) {
            // 参数不为空加载自定义配置
            this.dataSource = dataSource;
        } else {
            // 连接为空加载默认配置
            this.dataSource = DruidDataSourceFactory.createDataSource(properties);
        }
    }
}