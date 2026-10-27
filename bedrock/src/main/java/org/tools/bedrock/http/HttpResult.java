package org.tools.bedrock.http;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * HTTP 统一响应结果封装：success/failed 系列工厂方法与状态码透传。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
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
    private T data;

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

    /**
     * 构建无数据的成功响应
     *
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> HttpResult<T> success() {
        return new HttpResult<>(HttpEnum.OK);
    }

    /**
     * 构建携带字符串数据的成功响应
     *
     * @param data 返回数据
     * @return 成功响应对象
     */
    public static HttpResult<String> success(String data) {
        return new HttpResult<String>(HttpEnum.OK).setData(data);
    }

    /**
     * 构建携带泛型数据的成功响应
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 成功响应对象
     */
    public static <T> HttpResult<T> success(T data) {
        return new HttpResult<T>(HttpEnum.OK).setData(data);
    }

    /**
     * 按响应枚举构建无数据的成功响应
     *
     * @param httpEnum 响应枚举
     * @param <T>      数据类型
     * @return 成功响应对象
     */
    public static <T> HttpResult<T> success(HttpEnum httpEnum) {
        return new HttpResult<>(httpEnum);
    }

    /**
     * 按响应枚举构建携带字符串数据的成功响应
     *
     * @param httpEnum 响应枚举
     * @param data     返回数据
     * @return 成功响应对象
     */
    public static HttpResult<String> success(HttpEnum httpEnum, String data) {
        return new HttpResult<String>(httpEnum).setData(data);
    }

    /**
     * 按响应枚举构建携带泛型数据的成功响应
     *
     * @param httpEnum 响应枚举
     * @param data     返回数据
     * @param <T>      数据类型
     * @return 成功响应对象
     */
    public static <T> HttpResult<T> success(HttpEnum httpEnum, T data) {
        return new HttpResult<T>(httpEnum).setData(data);
    }

    /**
     * 按自定义 code/status/message 构建无数据的成功响应
     *
     * @param code    响应编码
     * @param status  响应状态
     * @param message 响应信息
     * @param <T>     数据类型
     * @return 成功响应对象
     */
    public static <T> HttpResult<T> success(Integer code, String status, String message) {
        return new HttpResult<>(code, status, message);
    }

    /**
     * 按自定义 code/status/message 构建携带字符串数据的成功响应
     *
     * @param code    响应编码
     * @param status  响应状态
     * @param message 响应信息
     * @param data    返回数据
     * @return 成功响应对象
     */
    public static HttpResult<String> success(Integer code, String status, String message, String data) {
        return new HttpResult<String>(code, status, message).setData(data);
    }

    /**
     * 按自定义 code/status/message 构建携带泛型数据的成功响应
     *
     * @param code    响应编码
     * @param status  响应状态
     * @param message 响应信息
     * @param data    返回数据
     * @param <T>     数据类型
     * @return 成功响应对象
     */
    public static <T> HttpResult<T> success(Integer code, String status, String message, T data) {
        return new HttpResult<T>(code, status, message).setData(data);
    }

    /**
     * 构建无数据的失败响应
     *
     * @param <T> 数据类型
     * @return 失败响应对象
     */
    public static <T> HttpResult<T> failed() {
        return new HttpResult<>(HttpEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 构建携带字符串数据的失败响应
     *
     * @param data 返回数据
     * @return 失败响应对象
     */
    public static HttpResult<String> failed(String data) {
        return new HttpResult<String>(HttpEnum.INTERNAL_SERVER_ERROR).setData(data);
    }

    /**
     * 构建携带泛型数据的失败响应
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 失败响应对象
     */
    public static <T> HttpResult<T> failed(T data) {
        return new HttpResult<T>(HttpEnum.INTERNAL_SERVER_ERROR).setData(data);
    }

    /**
     * 按响应枚举构建无数据的失败响应
     *
     * @param httpEnum 响应枚举
     * @param <T>      数据类型
     * @return 失败响应对象
     */
    public static <T> HttpResult<T> failed(HttpEnum httpEnum) {
        return new HttpResult<>(httpEnum);
    }

    /**
     * 按响应枚举构建携带字符串数据的失败响应
     *
     * @param httpEnum 响应枚举
     * @param data     返回数据
     * @return 失败响应对象
     */
    public static HttpResult<String> failed(HttpEnum httpEnum, String data) {
        return new HttpResult<String>(httpEnum).setData(data);
    }

    /**
     * 按响应枚举构建携带泛型数据的失败响应
     *
     * @param httpEnum 响应枚举
     * @param data     返回数据
     * @param <T>      数据类型
     * @return 失败响应对象
     */
    public static <T> HttpResult<T> failed(HttpEnum httpEnum, T data) {
        return new HttpResult<T>(httpEnum).setData(data);
    }

    /**
     * 按自定义 code/status/message 构建无数据的失败响应
     *
     * @param code    响应编码
     * @param status  响应状态
     * @param message 响应信息
     * @param <T>     数据类型
     * @return 失败响应对象
     */
    public static <T> HttpResult<T> failed(Integer code, String status, String message) {
        return new HttpResult<>(code, status, message);
    }

    /**
     * 按自定义 code/status/message 构建携带字符串数据的失败响应
     *
     * @param code    响应编码
     * @param status  响应状态
     * @param message 响应信息
     * @param data    返回数据
     * @return 失败响应对象
     */
    public static HttpResult<String> failed(Integer code, String status, String message, String data) {
        return new HttpResult<String>(code, status, message).setData(data);
    }

    /**
     * 按自定义 code/status/message 构建携带泛型数据的失败响应
     *
     * @param code    响应编码
     * @param status  响应状态
     * @param message 响应信息
     * @param data    返回数据
     * @param <T>     数据类型
     * @return 失败响应对象
     */
    public static <T> HttpResult<T> failed(Integer code, String status, String message, T data) {
        return new HttpResult<T>(code, status, message).setData(data);
    }
}
