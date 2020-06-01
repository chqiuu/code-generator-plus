package com.chqiuu.cgp.common.constant;


import com.chqiuu.cgp.connect.BaseConnect;

/**
 * 公共变量
 *
 * @author chqiu
 */
public class Constant {

    /**
     * 数据库
     */
    public static class Datebase {
        /**
         * 数据库连接
         */
        public static BaseConnect DATABASE_CONNECT;
        /**
         * 数据库驱动
         */
        public static String DATABASE_DRIVER_CLASS;
        /**
         * 数据库服务器地址
         */
        public static String DATABASE_SERVER;
        /**
         * 数据库名
         */
        public static String DATABASE_NAME;
        /**
         * 数据库端口号
         */
        public static String DATABASE_PORT;
        /**
         * 数据库登录名
         */
        public static String DATABASE_USER;
        /**
         * 数据库密码
         */
        public static String DATABASE_PASS;
    }

    /**
     * 资源文件路径
     */
    public static class ResourcePath {
        /**
         * 静态资源跟路径（不带resource）
         */
        public static String LOCAL_RESOURCE_PATH;
        /**
         * 上传文件临时存储路径
         */
        public static String FILE_DIR = "";
        /**
         * 临时文件存储路径
         */
        public static String FILE_DIR_TMP = "";

        public ResourcePath() {
        }
    }
}
