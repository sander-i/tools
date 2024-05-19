package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class R<T> extends RR {

    private T data;

    public R(int code, String msg) {
        super(code, msg);
    }

    public R(int code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public static RR success() {
        return new R<>(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg());
    }

    public static RR success(int code, String msg) {
        return new R<>(code, msg);
    }

    public static RR success(RE re) {
        return new R<>(re.getCode(), re.getMsg());
    }

    public static <T> R<T> success(T data) {
        return new R<>(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg(), data);
    }

    public static <T> R<T> success(int code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    public static <T> R<T> success(RE re, T data) {
        return new R<>(re.getCode(), re.getMsg(), data);
    }

    public static RR fail() {
        return new R<>(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg());
    }

    public static RR fail(int code, String msg) {
        return new R<>(code, msg);
    }

    public static RR fail(RE re) {
        return new R<>(re.getCode(), re.getMsg());
    }

    public static <T> R<T> fail(T data) {
        return new R<>(RE.FAIL.getCode(), RE.FAIL.getMsg(), data);
    }

    public static <T> R<T> fail(int code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    public static <T> R<T> fail(RE re, T data) {
        return new R<>(re.getCode(), re.getMsg(), data);
    }

}
