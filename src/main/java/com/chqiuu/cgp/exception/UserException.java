package com.chqiuu.cgp.exception;


import com.chqiuu.cgp.common.constant.ResultConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * 自定义异常处理
 *
 * @author chqiu
 */
public class UserException extends RuntimeException {

    @Getter
    @Setter
    private ResultConstant resultConstant;

    public UserException(ResultConstant resultConstant) {
        this(resultConstant, resultConstant.getMessage());
    }

    public UserException(ResultConstant resultConstant, Throwable e) {
        this(resultConstant, resultConstant.getMessage(), e);
    }

    public UserException(ResultConstant resultConstant, String message) {
        super(message);
        this.resultConstant = resultConstant;
    }

    public UserException(ResultConstant resultConstant, String message, Throwable e) {
        super(message, e);
        this.resultConstant = resultConstant;
    }
}
