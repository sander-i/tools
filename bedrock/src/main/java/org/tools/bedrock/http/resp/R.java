package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean success;
    private String code;
    private String message;
    private T data;

    private R(boolean success, String code, String message) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    private R(boolean success, String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    private R(boolean success, S s) {
        this.success = success;
        this.code = s.getCode();
        this.message = s.getMessage();
    }

    private R(boolean success, S s, T data) {
        this.success = success;
        this.code = s.getCode();
        this.message = s.getMessage();
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(true, RS.OK);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, RS.OK, data);
    }

    public static <T> R<T> ok(T data, S s) {
        return new R<>(true, s, data);
    }

    public static <T> R<T> fail() {
        return new R<>(false, RS.FAIL);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(false, RS.FAIL, data);
    }

    public static <T> R<T> fail(T data, S s) {
        return new R<>(false, s, data);
    }

}
