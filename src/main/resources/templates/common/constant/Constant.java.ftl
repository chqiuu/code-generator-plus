package ${rootPackage}.common.constant;

/**
 * 系统全局变量
 *
 * @author ${author}
 */
public class Constant {

    /**
     * 系统全局变量
     */
    public static class DefaultValues {
        /**
         * 用户默认密码
         */
        public static final String DEFAULT_PASSWORD = "123456";
    }

    /**
     * 其它参数
     */
    public static class Other {
        public Other() {
        }
    }

    /**
     * Session 属性名公共变量
     */
    public static class SessionAttr {
        /**
         * 当前登陆用户信息
         */
        public static final String INFO = "INFO";
        /**
         * 电子邮箱验证码参数名
         */
        public static final String EMAIL_VERIFICATION_CODE = "EMAIL_VERIFICATION_CODE";
        /**
         * 图形验证码参数名
         */
        public static final String IMAGE_CODE = "IMAGE_CODE";
        public SessionAttr() {
        }
    }

    /**
     * 版权信息
     */
    public static class CopyrightInfo {
        /**
         * 系统名称
         */
        public static String APPLICATION_NAME = "";
        /**
         * 联系电话 contact number
         */
        public static String CONTACT_NUMBER = "";
        /**
         * 联系地址 contact address
         */
        public static String CONTACT_ADDRESS = "";
        /**
         * 联系邮箱 Contact e-mail
         */
        public static String CONTACT_EMAIL = "";
        /**
         * 技术支持 technical support
         */
        public static String TECHNICAL_SUPPORT = "技术中心";
        /**
         * 版权所有 Copyright
         */
        public static String COPYRIGHT = "";
        /**
         * 版权所有年 Copyright year
         */
        public static String COPYRIGHT_YEAR = "2020";
        /**
         * 备案号 AQ
         */
        public static String AQ = "";

        public CopyrightInfo() {
        }
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

    /**
     * 邮件服务器配置
     */
    public static class Email {
        /**
         * 邮件服务器主机地址 smtp.163.com
         */
        public static String HOST = "smtp.163.com";
        /**
         * 发件地址
         */
        public static String ADDRESS = null;
        /**
         * 发件人用户名
         */
        public static String FROM = null;
        /**
         * 发件人密码
         */
        public static String PASSWORD = null;
        /**
         * 发件人显示名称
         */
        public static String SHOW_USER = null;
        /**
         * 邮件服务器是否使用SSL
         */
        public static boolean SSL = false;
        /**
         * 邮件服务器端口
         */
        public static Integer PORT = 25;

        public Email() {
        }
    }
}