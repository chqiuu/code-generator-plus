package ${rootPackage}.common.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码格式校验
 *
 * @author ${author}
 */
public class MobileNumberValidator implements ConstraintValidator${r'<'}MobileNumber, String> {
    /**
     * 手机号码正则表达式
     */
    private static final Pattern MOBILE_NO_PATTERN = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");

    @Override
    public void initialize(MobileNumber constraint) {
    }

    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext context) {
        if (null == mobile || mobile.length() ${r'<'} 11) {
            return false;
        }
        Matcher m = MOBILE_NO_PATTERN.matcher(mobile);
        return m.matches();
    }
}
