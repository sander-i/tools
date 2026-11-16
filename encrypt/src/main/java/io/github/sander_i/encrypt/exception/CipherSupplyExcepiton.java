package io.github.sander_i.encrypt.exception;

import io.github.sander_i.bedrock.exception.ErrorEnum;

/**
 * @author sander
 * @version 1.0
 */
public class CipherSupplyExcepiton extends EncryptException {

    public CipherSupplyExcepiton() {
    }

    public CipherSupplyExcepiton(String error) {
        super(error);
    }

    public CipherSupplyExcepiton(Exception exception) {
        super(exception);
    }

    public CipherSupplyExcepiton(String error, Exception exception) {
        super(error, exception);
    }

    public CipherSupplyExcepiton(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
