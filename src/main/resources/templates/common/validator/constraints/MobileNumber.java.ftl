package ${rootPackage}.common.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号码格式校验注解
 *
 * @author ${author}
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileNumberValidator.class)
public @interface MobileNumber {
    String message() default "手机号码格式有误！";

    Class${r'<'}?>[] groups() default {};

    Class${r'<'}? extends Payload>[] payload() default {};
}