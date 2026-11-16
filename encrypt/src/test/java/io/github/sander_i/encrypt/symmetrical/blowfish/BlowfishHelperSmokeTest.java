package io.github.sander_i.encrypt.symmetrical.blowfish;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Blowfish;
import io.github.sander_i.encrypt.exception.symmetrical.blowfish.BlowfishException;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Blowfish 冒烟测试
 *
 * @author sander
 */
public class BlowfishHelperSmokeTest extends TestCase {

    /**
     * 16 字节密钥。JDK 8 默认策略下 Blowfish 上限 128 位（16 字节）
     */
    private static final String KEY = "BlowfishKey12345";
    private static final String IV_8 = "12345678";

    private Blowfish newConfig() {
        Blowfish blowfish = new Blowfish();
        blowfish.setEnabled(true);
        blowfish.setSecretKey(KEY);
        blowfish.setIvParameter(IV_8);
        return blowfish;
    }

    public void testCbcRoundTrip() throws BlowfishException {
        BlowfishHelper.load(newConfig());
        String raw = "Hello Blowfish 中文测试 1234567890";
        String encoded = BlowfishHelper.encode(raw);
        assertNotNull(encoded);
        assertEquals(raw, BlowfishHelper.decode(encoded));
    }

    public void testEcbRoundTrip() throws BlowfishException {
        Blowfish blowfish = newConfig();
        blowfish.setAlgorithmMode("Blowfish/ECB/PKCS5Padding");
        BlowfishHelper.load(blowfish);

        String raw = "ECB mode test 中文";
        assertEquals(raw, BlowfishHelper.decode(BlowfishHelper.encode(raw)));
    }

    public void testExplicitKey() throws BlowfishException {
        BlowfishHelper.load(newConfig());
        String raw = "explicit key";
        String encoded = BlowfishHelper.encode(KEY, raw);
        assertEquals(raw, BlowfishHelper.decode(KEY, encoded));
    }

    public void testKeyGenerator() throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey();
        assertNotNull(key);
        assertEquals(16, key.getEncoded().length);
        assertEquals(32, BlowfishKeyGenerator.getSecretKeyStr(key).length());
    }

    public void testGroup() throws BlowfishException {
        Blowfish blowfish = newConfig();
        blowfish.setGroupEnabled(true);

        KeyPair group = new KeyPair();
        group.setName("pwd");
        group.setSecretKey("AnotherKey123456");
        List<KeyPair> groupList = new ArrayList<>();
        groupList.add(group);
        blowfish.setGroup(groupList);

        BlowfishHelper.load(blowfish, "pwd");
        assertEquals("AnotherKey123456", BlowfishHelper.getProperties().getSecretKeyStr());

        String raw = "group test 中文";
        assertEquals(raw, BlowfishHelper.decode(BlowfishHelper.encode(raw)));
    }

    public void testDisabledRejected() {
        Blowfish blowfish = newConfig();
        blowfish.setEnabled(false);
        try {
            BlowfishHelper.load(blowfish, "any");
            fail("expected BlowfishException");
        } catch (BlowfishException expected) {
            // 预期
        }
    }

    public void testEmptyKeyRejected() {
        Blowfish blowfish = newConfig();
        blowfish.setSecretKey("");
        try {
            BlowfishHelper.load(blowfish);
            fail("expected BlowfishException");
        } catch (BlowfishException expected) {
            // 预期
        }
    }

    public void testInvalidCipherText() throws BlowfishException {
        BlowfishHelper.load(newConfig());
        try {
            BlowfishHelper.decode("!!!not-base64!!!");
            fail("expected SymmetricalException");
        } catch (io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException expected) {
            // 预期
        }
    }
}
