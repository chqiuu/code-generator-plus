package ${rootPackage}.common.exception;


import ${rootPackage}.common.domain.ResultConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常处理
 *
 * @author ${author}
 */
public class UserException extends RuntimeException {

    @Getter
    @Setter
    private ResultConstant resultConstant;

    public UserException() {
        super();
    }

    public UserException(ResultConstant resultConstant) {
        this(resultConstant, resultConstant.getMessage());
    }

    public UserException(ResultConstant resultConstant, String message) {
        super(message);
        this.resultConstant = resultConstant;
    }
}
