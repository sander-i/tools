package org.tools.encrypt.asymmetric.rsa;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.asymmetric.RsaProperties;
import org.tools.encrypt.exception.EncryptException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RsaKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getKeyPair / getPublicKey / getPrivateKey / getPublicKeyStr×2 /
 * getPrivateKeyStr×2 / getPublicKey(String) / getPrivateKey(String)
 *
 * @author sander 2026/10/24
 */
public class RsaKeyGeneratorTest {

    @Test
    public void testGetKeyPair() throws Exception {
        java.security.KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        Assert.assertNotNull(keyPair.getPublic());
        Assert.assertNotNull(keyPair.getPrivate());
        Assert.assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }

    @Test
    public void testGetPublicKey() throws Exception {
        Assert.assertNotNull(RsaKeyGenerator.getPublicKey());
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        Assert.assertNotNull(RsaKeyGenerator.getPrivateKey());
    }

    @Test
    public void testGetPublicKeyStr() throws Exception {
        // 默认 2048 位
        Assert.assertTrue(RsaKeyGenerator.getPublicKeyStr().length() > 200);
    }

    @Test
    public void testGetPrivateKeyStr() throws Exception {
        Assert.assertTrue(RsaKeyGenerator.getPrivateKeyStr().length() > 200);
    }

    @Test
    public void testGetPublicKeyStrFromObject() throws Exception {
        PublicKey publicKey = RsaKeyGenerator.getPublicKey();
        Assert.assertEquals(RsaKeyGenerator.getPublicKeyStr(), RsaKeyGenerator.getPublicKeyStr(publicKey));
    }

    @Test
    public void testGetPrivateKeyStrFromObject() throws Exception {
        PrivateKey privateKey = RsaKeyGenerator.getPrivateKey();
        Assert.assertEquals(RsaKeyGenerator.getPrivateKeyStr(), RsaKeyGenerator.getPrivateKeyStr(privateKey));
    }

    @Test
    public void testGetPublicKeyFromString() throws Exception {
        PublicKey publicKey = RsaKeyGenerator.getPublicKey(RsaKeyGenerator.getPublicKeyStr());
        Assert.assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Test
    public void testGetPrivateKeyFromString() throws Exception {
        PrivateKey privateKey = RsaKeyGenerator.getPrivateKey(RsaKeyGenerator.getPrivateKeyStr());
        Assert.assertEquals("RSA", privateKey.getAlgorithm());
    }

    @Test
    public void testGetRsaProperties() throws Exception {
        RsaProperties properties = RsaKeyGenerator.getRsaProperties();
        Assert.assertNotNull(properties.getPublicKey());
        Assert.assertNotNull(properties.getPrivateKey());
        Assert.assertNotNull(properties.getPublicKeyStr());
        Assert.assertNotNull(properties.getPrivateKeyStr());
    }

}
