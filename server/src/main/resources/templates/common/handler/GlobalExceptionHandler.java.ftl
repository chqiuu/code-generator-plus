package ${rootPackage}.common.handler;

import ${rootPackage}.common.domain.R;
import ${rootPackage}.common.domain.ResultConstant;
import ${rootPackage}.common.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;

/**
 * 全局异常处理器
 *
 * @author ${author}
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(UserException.class)
    public R${r'<'}String> handleUserException(UserException e) {
        return R.failed(e.getResultConstant(), e.getMessage());
    }

    /**
     * 没有访问权限
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R${r'<'}String> handleAccessDeniedException(AccessDeniedException e) {
        return R.failed(ResultConstant.PERMISSION_DENIED);
    }

    /**
     * 方法参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R${r'<'}String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List${r'<'}ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        for (ObjectError error : errors) {
            errorMsg.append(error.getDefaultMessage()).append(";");
        }

        FieldError fieldError = e.getBindingResult().getFieldError();
        if (null == fieldError) {
            return R.failed(ResultConstant.FAILED, errorMsg);
        }
        if (null == fieldError.getCode()) {
            return R.failed(ResultConstant.FAILED, "未知错误！");
        }
        if (null == fieldError.getDefaultMessage()) {
            return getResult(fieldError.getCode(), errorMsg);
        }
       /*
        Map${r'<'}String, String> msgMap = new HashMap${r'<'}>();
        msgMap.put("field", fieldError.getField());
        msgMap.put("errorMessage", fieldError.getDefaultMessage());
        return getResult(fieldError.getCode(), msgMap);
        返回消息对象，可精确到字段
        */
        return getResult(fieldError.getCode(), fieldError.getDefaultMessage());
    }

    /**
     * ValidationException
     * 参数验证失败
     */
    @ExceptionHandler(ValidationException.class)
    public R${r'<'}String> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return R.failed(ResultConstant.FAILED, e.getMessage());
    }

    /**
     * ConstraintViolationException
     * 接口中多参数格式校验时候调用
     * 参数验证失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R${r'<'}String> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation${r'<'}?> violation : e.getConstraintViolations()) {
            sb.append(violation.getMessage());
            sb.append("；");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return R.failed(ResultConstant.INVALID_USER_INPUT, sb.toString());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R${r'<'}String> handlerNoFoundException(Exception e) {
        return R.failed(ResultConstant.FAILED, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R${r'<'}String> handleDuplicateKeyException(DuplicateKeyException e) {
        return R.failed(ResultConstant.FAILED, "数据重复，请检查后提交");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R${r'<'}String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return R.failed(ResultConstant.FAILED, "不支持当前请求方法");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R${r'<'}String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return R.failed(ResultConstant.FAILED, "不支持当前媒体类型，ContentType类型有误");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R${r'<'}String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return R.failed(ResultConstant.FAILED,"参数解析失败");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R${r'<'}String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return R.failed(ResultConstant.FAILED, "缺少请求参数");
    }

    @ExceptionHandler(BindException.class)
    public R${r'<'}String> handleBindException(BindException e) {
        log.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return R.failed(ResultConstant.FAILED, "参数绑定失败=" + message);
    }


    /**
     * 其他未知类型错误
     *
     * @param e Exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R${r'<'}String> handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
        return R.failed(ResultConstant.FAILED, "系统繁忙,请稍后再试");
    }

    /**
     * 返回错误消息及代码
     *
     * @param code         错误类型代码
     * @param errorMessage 错误消息
     * @return 错误消息及代码
     */
    private R${r'<'}String> getResult(String code, Object errorMessage) {
        switch (code) {
            case "NotNull":
                // 验证注解的元素值不是null 任意类型
            case "NotEmpty":
                // 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0） CharSequence子类型、Collection、Map、数组
            case "NotBlank":
                /*
                验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的首位空格
                CharSequence子类型
                 */
                return R.failed(ResultConstant.PARAM_EMPTY_ERROR, errorMessage);
            case "URL":
                return R.failed(ResultConstant.URL_FORMAL_ERROR, errorMessage);
            case "Email":
                return R.failed(ResultConstant.EMAIL_FORMAL_ERROR, errorMessage);
            case "MobileNumber":
                return R.failed(ResultConstant.MOBILE_FORMAL_ERROR, errorMessage);
            case "Range":
                // 验证注解的元素值在最小值和最大值之间 BigDecimal,BigInteger,CharSequence, byte, short, int, long等原子类型和包装类型
                return R.failed(ResultConstant.RANGE_ERROR, errorMessage);
            case "Size":
                // 验证注解的元素值的在min和max（包含）指定区间之内，如字符长度、集合大小 字符串、Collection、Map、数组等
            case "Length":
                // 验证注解的元素值长度在min和max区间内 CharSequence子类型
            case "Past":
                // 验证注解的元素值（日期类型）比当前时间早 java.util.Date,java.util.Calendar;Joda Time类库的日期类型
            case "Future":
                // 验证注解的元素值（日期类型）比当前时间晚 java.util.Date,java.util.Calendar;Joda Time类库的日期类型
                return R.failed(ResultConstant.OVER_RANGE_ERROR, errorMessage);
            case "Pattern":
                // 验证注解的元素值与指定的正则表达式匹配 String，任何CharSequence的子类型
                return R.failed(ResultConstant.PARAM_FORMAL_ERROR, errorMessage);
            default:
                break;
        }
        return R.failed(ResultConstant.FAILED, errorMessage);
    }
}
