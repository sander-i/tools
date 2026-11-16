package io.github.sander_i.encrypt.exception;


import io.github.sander_i.bedrock.exception.ErrorEnum;

/**
 * @author sander
 **/
public class CipherStrategyException extends EncryptException {

    private static final long serialVersionUID = 2433406435391534274L;

    public CipherStrategyException() {
    }

    public CipherStrategyException(String error) {
        super(error);
    }

    public CipherStrategyException(Exception exception) {
        super(exception);
    }

    public CipherStrategyException(String error, Exception exception) {
        super(error, exception);
    }

    public CipherStrategyException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
