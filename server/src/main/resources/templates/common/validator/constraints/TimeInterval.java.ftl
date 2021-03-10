package ${rootPackage}.common.validator.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 时间区间校验
 *
 * @author ${author}
 */
@Documented
@Target({TYPE, PARAMETER, FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeIntervalValidator.class)
@Repeatable(TimeInterval.List.class)
public @interface TimeInterval {

    String beginTime() default "beginDate";

    String endTime() default "endDate";

    String message() default "截止时间不能在开始时间之前";

    Class${r'<'}?>[] groups() default {};

    Class${r'<'}? extends Payload>[] payload() default {};

    @Target({TYPE, PARAMETER, FIELD, METHOD, ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        TimeInterval[] value();
    }
}
