package org.tools.encrypt.symmetrical.aes;

import junit.framework.TestCase;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.SecretKey;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
public class AesHelperTest extends TestCase {

    public void testEncode() throws AesException {

        final SecretKey secretKey = AesKeyGenerator.getSecretKey();
        AesProperties properties = new AesProperties();
        properties.setSecretKey(AesKeyGenerator.getSecretKeyStr(secretKey));
        properties.setAesKey(secretKey);
        properties.setIvParameter("1234567812345678");

        AesHelper.load(properties);

        final String encode = AesHelper.encode("1234567890");
        System.out.println("encode: " + encode);
        final String decode = AesHelper.decode(encode);
        System.out.println("decode: " + decode);
    }

    public void testDecode() {
    }
}