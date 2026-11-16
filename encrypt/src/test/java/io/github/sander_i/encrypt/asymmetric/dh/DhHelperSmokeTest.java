package io.github.sander_i.encrypt.asymmetric.dh;

import junit.framework.TestCase;

import java.security.KeyPair;
import java.util.Arrays;

/**
 * DH 冒烟测试
 *
 * @author sander
 */
public class DhHelperSmokeTest extends TestCase {

    /**
     * 双方协商出相同共享密钥
     */
    public void testSharedSecret() throws Exception {
        KeyPair alice = DhHelper.generateKeyPair();
        KeyPair bob = DhHelper.generateKeyPair(alice.getPublic());

        byte[] a = DhHelper.generateSecret(alice.getPrivate(), bob.getPublic());
        byte[] b = DhHelper.generateSecret(bob.getPrivate(), alice.getPublic());

        assertNotNull(a);
        assertTrue(a.length > 0);
        assertTrue("双方共享密钥应一致", Arrays.equals(a, b));
    }

    /**
     * 字符串形式的协商
     */
    public void testSharedSecretStr() throws Exception {
        KeyPair alice = DhHelper.generateKeyPair();
        KeyPair bob = DhHelper.generateKeyPair(alice.getPublic());

        String a = DhHelper.generateSecretStr(
                DhHelper.getPrivateKeyStr(alice.getPrivate()),
                DhHelper.getPublicKeyStr(bob.getPublic()));
        String b = DhHelper.generateSecretStr(
                DhHelper.getPrivateKeyStr(bob.getPrivate()),
                DhHelper.getPublicKeyStr(alice.getPublic()));

        assertEquals(a, b);
    }

    /**
     * 协商结果可直接作为 AES 密钥
     */
    public void testSecretAsAesKey() throws Exception {
        KeyPair alice = DhHelper.generateKeyPair();
        KeyPair bob = DhHelper.generateKeyPair(alice.getPublic());

        javax.crypto.SecretKey ak = DhHelper.generateSecretKey(alice.getPrivate(), bob.getPublic(), "AES", 16);
        javax.crypto.SecretKey bk = DhHelper.generateSecretKey(bob.getPrivate(), alice.getPublic(), "AES", 16);

        assertEquals(16, ak.getEncoded().length);
        assertTrue(Arrays.equals(ak.getEncoded(), bk.getEncoded()));
    }

    /**
     * 公钥/私钥字符串往返
     */
    public void testKeyRoundTrip() throws Exception {
        KeyPair kp = DhHelper.generateKeyPair();
        String pub = DhHelper.getPublicKeyStr(kp.getPublic());
        String pri = DhHelper.getPrivateKeyStr(kp.getPrivate());

        assertNotNull(DhHelper.getPublicKey(pub));
        assertNotNull(DhHelper.getPrivateKey(pri));
    }

    /**
     * 不同密钥对协商出不同密钥
     */
    public void testDifferentPairs() throws Exception {
        KeyPair alice = DhHelper.generateKeyPair();
        KeyPair bob = DhHelper.generateKeyPair(alice.getPublic());
        KeyPair eve = DhHelper.generateKeyPair(alice.getPublic());

        byte[] ab = DhHelper.generateSecret(alice.getPrivate(), bob.getPublic());
        byte[] ae = DhHelper.generateSecret(alice.getPrivate(), eve.getPublic());

        assertFalse(Arrays.equals(ab, ae));
    }

    /**
     * 空参数拒绝
     */
    public void testNullRejected() {
        try {
            DhHelper.generateSecret(null, null);
            fail("expected DhException");
        } catch (Exception expected) {
            // 预期
        }
    }

    /**
     * 非法 Base64 拒绝
     */
    public void testInvalidBase64() {
        try {
            DhHelper.getPublicKey("!!!not-base64!!!");
            fail("expected DhException");
        } catch (Exception expected) {
            // 预期
        }
    }

    /**
     * 非 DH 公钥类型拒绝
     */
    public void testWrongKeyTypeRejected() throws Exception {
        java.security.KeyPairGenerator g = java.security.KeyPairGenerator.getInstance("RSA");
        g.initialize(1024);
        java.security.PublicKey rsaPub = g.generateKeyPair().getPublic();
        try {
            DhHelper.generateKeyPair(rsaPub);
            fail("expected DhException");
        } catch (Exception expected) {
            // 预期
        }
    }
}
