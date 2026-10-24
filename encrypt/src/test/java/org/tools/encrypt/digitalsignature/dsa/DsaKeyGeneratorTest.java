package org.tools.encrypt.digitalsignature.dsa;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.digitalsignature.DsaProperties;

import java.security.KeyPair;
import org.tools.encrypt.exception.EncryptException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * DsaKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getKeyPair / getPublicKey / getPrivateKey / getPublicKeyStr×2 /
 * getPrivateKeyStr×2 / getPublicKey(String,String) / getPrivateKey(String,String) / getDsaProperties
 *
 * @author sander 2026/10/24
 */
public class DsaKeyGeneratorTest {

    @Test
    public void testGetKeyPair() throws Exception {
        KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        Assert.assertNotNull(keyPair.getPublic());
        Assert.assertNotNull(keyPair.getPrivate());
        Assert.assertEquals("DSA", keyPair.getPublic().getAlgorithm());
    }

    @Test
    public void testGetPublicKey() throws Exception {
        Assert.assertNotNull(DsaKeyGenerator.getPublicKey());
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        Assert.assertNotNull(DsaKeyGenerator.getPrivateKey());
    }

    @Test
    public void testGetPublicKeyStr() throws Exception {
        Assert.assertNotNull(DsaKeyGenerator.getPublicKeyStr());
    }

    @Test
    public void testGetPrivateKeyStr() throws Exception {
        Assert.assertNotNull(DsaKeyGenerator.getPrivateKeyStr());
    }

    @Test
    public void testGetPublicKeyStrFromObject() throws Exception {
        PublicKey publicKey = DsaKeyGenerator.getPublicKey();
        Assert.assertEquals(DsaKeyGenerator.getPublicKeyStr(), DsaKeyGenerator.getPublicKeyStr(publicKey));
    }

    @Test
    public void testGetPrivateKeyStrFromObject() throws Exception {
        PrivateKey privateKey = DsaKeyGenerator.getPrivateKey();
        Assert.assertEquals(DsaKeyGenerator.getPrivateKeyStr(), DsaKeyGenerator.getPrivateKeyStr(privateKey));
    }

    @Test
    public void testGetPublicKeyFromString() throws Exception {
        // KeyFactory 参数为密钥算法名 DSA（请勿传签名算法名）
        PublicKey publicKey = DsaKeyGenerator.getPublicKey("DSA", DsaKeyGenerator.getPublicKeyStr());
        Assert.assertEquals("DSA", publicKey.getAlgorithm());
    }

    @Test
    public void testGetPrivateKeyFromString() throws Exception {
        PrivateKey privateKey = DsaKeyGenerator.getPrivateKey("DSA", DsaKeyGenerator.getPrivateKeyStr());
        Assert.assertEquals("DSA", privateKey.getAlgorithm());
    }

    @Test
    public void testGetDsaProperties() throws Exception {
        DsaProperties properties = DsaKeyGenerator.getDsaProperties();
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getPublicKeyStr());
        Assert.assertNotNull(properties.getPrivateKeyStr());
    }

}
