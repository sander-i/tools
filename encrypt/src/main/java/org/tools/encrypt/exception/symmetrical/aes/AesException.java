package org.tools.encrypt.exception.symmetrical.aes;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
public class AesException extends SymmetricalException {

    private static final long serialVersionUID = 4154342030411918114L;

    public AesException() {
    }

    public AesException(String error) {
        super(error);
    }

    public AesException(Exception exception) {
        super(exception);
    }

    public AesException(String error, Exception exception) {
        super(error, exception);
    }

    public AesException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
