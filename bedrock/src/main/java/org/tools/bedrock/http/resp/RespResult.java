package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class RespResult<T> implements Serializable {

    private static final long serialVersionUID = -8914212094056460L;
    /**
     * 响应编码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private transient T data;

    private RespResult(RespEnum respEnum) {
        this.setCode(respEnum.getCode());
        this.setMsg(respEnum.getMsg());
    }

    private RespResult(Integer code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }

    public static <T> RespResult<T> success() {
        return new RespResult<>(RespEnum.OK);
    }

    public static RespResult<String> success(String data) {
        return new RespResult<String>(RespEnum.OK).setData(data);
    }

    public static <T> RespResult<T> success(T data) {
        return new RespResult<T>(RespEnum.OK).setData(data);
    }

    public static <T> RespResult<T> success(RespEnum respEnum) {
        return new RespResult<>(respEnum);
    }

    public static RespResult<String> success(RespEnum respEnum, String data) {
        return new RespResult<String>(respEnum).setData(data);
    }

    public static <T> RespResult<T> success(RespEnum respEnum, T data) {
        return new RespResult<T>(respEnum).setData(data);
    }

    public static <T> RespResult<T> success(Integer code, String msg) {
        return new RespResult<>(code, msg);
    }

    public static <T> RespResult<T> success(Integer code, String msg, T data) {
        return new RespResult<T>(code, msg).setData(data);
    }

    public static <T> RespResult<T> failed() {
        return new RespResult<>(RespEnum.INTERNAL_SERVER_ERROR);
    }

    public static RespResult<String> failed(String data) {
        return new RespResult<String>(RespEnum.INTERNAL_SERVER_ERROR).setData(data);
    }

    public static <T> RespResult<T> failed(T data) {
        return new RespResult<T>(RespEnum.INTERNAL_SERVER_ERROR).setData(data);
    }

    public static <T> RespResult<T> failed(RespEnum respEnum) {
        return new RespResult<>(respEnum);
    }

    public static RespResult<String> failed(RespEnum respEnum, String data) {
        return new RespResult<String>(respEnum).setData(data);
    }

    public static <T> RespResult<T> failed(RespEnum respEnum, T data) {
        return new RespResult<T>(respEnum).setData(data);
    }

    public static <T> RespResult<T> failed(Integer code, String msg) {
        return new RespResult<>(code, msg);
    }

    public static <T> RespResult<T> failed(Integer code, String msg, T data) {
        return new RespResult<T>(code, msg).setData(data);
    }

}
