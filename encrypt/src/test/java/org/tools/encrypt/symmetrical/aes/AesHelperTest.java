package org.tools.encrypt.symmetrical.aes;

import junit.framework.TestCase;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Aes;
import org.tools.encrypt.entity.symmetrical.AesProperties;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
public class AesHelperTest extends TestCase {

    public void testEncode() throws AesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {

        final SecretKey secretKey = AesKeyGenerator.getSecretKey();
//        AesProperties properties = new AesProperties();
//        properties.setSecretKey(AesKeyGenerator.getSecretKeyStr(secretKey));
//        properties.setAesKey(secretKey);
//        properties.setIvParameter("1234567812345678");
//
//        AesHelper.load(properties);
//
//        final String encode = AesHelper.encode("1234567890");
//        System.out.println("encode: " + encode);
//        final String decode = AesHelper.decode(encode);
//        System.out.println("decode: " + decode);

        Aes aes = new Aes();
        aes.setEnabled(true);
        aes.setGroupEnabled(true);
        aes.setSecretKey(AesKeyGenerator.getSecretKeyStr(secretKey));
        aes.setIvParameter("1234567812345678");

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair keyPair = new KeyPair();
        keyPair.setName("email");
        keyPair.setSecretKey(AesKeyGenerator.getSecretKeyStr(secretKey));
        keyPair.setIvParameter("8765432187654321");
        groupList.add(keyPair);
        aes.setGroup(groupList);

        System.out.println(aes);

        AesHelper.load(aes, "email");

        final AesProperties properties = AesHelper.getProperties();
        System.out.println(properties);

        final String encode = AesHelper.encode("1234567890");
        System.out.println("encode: " + encode);
        final String decode = AesHelper.decode(encode);
        System.out.println("decode: " + decode);

    }

    public void testDecode() {
    }
}