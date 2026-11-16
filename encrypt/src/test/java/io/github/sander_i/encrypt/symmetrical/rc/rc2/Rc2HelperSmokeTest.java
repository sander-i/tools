package io.github.sander_i.encrypt.symmetrical.rc.rc2;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.SecretKeyCodec;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Rc2;
import io.github.sander_i.encrypt.exception.symmetrical.rc2.Rc2Exception;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

/**
 * RC2 冒烟测试
 *
 * @author sander
 */
public class Rc2HelperSmokeTest extends TestCase {

    /**
     * 16 字节密钥
     */
    private static final String KEY = "Rc2SecretKey1234";
    private static final String IV_8 = "12345678";

    private Rc2 newConfig() {
        Rc2 rc2 = new Rc2();
        rc2.setEnabled(true);
        rc2.setSecretKey(KEY);
        rc2.setIvParameter(IV_8);
        return rc2;
    }

    public void testCbcRoundTrip() throws Rc2Exception {
        Rc2Helper.load(newConfig());
        String raw = "Hello RC2 中文测试 1234567890";
        String encoded = Rc2Helper.encode(raw);
        assertNotNull(encoded);
        assertEquals(raw, Rc2Helper.decode(encoded));
    }

    public void testEcbRoundTrip() throws Rc2Exception {
        Rc2 rc2 = newConfig();
        rc2.setAlgorithmMode("RC2/ECB/PKCS5Padding");
        Rc2Helper.load(rc2);

        String raw = "ECB mode test 中文";
        assertEquals(raw, Rc2Helper.decode(Rc2Helper.encode(raw)));
    }

    public void testExplicitKey() throws Rc2Exception {
        Rc2Helper.load(newConfig());
        String raw = "explicit key";
        String encoded = Rc2Helper.encode(KEY, raw);
        assertEquals(raw, Rc2Helper.decode(KEY, encoded));
    }

    public void testKeyGenerator() throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey();
        assertNotNull(key);
        assertEquals(16, key.getEncoded().length);
        assertEquals(SecretKeyCodec.HEX_PREFIX.length() + 32, Rc2KeyGenerator.getSecretKeyStr(key).length());
    }

    public void testGroup() throws Rc2Exception {
        Rc2 rc2 = newConfig();
        rc2.setGroupEnabled(true);

        KeyPair group = new KeyPair();
        group.setName("pwd");
        group.setSecretKey("Rc2GroupKey12345");
        List<KeyPair> groupList = new ArrayList<>();
        groupList.add(group);
        rc2.setGroup(groupList);

        Rc2Helper.load(rc2, "pwd");
        assertEquals("Rc2GroupKey12345", Rc2Helper.getProperties().getSecretKeyStr());

        String raw = "group test 中文";
        assertEquals(raw, Rc2Helper.decode(Rc2Helper.encode(raw)));
    }

    public void testDisabledRejected() {
        Rc2 rc2 = newConfig();
        rc2.setEnabled(false);
        try {
            Rc2Helper.load(rc2, "any");
            fail("expected Rc2Exception");
        } catch (Rc2Exception expected) {
            // 预期
        }
    }

    public void testEmptyKeyRejected() {
        Rc2 rc2 = newConfig();
        rc2.setSecretKey("");
        try {
            Rc2Helper.load(rc2);
            fail("expected Rc2Exception");
        } catch (Rc2Exception expected) {
            // 预期
        }
    }

    public void testInvalidCipherText() throws Rc2Exception {
        Rc2Helper.load(newConfig());
        try {
            Rc2Helper.decode("!!!not-base64!!!");
            fail("expected SymmetricalException");
        } catch (io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException expected) {
            // 预期
        }
    }
}
