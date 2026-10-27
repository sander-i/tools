package org.tools.encrypt.exception.symmetrical.idea;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * IDEA 加解密异常。
 *
 * @author sander
 * @since 1.0.6.6
 */
public class IdeaException extends SymmetricalException {
    private static final long serialVersionUID = 322912707809015209L;

    public IdeaException() {
    }

    public IdeaException(String error) {
        super(error);
    }

    public IdeaException(Exception exception) {
        super(exception);
    }

    public IdeaException(String error, Exception exception) {
        super(error, exception);
    }
}
