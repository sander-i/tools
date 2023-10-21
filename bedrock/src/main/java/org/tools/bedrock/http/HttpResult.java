package org.tools.bedrock.http;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/17 17:29
 * @description 响应实体
 * @scope http
 */
@Data
@Accessors(chain = true)
public class HttpResult<T> implements Serializable {

    private static final long serialVersionUID = -955286493494332572L;
    /**
     * 响应编码
     */
    private Integer code;
    /**
     * 响应状态
     */
    private String status;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private transient T data;

    private HttpResult() {
    }

    private HttpResult(HttpEnum httpEnum) {
        this.setCode(httpEnum.getCode());
        this.setStatus(httpEnum.getStatus());
        this.setMsg(httpEnum.getMsg());
    }

    private HttpResult(Integer code, String status, String message) {
        this.setCode(code);
        this.setStatus(status);
        this.setMsg(message);
    }

    public static <T> HttpResult<T> success() {
        return new HttpResult<>(HttpEnum.OK);
    }

    public static HttpResult<String> success(String data) {
        return new HttpResult<String>(HttpEnum.OK).setData(data);
    }

    public static <T> HttpResult<T> success(T data) {
        return new HttpResult<T>(HttpEnum.OK).setData(data);
    }

    public static <T> HttpResult<T> success(HttpEnum httpEnum) {
        return new HttpResult<>(httpEnum);
    }

    public static HttpResult<String> success(HttpEnum httpEnum, String data) {
        return new HttpResult<String>(httpEnum).setData(data);
    }

    public static <T> HttpResult<T> success(HttpEnum httpEnum, T data) {
        return new HttpResult<T>(httpEnum).setData(data);
    }

    public static <T> HttpResult<T> success(Integer code, String status, String message) {
        return new HttpResult<>(code, status, message);
    }

    public static HttpResult<String> success(Integer code, String status, String message, String data) {
        return new HttpResult<String>(code, status, message).setData(data);
    }

    public static <T> HttpResult<T> success(Integer code, String status, String message, T data) {
        return new HttpResult<T>(code, status, message).setData(data);
    }

    public static <T> HttpResult<T> failed() {
        return new HttpResult<>(HttpEnum.INTERNAL_SERVER_ERROR);
    }

    public static HttpResult<String> failed(String data) {
        return new HttpResult<String>(HttpEnum.INTERNAL_SERVER_ERROR).setData(data);
    }

    public static <T> HttpResult<T> failed(T data) {
        return new HttpResult<T>(HttpEnum.INTERNAL_SERVER_ERROR).setData(data);
    }

    public static <T> HttpResult<T> failed(HttpEnum httpEnum) {
        return new HttpResult<>(httpEnum);
    }

    public static HttpResult<String> failed(HttpEnum httpEnum, String data) {
        return new HttpResult<String>(httpEnum).setData(data);
    }

    public static <T> HttpResult<T> failed(HttpEnum httpEnum, T data) {
        return new HttpResult<T>(httpEnum).setData(data);
    }

    public static <T> HttpResult<T> failed(Integer code, String status, String message) {
        return new HttpResult<>(code, status, message);
    }

    public static HttpResult<String> failed(Integer code, String status, String message, String data) {
        return new HttpResult<String>(code, status, message).setData(data);
    }

    public static <T> HttpResult<T> failed(Integer code, String status, String message, T data) {
        return new HttpResult<T>(code, status, message).setData(data);
    }

}
