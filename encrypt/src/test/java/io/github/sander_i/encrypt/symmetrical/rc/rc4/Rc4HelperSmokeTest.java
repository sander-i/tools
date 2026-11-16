package io.github.sander_i.encrypt.symmetrical.rc.rc4;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Rc4;
import io.github.sander_i.encrypt.exception.symmetrical.rc4.Rc4Exception;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

/**
 * RC4 冒烟测试
 *
 * @author sander
 */
public class Rc4HelperSmokeTest extends TestCase {

    private static final String KEY = "Rc4SecretKey1234";

    private Rc4 newConfig() {
        Rc4 rc4 = new Rc4();
        rc4.setEnabled(true);
        rc4.setSecretKey(KEY);
        return rc4;
    }

    public void testRoundTrip() throws Rc4Exception {
        Rc4Helper.load(newConfig());
        String raw = "Hello RC4 中文测试 1234567890";
        String encoded = Rc4Helper.encode(raw);
        assertNotNull(encoded);
        assertEquals(raw, Rc4Helper.decode(encoded));
    }

    public void testExplicitKey() throws Rc4Exception {
        Rc4Helper.load(newConfig());
        String raw = "explicit key";
        String encoded = Rc4Helper.encode(KEY, raw);
        assertEquals(raw, Rc4Helper.decode(KEY, encoded));
    }

    /**
     * 流加密同一密钥同一明文应产出相同密文（无随机 IV）
     */
    public void testDeterministic() throws Rc4Exception {
        Rc4Helper.load(newConfig());
        String raw = "deterministic";
        assertEquals(Rc4Helper.encode(raw), Rc4Helper.encode(raw));
    }

    /**
     * 不同明文产出不同密文
     */
    public void testDifferentPlaintext() throws Rc4Exception {
        Rc4Helper.load(newConfig());
        assertFalse(Rc4Helper.encode("aaa").equals(Rc4Helper.encode("bbb")));
    }

    /**
     * 密文长度与明文长度关系：RC4 不填充，Base64 后长度可预期
     */
    public void testNoPadding() throws Rc4Exception {
        Rc4Helper.load(newConfig());
        // "1234567890" 10 字节 -> Base64 后 16 字符
        assertEquals(16, Rc4Helper.encode("1234567890").length());
    }

    public void testKeyGenerator() throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey();
        assertNotNull(key);
        assertEquals(16, key.getEncoded().length);
        assertEquals(32, Rc4KeyGenerator.getSecretKeyStr(key).length());
    }

    public void testGroup() throws Rc4Exception {
        Rc4 rc4 = newConfig();
        rc4.setGroupEnabled(true);

        KeyPair group = new KeyPair();
        group.setName("pwd");
        group.setSecretKey("Rc4GroupKey12345");
        List<KeyPair> groupList = new ArrayList<>();
        groupList.add(group);
        rc4.setGroup(groupList);

        Rc4Helper.load(rc4, "pwd");
        assertEquals("Rc4GroupKey12345", Rc4Helper.getProperties().getSecretKeyStr());

        String raw = "group test 中文";
        assertEquals(raw, Rc4Helper.decode(Rc4Helper.encode(raw)));
    }

    public void testDisabledRejected() {
        Rc4 rc4 = newConfig();
        rc4.setEnabled(false);
        try {
            Rc4Helper.load(rc4, "any");
            fail("expected Rc4Exception");
        } catch (Rc4Exception expected) {
            // 预期
        }
    }

    public void testEmptyKeyRejected() {
        Rc4 rc4 = newConfig();
        rc4.setSecretKey("");
        try {
            Rc4Helper.load(rc4);
            fail("expected Rc4Exception");
        } catch (Rc4Exception expected) {
            // 预期
        }
    }

    public void testInvalidCipherText() throws Rc4Exception {
        Rc4Helper.load(newConfig());
        try {
            Rc4Helper.decode("!!!not-base64!!!");
            fail("expected SymmetricalException");
        } catch (io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException expected) {
            // 预期
        }
    }
}
