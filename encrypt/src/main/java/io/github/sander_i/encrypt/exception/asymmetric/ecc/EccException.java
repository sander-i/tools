package io.github.sander_i.encrypt.exception.asymmetric.ecc;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.asymmetric.AsymmetricException;

/**
 * ECC 椭圆曲线异常
 *
 * @author sander
 * @version 1.0
 */
public class EccException extends AsymmetricException {

    private static final long serialVersionUID = -2640183957216038417L;

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

    public EccException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
