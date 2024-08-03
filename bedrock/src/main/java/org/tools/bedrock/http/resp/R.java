package org.tools.bedrock.http.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Deprecated
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> R<T> ok() {
        return new R<>(true, RS.OK.getCode(), RS.OK.getMsg(), null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, RS.OK.getCode(), RS.OK.getMsg(), data);
    }

    public static <T> R<T> ok(T data, S s) {
        return new R<>(true, s.getCode(), s.getMsg(), data);
    }

    public static <T> R<T> fail() {
        return new R<>(false, RS.FAIL.getCode(), RS.FAIL.getMsg(), null);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(false, RS.FAIL.getCode(), RS.FAIL.getMsg(), data);
    }

    public static <T> R<T> fail(T data, S s) {
        return new R<>(false, s.getCode(), s.getMsg(), data);
    }

}
