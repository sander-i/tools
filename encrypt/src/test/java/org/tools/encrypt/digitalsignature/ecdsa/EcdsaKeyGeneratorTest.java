package org.tools.encrypt.digitalsignature.ecdsa;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.digitalsignature.EcdsaProperties;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;

/**
 * EcdsaKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getKeyPair / getPublicKey / getPrivateKey / getPublicKeyStr×2 /
 * getPrivateKeyStr×2 / getPublicKey(String,String) / getPrivateKey(String,String) / getEcdsaProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class EcdsaKeyGeneratorTest {
    @Test
    public void testGetKeyPair() throws Exception {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        Assert.assertNotNull(keyPair.getPublic());
        Assert.assertNotNull(keyPair.getPrivate());
        Assert.assertEquals("EC", keyPair.getPublic().getAlgorithm());
    }

    @Test
    public void testGetPublicKey() throws Exception {
        Assert.assertNotNull(EcdsaKeyGenerator.getPublicKey());
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        Assert.assertNotNull(EcdsaKeyGenerator.getPrivateKey());
    }

    @Test
    public void testGetPublicKeyStr() throws Exception {
        Assert.assertNotNull(EcdsaKeyGenerator.getPublicKeyStr());
    }

    @Test
    public void testGetPrivateKeyStr() throws Exception {
        Assert.assertNotNull(EcdsaKeyGenerator.getPrivateKeyStr());
    }

    @Test
    public void testGetPublicKeyStrFromObject() throws Exception {
        PublicKey publicKey = EcdsaKeyGenerator.getPublicKey();
        Assert.assertEquals(EcdsaKeyGenerator.getPublicKeyStr(), EcdsaKeyGenerator.getPublicKeyStr(publicKey));
    }

    @Test
    public void testGetPrivateKeyStrFromObject() throws Exception {
        PrivateKey privateKey = EcdsaKeyGenerator.getPrivateKey();
        Assert.assertEquals(EcdsaKeyGenerator.getPrivateKeyStr(), EcdsaKeyGenerator.getPrivateKeyStr(privateKey));
    }

    @Test
    public void testGetPublicKeyFromString() throws Exception {
        // KeyFactory 参数为密钥算法名 EC（请勿传签名算法名）
        PublicKey publicKey = EcdsaKeyGenerator.getPublicKey("EC", EcdsaKeyGenerator.getPublicKeyStr());
        Assert.assertEquals("EC", publicKey.getAlgorithm());
        Assert.assertArrayEquals(EcdsaKeyGenerator.getPublicKey().getEncoded(), publicKey.getEncoded());
    }

    @Test
    public void testGetPrivateKeyFromString() throws Exception {
        PrivateKey privateKey = EcdsaKeyGenerator.getPrivateKey("EC", EcdsaKeyGenerator.getPrivateKeyStr());
        Assert.assertEquals("EC", privateKey.getAlgorithm());
        Assert.assertArrayEquals(EcdsaKeyGenerator.getPrivateKey().getEncoded(), privateKey.getEncoded());
    }

    @Test
    public void testGetEcdsaProperties() throws Exception {
        EcdsaProperties properties = EcdsaKeyGenerator.getEcdsaProperties();
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getPublicKeyStr());
        Assert.assertNotNull(properties.getPrivateKeyStr());
        Assert.assertNotNull(properties.getPublicKey());
        Assert.assertNotNull(properties.getPrivateKey());
        Assert.assertEquals("EC", properties.getAlgorithm());
        Assert.assertEquals("SHA256withECDSA", properties.getSignAlgorithm());
        Assert.assertEquals("secp256r1", properties.getCurve());
        Assert.assertEquals(256, properties.getKeyBits());
    }

    /**
     * 设置曲线后应真正影响新生成的密钥对与回填的配置。
     *
     * @throws Exception 密钥生成失败时抛出
     */
    @Test
    public void testSetCurveAffectsGeneratedKeyPair() throws Exception {
        try {
            EcdsaKeyGenerator.setCurve("secp384r1");
            KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
            int fieldSize = ((ECPublicKey) keyPair.getPublic()).getParams().getCurve().getField().getFieldSize();
            Assert.assertEquals(384, fieldSize);
            Assert.assertEquals("secp384r1", EcdsaKeyGenerator.getEcdsaProperties().getCurve());
        } finally {
            // 重置静态状态，避免影响同 JVM 内的其它用例
            EcdsaKeyGenerator.setCurve("secp256r1");
        }
    }
}
