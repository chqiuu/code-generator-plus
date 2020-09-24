package ${rootPackage}.common.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 校验枚举值有效性
 *
 * @author ${author}
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    /**
     * 设置默认提示文字
     */
    String message() default "enum.value.invalid";

    /**
     * 设置枚举类对象
     *
     * @return 枚举类对象
     */
    Class${r'<'}? extends Enum${r'<'}?>> enumClass();

    /**
     * 设置枚举校验的方法
     *
     * @return 枚举校验的方法
     */
    String enumMethod() default "isValidEnumByValue";

    /**
     * 设置是否允许为空
     *
     * @return 是否允许为空，默认为允许
     */
    boolean allowNull() default true;

    Class${r'<'}?>[] groups() default {};

    Class${r'<'}? extends Payload>[] payload() default {};
}
