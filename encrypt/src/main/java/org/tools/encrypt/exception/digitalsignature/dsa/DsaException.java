package org.tools.encrypt.exception.digitalsignature.dsa;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

/**
 * @author sander
 * @version 1.0
 */
public class DsaException extends DigitalSignatureException {

    private static final long serialVersionUID = -1333337529605308307L;

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
