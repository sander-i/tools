package org.tools.bedrock.http.resp.v2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.tools.bedrock.http.resp.RR;
import org.tools.bedrock.http.resp.RS;
import org.tools.bedrock.http.resp.S;

/**
 * @author sander
 * @version 2.0
 * @deprecated The current object is migrated to
 * @see org.tools.bedrock.http.resp.R
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Deprecated
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

    /** 构建无数据的成功响应（RR 视图） */
    public static RR ok() {
        return new RR();
    }

    /** 构建无数据的失败响应（RR 视图） */
    public static RR fail() {
        return new RR();
    }

    /** 构建携带数据的成功响应 */
    public static <T> R<T> ok(T data) {
        return new R<>(true, RS.OK, null, data);
    }

    /** 构建携带错误信息的失败响应 */
    public static <T> R<T> fail(String message) {
        return new R<>(false, RS.FAIL, message, null);
    }

    /** 构建携带数据的失败响应 */
    public static <T> R<T> fail(T data) {
        return new R<>(false, RS.FAIL, null, data);
    }

    /** 按状态枚举与消息构建 RR 响应 */
    public static RR build(S s, String message) {
        return new RR(true, s.getCode(), s.getMsg(), message);
    }

    /** 按状态枚举与数据构建响应 */
    public static <T> R<T> build(S s, T data) {
        return new R<T>(true, s, null, data);
    }

    /** 按状态枚举、消息与数据构建响应 */
    public static <T> R<T> build(S s, String message, T data) {
        return new R<T>(true, s, message, data);
    }

}
