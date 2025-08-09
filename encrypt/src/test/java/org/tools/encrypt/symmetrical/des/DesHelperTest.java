package org.tools.encrypt.symmetrical.des;

import junit.framework.TestCase;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.exception.EncryptException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/30
 */
public class DesHelperTest extends TestCase {

    public void testEncode() throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
//        final DesProperties properties1 = new DesProperties();
//        properties1.setSecretKey("1234567890");
//        DesKeyGenerator.setProperties(properties1);

        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm(AlgorithmEnum.DES.getAlgorithm());
        keyPair.setAlgorithmMode(AlgorithmModeEnum.DES.getAlgorithmMode());
        keyPair.setIvParameter("12345678");
        keyPair.setSecretKey("12345678");

        DesHelper.load(keyPair);

        final DesProperties properties = DesHelper.getProperties();
        final String secretKey = properties.getSecretKeyStr();
        System.out.println("secretKey: " + secretKey);

        final String encode = DesHelper.encode("66666666");
        System.out.println("encode: " + encode);
        final String decode = DesHelper.decode(encode);
        System.out.println("decode: " + decode);
    }

    public void testDecode() {
    }

    public void testTestEncode() {
    }

    public void testTestEncode1() {
    }

    public void testTestDecode() {
    }

    public void testTestDecode1() {
    }
}