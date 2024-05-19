package org.tools.encrypt.exception.symmetrical.des;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * @author sander
 * @version 1.0
 */
public class DesException extends SymmetricalException {

    private static final long serialVersionUID = -658506542807550203L;

    public DesException() {
    }

    public DesException(String error) {
        super(error);
    }

    public DesException(Exception exception) {
        super(exception);
    }

    public DesException(String error, Exception exception) {
        super(error, exception);
    }

    public DesException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
