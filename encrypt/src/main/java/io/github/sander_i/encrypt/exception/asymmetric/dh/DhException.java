package io.github.sander_i.encrypt.exception.asymmetric.dh;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.asymmetric.AsymmetricException;

/**
 * DH（Diffie-Hellman）密钥交换异常
 *
 * @author sander
 * @version 1.0
 */
public class DhException extends AsymmetricException {

    private static final long serialVersionUID = 3057192648130572843L;

    public DhException() {
    }

    public DhException(String error) {
        super(error);
    }

    public DhException(Exception exception) {
        super(exception);
    }

    public DhException(String error, Exception exception) {
        super(error, exception);
    }

    public DhException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
