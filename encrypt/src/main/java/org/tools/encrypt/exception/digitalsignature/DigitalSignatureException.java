package org.tools.encrypt.exception.digitalsignature;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:28
 * @description 数字签名算法异常
 * @scope encrypt
 */
public class DigitalSignatureException extends EncryptException {

    private static final long serialVersionUID = -5287092327897656647L;

    public DigitalSignatureException() {
    }

    public DigitalSignatureException(String error) {
        super(error);
    }

    public DigitalSignatureException(Exception exception) {
        super(exception);
    }

    public DigitalSignatureException(String error, Exception exception) {
        super(error, exception);
    }

    public DigitalSignatureException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
