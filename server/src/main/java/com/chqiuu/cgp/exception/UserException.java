package com.chqiuu.cgp.exception;


import com.chqiuu.cgp.common.domain.ResultEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常处理
 *
 * @author chqiu
 */
public class UserException extends RuntimeException {

    @Getter
    @Setter
    private ResultEnum resultEnum;

    public UserException(ResultEnum resultEnum) {
        this(resultEnum, resultEnum.getMessage());
    }

    public UserException(ResultEnum resultEnum, Throwable e) {
        this(resultEnum, resultEnum.getMessage(), e);
    }

    public UserException(ResultEnum resultEnum, String message) {
        super(message);
        this.resultEnum = resultEnum;
    }

    public UserException(ResultEnum resultEnum, String message, Throwable e) {
        super(message, e);
        this.resultEnum = resultEnum;
    }
}
