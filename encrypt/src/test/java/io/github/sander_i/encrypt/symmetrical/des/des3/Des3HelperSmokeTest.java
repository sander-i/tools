package io.github.sander_i.encrypt.symmetrical.des.des3;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Des3;
import io.github.sander_i.encrypt.exception.symmetrical.des3.Des3Exception;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

/**
 * 3DES 冒烟测试
 *
 * @author sander
 */
public class Des3HelperSmokeTest extends TestCase {

    /**
     * 24 字节密钥（3DES 要求 24 字节，即 192 位含校验位）
     */
    private static final String KEY_24 = "123456789012345678901234";

    /**
     * CBC 模式要求 8 字节向量
     */
    private static final String IV_8 = "12345678";

    private Des3 newConfig() {
        Des3 des3 = new Des3();
        des3.setEnabled(true);
        des3.setSecretKey(KEY_24);
        des3.setIvParameter(IV_8);
        return des3;
    }

    /**
     * CBC 模式加解密往返
     */
    public void testCbcRoundTrip() throws Des3Exception {
        Des3Helper.load(newConfig());
        String raw = "Hello 3DES 中文测试 1234567890";
        String encoded = Des3Helper.encode(raw);
        assertNotNull(encoded);
        assertEquals(raw, Des3Helper.decode(encoded));
    }

    /**
     * ECB 模式加解密往返
     */
    public void testEcbRoundTrip() throws Des3Exception {
        Des3 des3 = newConfig();
        des3.setAlgorithmMode("DESede/ECB/PKCS5Padding");
        Des3Helper.load(des3);

        String raw = "ECB mode test 中文";
        String encoded = Des3Helper.encode(raw);
        assertEquals(raw, Des3Helper.decode(encoded));
    }

    /**
     * 同一明文两次加密，CBC 因随机 IV 不同而密文不同，但都能解回
     */
    public void testCbcProducesDifferentCipherText() throws Des3Exception {
        Des3Helper.load(newConfig());
        String raw = "same plaintext";
        String a = Des3Helper.encode(raw);
        String b = Des3Helper.encode(raw);
        assertEquals(raw, Des3Helper.decode(a));
        assertEquals(raw, Des3Helper.decode(b));
    }

    /**
     * 指定密钥加密、对应密钥解密
     */
    public void testExplicitKey() throws Des3Exception {
        Des3Helper.load(newConfig());
        String raw = "explicit key";
        String encoded = Des3Helper.encode(KEY_24, raw);
        assertEquals(raw, Des3Helper.decode(KEY_24, encoded));
    }

    /**
     * 密钥生成器产出可用密钥
     */
    public void testKeyGenerator() throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey();
        assertNotNull(key);
        assertEquals(24, key.getEncoded().length);

        String keyStr = Des3KeyGenerator.getSecretKeyStr(key);
        assertEquals(48, keyStr.length());
    }

    /**
     * 分组配置：按 name 选中对应密钥
     */
    public void testGroup() throws Des3Exception {
        Des3 des3 = newConfig();
        des3.setGroupEnabled(true);

        KeyPair group = new KeyPair();
        group.setName("email");
        group.setSecretKey("abcdefghijklmnopqrstuvwx");
        List<KeyPair> groupList = new ArrayList<>();
        groupList.add(group);
        des3.setGroup(groupList);

        Des3Helper.load(des3, "email");
        assertEquals("abcdefghijklmnopqrstuvwx", Des3Helper.getProperties().getSecretKeyStr());

        String raw = "group test";
        assertEquals(raw, Des3Helper.decode(Des3Helper.encode(raw)));
    }

    /**
     * enabled=false 时应拒绝加载
     */
    public void testDisabledRejected() {
        Des3 des3 = newConfig();
        des3.setEnabled(false);
        try {
            Des3Helper.load(des3, "any");
            fail("expected Des3Exception");
        } catch (Des3Exception expected) {
            // 预期
        }
    }

    /**
     * 密钥为空时应拒绝加载
     */
    public void testEmptyKeyRejected() {
        Des3 des3 = newConfig();
        des3.setSecretKey("");
        try {
            Des3Helper.load(des3);
            fail("expected Des3Exception");
        } catch (Des3Exception expected) {
            // 预期
        }
    }

    /**
     * 非法密文应抛异常而不是返回垃圾
     */
    public void testInvalidCipherText() throws Des3Exception {
        Des3Helper.load(newConfig());
        try {
            Des3Helper.decode("not-a-valid-base64-cipher!!");
            fail("expected SymmetricalException");
        } catch (io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException expected) {
            // 预期：非法 Base64 由 SymmetricalUtils 统一包装
        }
    }
}
