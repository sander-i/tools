package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author sander
 * @version 2.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class R<T> extends RR {

    private static final long serialVersionUID = -3650942902842307882L;

    private T data;

    private R(boolean success, String code, String msg, String message, T data) {
        super(success, code, msg, message);
        this.data = data;
    }

    private R(boolean success, S s, String message, T data) {
        super(success, s.getCode(), s.getMsg(), message);
        this.data = data;
    }

    public static RR ok() {
        return new RR();
    }

    public static RR fail() {
        return new RR();
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, RS.OK, null, data);
    }

    public static <T> R<T> fail(String message) {
        return new R<>(false, RS.FAIL, message, null);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(false, RS.FAIL, null, data);
    }

    public static RR build(S s, String message) {
        return new RR(true, s.getCode(), s.getMsg(), message);
    }

    public static <T> R<T> build(S s, T data) {
        return new R<>(true, s, null, data);
    }

    public static <T> R<T> build(S s, String message, T data) {
        return new R<>(true, s, message, data);
    }

}
