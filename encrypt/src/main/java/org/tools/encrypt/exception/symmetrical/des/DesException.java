package org.tools.encrypt.exception.symmetrical.des;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/06 23:26
 * @description des加密异常
 * @scope encrypt
 */
@Deprecated
public class DesException extends SymmetricalException {

    public DesException() {
    }

    public DesException(String error) {
        super(error);
    }

    public DesException(Exception exception) {
        super(exception);
    }

    public DesException(String error, Exception exception) {
        super(error, exception);
    }

    public DesException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
