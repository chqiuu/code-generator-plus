package com.chqiuu.cgp.db;

import com.chqiuu.cgp.db.enums.DriverClassEnum;

/**
 * 数据库工厂类
 *
 * @author chqiu
 */
public class DatabaseFactory {

    public BaseDatabase create(DriverClassEnum driverClass) {
        switch (driverClass) {
            case MYSQL:
                return new MySqlDatabase();
            case SQLITE:
                //TODO
                break;
            case SQLSERVER:
                //TODO
                break;
            default:
                //TODO
                break;
        }
        return null;
    }
}
