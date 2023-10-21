package org.tools.bedrock.http.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tools.bedrock.annotations.Inherit;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/21 23:01
 * @description response result
 * @scope response
 */
@Inherit
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RR {

    private int code;
    private String msg;

    public static RR success() {
        return new RR(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg());
    }

    public static RR success(int code, String msg) {
        return new RR(code, msg);
    }

    public static RR success(RE re) {
        return new RR(re.getCode(), re.getMsg());
    }

    public static RR fail() {
        return new RR(RE.FAIL.getCode(), RE.FAIL.getMsg());
    }

    public static RR fail(int code, String msg) {
        return new RR(code, msg);
    }

    public static RR fail(RE re) {
        return new RR(re.getCode(), re.getMsg());
    }

}
