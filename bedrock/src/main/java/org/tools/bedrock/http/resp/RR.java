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

    public static RR ok() {
        return new RR(true, RS.OK.getCode(), RS.OK.getMsg(), null);
    }

    public static RR fail() {
        return new RR(true, RS.FAIL.getCode(), RS.FAIL.getMsg(), null);
    }

    public static RR build(S s) {
        return new RR(true, s.getCode(), s.getMsg(), null);
    }

}
