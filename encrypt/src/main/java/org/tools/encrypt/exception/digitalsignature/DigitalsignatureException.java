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
public class DigitalsignatureException extends EncryptException {

    public DigitalsignatureException() {
    }

    public DigitalsignatureException(String error) {
        super(error);
    }

    public DigitalsignatureException(Exception exception) {
        super(exception);
    }

    public DigitalsignatureException(String error, Exception exception) {
        super(error, exception);
    }

    public DigitalsignatureException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
