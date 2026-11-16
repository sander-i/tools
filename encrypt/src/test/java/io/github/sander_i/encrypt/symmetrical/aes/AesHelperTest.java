package io.github.sander_i.encrypt.symmetrical.aes;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Aes;
import io.github.sander_i.encrypt.entity.symmetrical.AesProperties;
import io.github.sander_i.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * AES 加解密测试
 *
 * @author sander 2024/04/14
 */
public class AesHelperTest extends TestCase {

    private static final String IV16 = "1234567812345678";
    private static final String PLAIN = "AES 中文测试 1234567890";

    public void testEncode() throws AesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKey secretKey = AesKeyGenerator.getSecretKey();

        Aes aes = new Aes();
        aes.setEnabled(true);
        aes.setGroupEnabled(true);
        aes.setSecretKey(AesKeyGenerator.getSecretKeyStr(secretKey));
        aes.setIvParameter(IV16);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair keyPair = new KeyPair();
        keyPair.setName("email");
        keyPair.setSecretKey(AesKeyGenerator.getSecretKeyStr(secretKey));
        keyPair.setIvParameter("8765432187654321");
        groupList.add(keyPair);
        aes.setGroup(groupList);

        AesHelper.load(aes, "email");

        AesProperties properties = AesHelper.getProperties();
        assertNotNull(properties);
        assertEquals("分组配置应选中 email 的向量", "8765432187654321", properties.getIvParameter());
        assertEquals(PLAIN, AesHelper.decode(AesHelper.encode(PLAIN)));
    }

    public void testDecode() throws Exception {
        Aes aes = new Aes();
        aes.setEnabled(true);
        aes.setSecretKey(AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey()));
        aes.setIvParameter(IV16);
        AesHelper.load(aes);

        String encoded = AesHelper.encode(PLAIN);
        assertNotNull(encoded);
        assertEquals(PLAIN, AesHelper.decode(encoded));
    }

    /**
     * 回归：分组未配置 vector 时应继承父级，否则 CBC 初始化会 NPE
     */
    public void testGroupInheritsIv() throws Exception {
        Aes aes = new Aes();
        aes.setEnabled(true);
        aes.setGroupEnabled(true);
        aes.setSecretKey(AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey()));
        aes.setIvParameter(IV16);

        KeyPair group = new KeyPair();
        group.setName("no-iv");
        group.setSecretKey(AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey()));
        // 刻意不设置 ivParameter
        List<KeyPair> groupList = new ArrayList<>();
        groupList.add(group);
        aes.setGroup(groupList);

        AesHelper.load(aes, "no-iv");
        assertEquals("应继承父级向量", IV16, AesHelper.getProperties().getIvParameter());
        assertEquals(PLAIN, AesHelper.decode(AesHelper.encode(PLAIN)));
    }

    public void testDisabledRejected() throws Exception {
        Aes aes = new Aes();
        aes.setEnabled(false);
        aes.setSecretKey(AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey()));
        try {
            AesHelper.load(aes, "any");
            fail("expected AesException");
        } catch (AesException expected) {
            // 预期
        }
    }

    /**
     * 密钥字符串往返后仍可用于加解密
     */
    public void testKeyRoundTripUsable() throws Exception {
        SecretKey original = AesKeyGenerator.getSecretKey();
        String encoded = AesKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = AesKeyGenerator.getSecretKey("AES", encoded);

        assertEquals("往返后密钥字节应一致",
                java.util.Arrays.toString(original.getEncoded()),
                java.util.Arrays.toString(restored.getEncoded()));

        Aes aes = new Aes();
        aes.setEnabled(true);
        aes.setSecretKey(encoded);
        aes.setIvParameter(IV16);
        AesHelper.load(aes);
        assertEquals(PLAIN, AesHelper.decode(AesHelper.encode(PLAIN)));
    }
}
