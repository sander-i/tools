package org.tools.encrypt.asymmetric.ecc;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.asymmetric.Ecc;
import org.tools.encrypt.entity.asymmetric.EccProperties;
import org.tools.encrypt.exception.asymmetric.ecc.EccException;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * EccHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / generateKeyPair×2 / generateSharedSecret×1 /
 * generateSharedSecretStr×2 / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class EccHelperTest {
    @Test
    public void testGetPropertiesDefaults() {
        EccProperties properties = EccHelper.getProperties();
        Assert.assertEquals("EC", properties.getAlgorithm());
        Assert.assertEquals(256, properties.getKeyBits());
        Assert.assertEquals("secp256r1", properties.getCurve());
    }

    @Test
    public void testGenerateKeyPairDefaultCurve() throws Exception {
        KeyPair keyPair = EccHelper.generateKeyPair();
        Assert.assertEquals("EC", keyPair.getPublic().getAlgorithm());
        Assert.assertEquals("EC", keyPair.getPrivate().getAlgorithm());
    }

    @Test
    public void testGenerateKeyPairWithCurve() throws Exception {
        KeyPair keyPair = EccHelper.generateKeyPair("secp384r1");
        KeyPair peer = EccHelper.generateKeyPair("secp384r1");
        byte[] secret = EccHelper.generateSharedSecret(keyPair.getPrivate(), peer.getPublic());
        // P-384 共享秘密为 48 字节
        Assert.assertEquals(48, secret.length);
    }

    @Test
    public void testGenerateKeyPairWithUnknownCurve() {
        try {
            EccHelper.generateKeyPair("no-such-curve");
            Assert.fail("未知曲线应抛 EccException");
        } catch (EccException e) {
            Assert.assertNotNull(e.getCause());
        }
    }

    @Test
    public void testGenerateSharedSecretWithKeyObjects() throws Exception {
        KeyPair a = EccKeyGenerator.generateKeyPair();
        KeyPair b = EccKeyGenerator.generateKeyPair();
        byte[] secretA = EccHelper.generateSharedSecret(a.getPrivate(), b.getPublic());
        byte[] secretB = EccHelper.generateSharedSecret(b.getPrivate(), a.getPublic());
        // P-256 共享秘密为 32 字节，双向协商结果一致
        Assert.assertEquals(32, secretA.length);
        Assert.assertArrayEquals(secretA, secretB);
    }

    @Test
    public void testGenerateSharedSecretStrWithKeyObjects() throws Exception {
        KeyPair a = EccKeyGenerator.generateKeyPair();
        KeyPair b = EccKeyGenerator.generateKeyPair();
        String secretA = EccHelper.generateSharedSecretStr(a.getPrivate(), b.getPublic());
        String secretB = EccHelper.generateSharedSecretStr(b.getPrivate(), a.getPublic());
        Assert.assertEquals(secretA, secretB);
        Assert.assertEquals(32, Base64.decodeBase64(secretA).length);
    }

    @Test
    public void testGenerateSharedSecretStrWithStringKeys() throws Exception {
        KeyPair a = EccKeyGenerator.generateKeyPair();
        KeyPair b = EccKeyGenerator.generateKeyPair();
        String aPrivate = EccKeyGenerator.getPrivateKeyStr(a.getPrivate());
        String aPublic = EccKeyGenerator.getPublicKeyStr(a.getPublic());
        String bPrivate = EccKeyGenerator.getPrivateKeyStr(b.getPrivate());
        String bPublic = EccKeyGenerator.getPublicKeyStr(b.getPublic());
        String fromA = EccHelper.generateSharedSecretStr(aPrivate, bPublic);
        String fromB = EccHelper.generateSharedSecretStr(bPrivate, aPublic);
        Assert.assertEquals(fromA, fromB);
        // 字符串路径与对象路径结果一致
        Assert.assertEquals(EccHelper.generateSharedSecretStr(a.getPrivate(), b.getPublic()), fromA);
    }

    @Test
    public void testGenerateSharedSecretWithDifferentCurves() throws Exception {
        KeyPair p256 = EccHelper.generateKeyPair("secp256r1");
        KeyPair p384 = EccHelper.generateKeyPair("secp384r1");
        try {
            EccHelper.generateSharedSecret(p256.getPrivate(), p384.getPublic());
            Assert.fail("曲线不一致应抛 EccException");
        } catch (EccException e) {
            // JDK 8 SunEC 抛 IllegalStateException，JDK 17 抛 InvalidKeyException，均被包装
            Assert.assertNotNull(e.getCause());
        }
    }

    @Test
    public void testLoadKeyPair() throws Exception {
        KeyPair pair = EccKeyGenerator.generateKeyPair();
        Ecc ecc = new Ecc();
        ecc.setPublicKey(EccKeyGenerator.getPublicKeyStr(pair.getPublic()));
        ecc.setPrivateKey(EccKeyGenerator.getPrivateKeyStr(pair.getPrivate()));
        EccHelper.load(ecc);
        Assert.assertEquals("EC", EccHelper.getProperties().getAlgorithm());
        Assert.assertEquals("secp256r1", EccHelper.getProperties().getCurve());
        Assert.assertNotNull(EccHelper.getProperties().getPublicKey());
        Assert.assertNotNull(EccHelper.getProperties().getPrivateKey());
    }

    @Test
    public void testLoadKeyPairWithGroup() throws Exception {
        KeyPair pair = EccKeyGenerator.generateKeyPair();
        Ecc item = new Ecc();
        item.setName("primary");
        item.setPublicKey(EccKeyGenerator.getPublicKeyStr(pair.getPublic()));
        item.setPrivateKey(EccKeyGenerator.getPrivateKeyStr(pair.getPrivate()));
        List<org.tools.encrypt.entity.KeyPair> group = new ArrayList<>();
        group.add(item);
        Ecc ecc = new Ecc();
        ecc.setGroupEnabled(true);
        ecc.setGroup(group);
        EccHelper.load(ecc, "primary");
        Assert.assertEquals(EccKeyGenerator.getPublicKeyStr(pair.getPublic()),
                EccHelper.getProperties().getPublicKeyStr());
    }

    /**
     * 分组自身配置了曲线时应优先于顶层 curve。
     *
     * @throws Exception 密钥生成或装载失败时抛出
     */
    @Test
    public void testLoadGroupCurveTakesPrecedence() throws Exception {
        KeyPair pair = EccKeyGenerator.generateKeyPair("secp384r1");
        Ecc item = new Ecc();
        item.setName("primary");
        item.setCurve("secp384r1");
        item.setPublicKey(EccKeyGenerator.getPublicKeyStr(pair.getPublic()));
        item.setPrivateKey(EccKeyGenerator.getPrivateKeyStr(pair.getPrivate()));
        List<org.tools.encrypt.entity.KeyPair> group = new ArrayList<>();
        group.add(item);
        Ecc ecc = new Ecc();
        ecc.setCurve("secp256r1");
        ecc.setGroupEnabled(true);
        ecc.setGroup(group);
        try {
            EccHelper.load(ecc, "primary");
            Assert.assertEquals("secp384r1", EccHelper.getProperties().getCurve());
        } finally {
            // 还原全局默认配置，避免静态 properties 污染同 JVM 内的其它用例
            KeyPair defaultPair = EccKeyGenerator.generateKeyPair("secp256r1");
            EccProperties restored = new EccProperties();
            restored.setPublicKey(defaultPair.getPublic());
            restored.setPrivateKey(defaultPair.getPrivate());
            restored.setPublicKeyStr(EccKeyGenerator.getPublicKeyStr(defaultPair.getPublic()));
            restored.setPrivateKeyStr(EccKeyGenerator.getPrivateKeyStr(defaultPair.getPrivate()));
            EccHelper.load(restored);
        }
    }

    @Test
    public void testLoadProperties() throws Exception {
        KeyPair pair = EccKeyGenerator.generateKeyPair();
        EccProperties properties = new EccProperties();
        properties.setPublicKey(pair.getPublic());
        properties.setPrivateKey(pair.getPrivate());
        properties.setPublicKeyStr(EccKeyGenerator.getPublicKeyStr(pair.getPublic()));
        properties.setPrivateKeyStr(EccKeyGenerator.getPrivateKeyStr(pair.getPrivate()));
        EccHelper.load(properties);
        Assert.assertSame(properties, EccHelper.getProperties());
        Assert.assertEquals("secp256r1", EccHelper.getProperties().getCurve());
    }

    /**
     * 只提供公钥对象的配置应被拒绝。
     *
     * @throws Exception 密钥生成失败时抛出
     */
    @Test
    public void testLoadPropertiesWithOnlyPublicKeyRejected() throws Exception {
        EccProperties properties = new EccProperties();
        properties.setPublicKey(EccKeyGenerator.generateKeyPair().getPublic());
        try {
            EccHelper.load(properties);
            Assert.fail("只提供公钥应抛 EccException");
        } catch (EccException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    /**
     * 只提供私钥对象的配置应被拒绝。
     *
     * @throws Exception 密钥生成失败时抛出
     */
    @Test
    public void testLoadPropertiesWithOnlyPrivateKeyRejected() throws Exception {
        EccProperties properties = new EccProperties();
        properties.setPrivateKey(EccKeyGenerator.generateKeyPair().getPrivate());
        try {
            EccHelper.load(properties);
            Assert.fail("只提供私钥应抛 EccException");
        } catch (EccException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    @Test
    public void testLoadRejectsNonEccKeyPair() {
        try {
            EccHelper.load(new org.tools.encrypt.entity.KeyPair(), "primary");
            Assert.fail("非 Ecc 实体应抛 EccException");
        } catch (EccException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }
}
