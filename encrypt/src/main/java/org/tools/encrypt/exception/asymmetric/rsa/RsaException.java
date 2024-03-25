package org.tools.encrypt.exception.asymmetric.rsa;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.asymmetric.AsymmetricException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/07 23:41
 * @description rsa加密异常
 * @scope encrypt
 */
@Deprecated
public class RsaException extends AsymmetricException {

    public RsaException() {
    }

    public RsaException(String error) {
        super(error);
    }

    public RsaException(Exception exception) {
        super(exception);
    }

    public RsaException(String error, Exception exception) {
        super(error, exception);
    }

    public RsaException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
