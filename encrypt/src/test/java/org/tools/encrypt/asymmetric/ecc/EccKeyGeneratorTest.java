package org.tools.encrypt.asymmetric.ecc;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.asymmetric.EccProperties;
import org.tools.encrypt.exception.asymmetric.ecc.EccException;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * EccKeyGenerator 冒烟测试
 * 覆盖全部公开方法：generateKeyPair×2 / getPublicKey / getPrivateKey / getPublicKeyStr×2 /
 * getPrivateKeyStr×2 / getPublicKey(String,String) / getPrivateKey(String,String) / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class EccKeyGeneratorTest {
    @Test
    public void testGenerateKeyPair() throws Exception {
        KeyPair keyPair = EccKeyGenerator.generateKeyPair();
        Assert.assertEquals("EC", keyPair.getPublic().getAlgorithm());
        Assert.assertEquals("EC", keyPair.getPrivate().getAlgorithm());
    }

    @Test
    public void testGenerateKeyPairWithCurve() throws Exception {
        KeyPair keyPair = EccKeyGenerator.generateKeyPair("secp384r1");
        Assert.assertEquals("EC", keyPair.getPublic().getAlgorithm());
        Assert.assertTrue(EccKeyGenerator.getPublicKeyStr(keyPair.getPublic()).length() > 100);
    }

    @Test
    public void testGenerateKeyPairWithUnknownCurve() {
        try {
            EccKeyGenerator.generateKeyPair("no-such-curve");
            Assert.fail("未知曲线应抛 EccException");
        } catch (EccException e) {
            Assert.assertNotNull(e.getCause());
        }
    }

    @Test
    public void testGetPublicKey() throws Exception {
        PublicKey publicKey = EccKeyGenerator.getPublicKey();
        Assert.assertNotNull(publicKey);
        Assert.assertEquals("EC", publicKey.getAlgorithm());
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        PrivateKey privateKey = EccKeyGenerator.getPrivateKey();
        Assert.assertNotNull(privateKey);
        Assert.assertEquals("EC", privateKey.getAlgorithm());
    }

    @Test
    public void testGetPublicKeyStr() throws Exception {
        String publicKeyStr = EccKeyGenerator.getPublicKeyStr();
        Assert.assertNotNull(publicKeyStr);
        Assert.assertEquals(EccKeyGenerator.getPublicKeyStr(EccKeyGenerator.getPublicKey()), publicKeyStr);
    }

    @Test
    public void testGetPrivateKeyStr() throws Exception {
        String privateKeyStr = EccKeyGenerator.getPrivateKeyStr();
        Assert.assertNotNull(privateKeyStr);
        Assert.assertEquals(EccKeyGenerator.getPrivateKeyStr(EccKeyGenerator.getPrivateKey()), privateKeyStr);
    }

    @Test
    public void testGetPublicKeyFromString() throws Exception {
        PublicKey publicKey = EccKeyGenerator.getPublicKey("EC", EccKeyGenerator.getPublicKeyStr());
        Assert.assertEquals("EC", publicKey.getAlgorithm());
        Assert.assertArrayEquals(EccKeyGenerator.getPublicKey().getEncoded(), publicKey.getEncoded());
    }

    @Test
    public void testGetPrivateKeyFromString() throws Exception {
        PrivateKey privateKey = EccKeyGenerator.getPrivateKey("EC", EccKeyGenerator.getPrivateKeyStr());
        Assert.assertEquals("EC", privateKey.getAlgorithm());
        Assert.assertArrayEquals(EccKeyGenerator.getPrivateKey().getEncoded(), privateKey.getEncoded());
    }

    @Test
    public void testGetProperties() throws Exception {
        EccProperties properties = EccKeyGenerator.getProperties();
        Assert.assertNotNull(properties.getPublicKey());
        Assert.assertNotNull(properties.getPrivateKey());
        Assert.assertNotNull(properties.getPublicKeyStr());
        Assert.assertNotNull(properties.getPrivateKeyStr());
        Assert.assertEquals("EC", properties.getAlgorithm());
        Assert.assertEquals("secp256r1", properties.getCurve());
        Assert.assertEquals(256, properties.getKeyBits());
    }
}
