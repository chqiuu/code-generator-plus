package ${rootPackage}.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.Getter;

/**
 * 登录方式
 * 登录名、手机号码、电子邮箱
 *
 * @author ${author}
 */
@Getter
public enum LoginTypeEnum implements IEnum${r'<'}Integer> {

    /**
     * 登录名
     */
    LOGIN_NAME(1, "登录名"),
    /**
     * 手机号码
     */
    MOBILE_NUMBER(2, "手机号码"),
    /**
     * 电子邮箱
     */
    EMAIL(3, "电子邮箱");


    LoginTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final int code;

    private final String desc;

    @Override
    public Integer getValue() {
        return code;
    }

    public static LoginTypeEnum getByCode(int code) {
        for (LoginTypeEnum e : values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}
