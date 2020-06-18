package com.chqiuu.cgp.handler;

import com.chqiuu.cgp.common.constant.R;
import com.chqiuu.cgp.common.constant.ResultConstant;
import com.chqiuu.cgp.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author chqiu
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(UserException.class)
    public R<String> handleUserException(UserException e) {
        log.error(e.getMessage(), e);
        return R.failed(e.getResultConstant(), e.getMessage());
    }

    /**
     * 方法参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getResult(Objects.requireNonNull(Objects.requireNonNull(e.getBindingResult().getFieldError()).getCode())
                , Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public R<String> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return R.failed(ResultConstant.FAILED, e.getMessage());
    }

    /**
     * ConstraintViolationException
     * 接口中多参数格式校验时候调用
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<String> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            sb.append(violation.getMessage());
            sb.append("；");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return R.failed(ResultConstant.INVALID_USER_INPUT, sb.toString());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R<String> handlerNoFoundException(Exception e) {
        return R.failed(ResultConstant.FAILED, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return R.failed(ResultConstant.FAILED, "ContentType类型有误");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
        return R.failed(ResultConstant.FAILED, e.getMessage());
    }


    /**
     * 其他未知类型错误
     *
     * @param e Exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
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
    private R<String> getResult(String code, String errorMessage) {
        R<String> result;
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
                result = R.failed(ResultConstant.PARAM_EMPTY_ERROR, errorMessage);
                break;
            case "URL":
                result = R.failed(ResultConstant.URL_FORMAL_ERROR, errorMessage);
                break;
            case "Email":
                result = R.failed(ResultConstant.EMAIL_FORMAL_ERROR, errorMessage);
                break;
            case "MobileNumber":
                result = R.failed(ResultConstant.MOBILE_FORMAL_ERROR, errorMessage);
                break;
            case "Range":
                // 验证注解的元素值在最小值和最大值之间 BigDecimal,BigInteger,CharSequence, byte, short, int, long等原子类型和包装类型
                result = R.failed(ResultConstant.RANGE_ERROR, errorMessage);
                break;
            case "Size":
                // 验证注解的元素值的在min和max（包含）指定区间之内，如字符长度、集合大小 字符串、Collection、Map、数组等
            case "Length":
                // 验证注解的元素值长度在min和max区间内 CharSequence子类型
            case "Past":
                // 验证注解的元素值（日期类型）比当前时间早 java.util.Date,java.util.Calendar;Joda Time类库的日期类型
            case "Future":
                // 验证注解的元素值（日期类型）比当前时间晚 java.util.Date,java.util.Calendar;Joda Time类库的日期类型
                result = R.failed(ResultConstant.OVER_RANGE_ERROR, errorMessage);
                break;
            case "Pattern":
                // 验证注解的元素值与指定的正则表达式匹配 String，任何CharSequence的子类型
                result = R.failed(ResultConstant.PARAM_FORMAL_ERROR, errorMessage);
                break;
            default:
                result = R.failed(ResultConstant.FAILED, errorMessage);
                break;
        }
        return result;
    }
}
