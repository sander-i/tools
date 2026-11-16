package io.github.sander_i.encrypt.asymmetric.ecc;

import junit.framework.TestCase;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * ECC / ECDH 冒烟测试
 *
 * @author sander
 */
public class EccKeyGeneratorSmokeTest extends TestCase {

    /**
     * 密钥对生成与字符串往返
     */
    public void testKeyPairRoundTrip() throws Exception {
        KeyPair keyPair = EccKeyGenerator.getKeyPair();
        assertNotNull(keyPair);

        String pub = EccKeyGenerator.getPublicKeyStr(keyPair.getPublic());
        String pri = EccKeyGenerator.getPrivateKeyStr(keyPair.getPrivate());

        assertNotNull(pub);
        assertNotNull(pri);

        // 字符串还原回对象
        PublicKey restored = EccKeyGenerator.getPublicKey(pub);
        assertNotNull(restored);
        assertEquals(pub, EccKeyGenerator.getPublicKeyStr(restored));

        assertNotNull(EccKeyGenerator.getPrivateKey(pri));
    }

    /**
     * 不同曲线均可生成
     */
    public void testMultipleCurves() throws Exception {
        for (String curve : new String[]{"secp256r1", "secp384r1", "secp521r1"}) {
            KeyPair keyPair = EccKeyGenerator.getKeyPair(curve);
            assertNotNull("curve " + curve, keyPair);
        }
    }

    /**
     * ECDH 双方协商出相同共享密钥
     */
    public void testEcdhSharedSecret() throws Exception {
        KeyPair alice = EccKeyGenerator.getKeyPair();
        KeyPair bob = EccKeyGenerator.getKeyPair();

        byte[] aliceSecret = EcdhHelper.generateSecret(alice.getPrivate(), bob.getPublic());
        byte[] bobSecret = EcdhHelper.generateSecret(bob.getPrivate(), alice.getPublic());

        assertNotNull(aliceSecret);
        assertTrue(aliceSecret.length > 0);
        assertTrue("双方共享密钥应一致", Arrays.equals(aliceSecret, bobSecret));
    }

    /**
     * 字符串形式的 ECDH 协商
     */
    public void testEcdhSharedSecretStr() throws Exception {
        KeyPair alice = EccKeyGenerator.getKeyPair();
        KeyPair bob = EccKeyGenerator.getKeyPair();

        String aliceStr = EcdhHelper.generateSecretStr(
                EccKeyGenerator.getPrivateKeyStr(alice.getPrivate()),
                EccKeyGenerator.getPublicKeyStr(bob.getPublic()));
        String bobStr = EcdhHelper.generateSecretStr(
                EccKeyGenerator.getPrivateKeyStr(bob.getPrivate()),
                EccKeyGenerator.getPublicKeyStr(alice.getPublic()));

        assertEquals(aliceStr, bobStr);
    }

    /**
     * 协商出的密钥可直接用于 AES
     */
    public void testEcdhToAesKey() throws Exception {
        KeyPair alice = EccKeyGenerator.getKeyPair();
        KeyPair bob = EccKeyGenerator.getKeyPair();

        javax.crypto.SecretKey aliceKey = EcdhHelper.generateSecretKey(
                alice.getPrivate(), bob.getPublic(), "AES", 16);
        javax.crypto.SecretKey bobKey = EcdhHelper.generateSecretKey(
                bob.getPrivate(), alice.getPublic(), "AES", 16);

        assertNotNull(aliceKey);
        assertEquals(16, aliceKey.getEncoded().length);
        assertTrue(Arrays.equals(aliceKey.getEncoded(), bobKey.getEncoded()));
    }

    /**
     * 不同密钥对协商结果不同
     */
    public void testDifferentPairsDifferentSecret() throws Exception {
        KeyPair alice = EccKeyGenerator.getKeyPair();
        KeyPair bob = EccKeyGenerator.getKeyPair();
        KeyPair eve = EccKeyGenerator.getKeyPair();

        byte[] ab = EcdhHelper.generateSecret(alice.getPrivate(), bob.getPublic());
        byte[] ae = EcdhHelper.generateSecret(alice.getPrivate(), eve.getPublic());

        assertFalse("与不同对象协商应得到不同密钥", Arrays.equals(ab, ae));
    }

    /**
     * 空参数应拒绝
     */
    public void testNullArgsRejected() {
        try {
            EcdhHelper.generateSecret(null, null);
            fail("expected EccException");
        } catch (Exception expected) {
            // 预期
        }
    }

    /**
     * 非法 Base64 应抛异常
     */
    public void testInvalidBase64() {
        try {
            EccKeyGenerator.getPublicKey("!!!not-base64!!!");
            fail("expected EccException");
        } catch (Exception expected) {
            // 预期
        }
    }
}
