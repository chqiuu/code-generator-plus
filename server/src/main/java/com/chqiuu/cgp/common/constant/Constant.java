package com.chqiuu.cgp.common.constant;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.chqiuu.cgp.connect.BaseConnect;

/**
 * 公共变量
 *
 * @author chqiu
 */
public class Constant {

    /**
     * 系统默认值
     */
    public static class DefaultValues {
        /**
         * 系统默认密码
         */
        public static final String DEFAULT_PASSWORD = "123456";
        /**
         * 加密后的默认密码
         */
        public static final String DEFAULT_PASSWORD_ENCODE = "$2a$10$AaFMnxj0UrAfSel0g3zqtuKQYWGlxnq9igF5DOfiPdPEo5e6UNZLS";
    }

    /**
     * 系统全局变量
     */
    public static class Public {
        /**
         * 设备验证码缓存前缀
         */
        public final static String VERIFICATION_CODE_REDIS_PREFIX = "gen-vc:";
        /**
         * 微信二维码登录唯一值
         */
        public final static String WECHAT_LOGIN_QR_CODE_REDIS_PREFIX = "gen-login:";
        /**
         * 代码模版文件列表
         */
        public final static String[] CODE_FILE_TEMPLATES = new String[]{
                "Entity.java.ftl"
                , "Controller.java.ftl"
                , "Converter.java.ftl"
                , "Service.java.ftl"
                , "ServiceImpl.java.ftl"
                , "Dao.java.ftl"
                , "Mapper.xml.ftl"
                , "BriefDTO.java.ftl"
                , "DetailDTO.java.ftl"
                , "ListDTO.java.ftl"
                , "InputVO.java.ftl"
                , "ListQuery.java.ftl"
                , "PageQuery.java.ftl"
                , "menu.sql.ftl"
                , "index.vue.ftl"
                , "add-or-update.vue.ftl"
                , "layuimini.add.html.ftl"
                , "layuimini.edit.html.ftl"
                , "layuimini.table.html.ftl"};
        /**
         * 初始化ID生成器，调用方式 snowflake.nextId();
         */
        public static Snowflake SNOW_FLAKE = IdUtil.getSnowflake(1, 2);
    }

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
