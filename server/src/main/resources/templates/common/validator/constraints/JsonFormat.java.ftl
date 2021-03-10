package ${rootPackage}.common.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * JSON格式数据校验
 *
 * @author ${author}
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonFormatValidator.class)
public @interface JsonFormat {
    String message() default "JSON格式有误！";

    Class${r'<'}?>[] groups() default {};

    Class${r'<'}? extends Payload>[] payload() default {};
}