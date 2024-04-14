package org.tools.encrypt.exception.asymmetric;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/07
 * @description 非对称加密异常
 * @scope encrypt
 */
@Deprecated
public class AsymmetricException extends EncryptException {

    public AsymmetricException() {
    }

    public AsymmetricException(String error) {
        super(error);
    }

    public AsymmetricException(Exception exception) {
        super(exception);
    }

    public AsymmetricException(String error, Exception exception) {
        super(error, exception);
    }

    public AsymmetricException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
