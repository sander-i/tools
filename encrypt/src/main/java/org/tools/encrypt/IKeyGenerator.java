package org.tools.encrypt;

import org.tools.encrypt.exception.EncryptException;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/30
 */
public abstract class IKeyGenerator<P> {

    public abstract P getProperties() throws EncryptException;

    public abstract void setProperties(P properties) throws EncryptException;

}
