package org.tools.encrypt.exception.digitalsignature.ecdsa;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

/**
 * ECDSA 数字签名异常。
 *
 * @author sander
 * @version 1.0
 * @since 2.0.1
 */
public class EcdsaException extends DigitalSignatureException {
    private static final long serialVersionUID = -1333337529605308308L;

    public EcdsaException() {
    }

    public EcdsaException(String error) {
        super(error);
    }

    public EcdsaException(Exception exception) {
        super(exception);
    }

    public EcdsaException(String error, Exception exception) {
        super(error, exception);
    }

    public EcdsaException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }
}
