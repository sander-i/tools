package org.tools.encrypt.exception.asymmetric.ecc;

import org.tools.encrypt.exception.asymmetric.AsymmetricException;

/**
 * ECC 椭圆曲线密钥协商异常。
 *
 * @author sander
 * @version 1.0
 * @since 2.0.1
 */
public class EccException extends AsymmetricException {
    private static final long serialVersionUID = 6938172739695073405L;

    public EccException() {
    }

    public EccException(String error) {
        super(error);
    }

    public EccException(Exception exception) {
        super(exception);
    }

    public EccException(String error, Exception exception) {
        super(error, exception);
    }
}
