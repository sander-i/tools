package org.tools.encrypt.digitalsignature.dsa;

import junit.framework.TestCase;
import org.tools.encrypt.exception.EncryptException;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
public class DsaUtilsTest extends TestCase {

    public void testSign() throws EncryptException {
        final KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        final PrivateKey aPrivate = keyPair.getPrivate();
        final String sign = DsaUtils.sign("SHA256withDSA", aPrivate, "1234567890");
        System.out.println("sign: " + sign);
    }

    public void testVerify() throws EncryptException {
        final KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        final PrivateKey aPrivate = keyPair.getPrivate();
        final PublicKey aPublic = keyPair.getPublic();
        final String sign = DsaUtils.sign("SHA256withDSA", aPrivate, "1234567890");
        final Boolean verify = DsaUtils.verify("SHA256withDSA", aPublic, "1234567890", sign);
        System.out.println("verify: " + verify);
    }
}