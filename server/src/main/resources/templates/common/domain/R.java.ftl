package ${rootPackage}.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;
import java.util.Formatter;

/**
 * 响应信息主体
 */
@Getter
@ApiModel(value = "响应信息主体")
public class R${r'<'}T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 状态码：1成功，其他均为失败【详见错误状态码表】
     */
    @ApiModelProperty(value = "状态码")
    private int code;
    /**
     * 成功为success，其他为失败原因
     */
    @ApiModelProperty(value = "消息")
    private Object message = "success";
    /**
     * 数据结果集
     */
    @ApiModelProperty(value = "数据结果集")
    private T data;
    /**
     * 当前时间
     */
    @ApiModelProperty(value = "时间戳")
    private final long time = System.currentTimeMillis();

    public R${r'<'}T> setMessage(Object message) {
        this.message = message;
        return this;
    }

    public R${r'<'}T> setMessage(String format, Object... args) {
        this.message = new Formatter().format(format, args).toString();
        return this;
    }

    public R() {
    }

    /**
     * 使用枚举类中模版消息
     *
     * @param resultConstant ResultConstant
     * @param data           数据结果集
     */
    private R(ResultConstant resultConstant, T data) {
        this.code = resultConstant.getCode();
        this.message = resultConstant.getMessage();
        this.data = data;
    }

    public static ${r'<'}T> R${r'<'}T> ok() {
        return restResult(ResultConstant.SUCCESS, null, null);
    }

    public static ${r'<'}T> R${r'<'}T> ok(T data) {
        return restResult(ResultConstant.SUCCESS, null, data);
    }

    public static ${r'<'}T> R${r'<'}T> ok(T data, Object message) {
        return restResult(ResultConstant.SUCCESS, message, data);
    }

    public static ${r'<'}T> R${r'<'}T> failed(ResultConstant resultConstant) {
        return restResult(resultConstant, null, null);
    }

    public static ${r'<'}T> R${r'<'}T> failed(ResultConstant resultConstant, Object message) {
        return restResult(resultConstant, message, null);
    }

    public static ${r'<'}T> R${r'<'}T> failed(ResultConstant resultConstant, Object message, T data) {
        return restResult(resultConstant, message, data);
    }

    private static ${r'<'}T> R${r'<'}T> restResult(ResultConstant resultConstant, Object message, T data) {
        R${r'<'}T> apiResult = new R${r'<'}>(resultConstant, data);
        if (null != message) {
            apiResult.setMessage(message);
        }
        return apiResult;
    }
}
