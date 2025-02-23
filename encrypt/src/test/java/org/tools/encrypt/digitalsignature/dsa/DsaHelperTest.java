package org.tools.encrypt.digitalsignature.dsa;

import junit.framework.TestCase;
import org.tools.encrypt.entity.digitalsignature.DsaProperties;
import org.tools.encrypt.enums.SignAlgorithmEnum;
import org.tools.encrypt.exception.EncryptException;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
public class DsaHelperTest extends TestCase {

    public void testSign() throws EncryptException, NoSuchAlgorithmException {
        final KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        final PrivateKey aPrivate = keyPair.getPrivate();
        final PublicKey aPublic = keyPair.getPublic();
        DsaProperties properties = new DsaProperties();
        properties.setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());
        properties.setPrivateKey(aPrivate);
        properties.setPublicKey(aPublic);
        DsaHelper.load(properties);
        final String sign = DsaHelper.sign(aPrivate, "1234567890");
        System.out.println("sign: " + sign);
    }

    public void testVerify() throws EncryptException, NoSuchAlgorithmException {
        final KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        final PrivateKey aPrivate = keyPair.getPrivate();
        final PublicKey aPublic = keyPair.getPublic();
        DsaProperties properties = new DsaProperties();
        properties.setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());
        properties.setPrivateKey(aPrivate);
        properties.setPublicKey(aPublic);
        DsaHelper.load(properties);
        final String sign = DsaHelper.sign(aPrivate, "1234567890");
        System.out.println("sign: " + sign);
        final boolean verify = DsaHelper.verify(aPublic, "1234567890", sign);
        System.out.println("verify: " + verify);
    }
}