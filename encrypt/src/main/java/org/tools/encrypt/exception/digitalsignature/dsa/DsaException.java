package org.tools.encrypt.exception.digitalsignature.dsa;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.digitalsignature.DigitalsignatureException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:27
 * @description dsa签名算法异常
 * @scope encrypt
 */
public class DsaException extends DigitalsignatureException {

    public DsaException() {
    }

    public DsaException(String error) {
        super(error);
    }

    public DsaException(Exception exception) {
        super(exception);
    }

    public DsaException(String error, Exception exception) {
        super(error, exception);
    }

    public DsaException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
