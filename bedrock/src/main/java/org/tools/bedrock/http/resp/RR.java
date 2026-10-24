package org.tools.bedrock.http.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tools.bedrock.annotations.Inherit;

import java.io.Serializable;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/08/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inherit
public class RR implements Serializable {
    private static final long serialVersionUID = 2567760980002554897L;

    private boolean success;
    private String code;
    private String msg;
    private String message;

    /** 构建无数据的成功响应 */
    public static RR ok() {
        return new RR(true, RS.OK.getCode(), RS.OK.getMsg(), null);
    }

    /** 构建无数据的失败响应（success=false） */
    public static RR fail() {
        return new RR(false, RS.FAIL.getCode(), RS.FAIL.getMsg(), null);
    }

    /**
     * 按状态枚举构建 RR 响应：code 匹配 {@link RS#OK} 时 success=true，否则 false。
     *
     * @param s 状态定义
     * @return RR 响应
     */
    public static RR build(S s) {
        return build(s, RS.OK.getCode().equals(s.getCode()));
    }

    /**
     * 按状态枚举与显式的 success 标记构建 RR 响应。
     *
     * @param s       状态定义
     * @param success 是否成功
     * @return RR 响应
     */
    public static RR build(S s, boolean success) {
        return new RR(success, s.getCode(), s.getMsg(), null);
    }

}
