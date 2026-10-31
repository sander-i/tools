package org.tools.encrypt.asymmetric.dh;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.asymmetric.DhProperties;
import org.tools.encrypt.exception.asymmetric.dh.DhException;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * DhKeyGenerator 冒烟测试
 * 覆盖全部公开方法：generateKeyPair×3 / getPublicKey / getPrivateKey / getPublicKeyStr×2 /
 * getPrivateKeyStr×2 / getPublicKey(String,String) / getPrivateKey(String,String) /
 * getParams / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class DhKeyGeneratorTest {
    @Test
    public void testGenerateKeyPairDefault() throws Exception {
        KeyPair keyPair = DhKeyGenerator.generateKeyPair();
        Assert.assertNotNull(keyPair.getPublic());
        Assert.assertNotNull(keyPair.getPrivate());
        Assert.assertEquals("DH", keyPair.getPublic().getAlgorithm());
        // 默认 2048 位
        Assert.assertEquals(2048, ((DHPublicKey) keyPair.getPublic()).getParams().getP().bitLength());
    }

    @Test
    public void testGenerateKeyPairWithKeyBits() throws Exception {
        KeyPair keyPair = DhKeyGenerator.generateKeyPair(1024);
        Assert.assertEquals("DH", keyPair.getPublic().getAlgorithm());
        Assert.assertEquals(1024, ((DHPublicKey) keyPair.getPublic()).getParams().getP().bitLength());
    }

    @Test
    public void testGenerateKeyPairWithParams() throws Exception {
        KeyPair first = DhKeyGenerator.generateKeyPair(1024);
        DHParameterSpec params = DhKeyGenerator.getParams(first.getPublic());
        KeyPair second = DhKeyGenerator.generateKeyPair(params);
        Assert.assertEquals(params.getP(), ((DHPublicKey) second.getPublic()).getParams().getP());
        Assert.assertEquals(params.getG(), ((DHPublicKey) second.getPublic()).getParams().getG());
    }

    @Test
    public void testGenerateKeyPairWithNullParams() {
        try {
            DhKeyGenerator.generateKeyPair((DHParameterSpec) null);
            Assert.fail("params 为空应抛出 DhException");
        } catch (DhException e) {
            Assert.assertTrue(e.getMessage().contains("params"));
        }
    }

    @Test
    public void testGenerateKeyPairWithTooSmallKeyBits() {
        try {
            DhKeyGenerator.generateKeyPair(256);
            Assert.fail("密钥位数小于 512 应抛出 DhException");
        } catch (DhException e) {
            Assert.assertTrue(e.getMessage().contains("512"));
        }
    }

    @Test
    public void testGetPublicKey() throws Exception {
        PublicKey publicKey = DhKeyGenerator.getPublicKey();
        Assert.assertNotNull(publicKey);
        Assert.assertEquals("DH", publicKey.getAlgorithm());
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        PrivateKey privateKey = DhKeyGenerator.getPrivateKey();
        Assert.assertNotNull(privateKey);
        Assert.assertEquals("DH", privateKey.getAlgorithm());
    }

    @Test
    public void testGetPublicKeyStr() throws Exception {
        Assert.assertTrue(DhKeyGenerator.getPublicKeyStr().length() > 200);
    }

    @Test
    public void testGetPrivateKeyStr() throws Exception {
        Assert.assertTrue(DhKeyGenerator.getPrivateKeyStr().length() > 200);
    }

    @Test
    public void testGetPublicKeyStrFromObject() throws Exception {
        PublicKey publicKey = DhKeyGenerator.getPublicKey();
        Assert.assertEquals(DhKeyGenerator.getPublicKeyStr(), DhKeyGenerator.getPublicKeyStr(publicKey));
    }

    @Test
    public void testGetPrivateKeyStrFromObject() throws Exception {
        PrivateKey privateKey = DhKeyGenerator.getPrivateKey();
        Assert.assertEquals(DhKeyGenerator.getPrivateKeyStr(), DhKeyGenerator.getPrivateKeyStr(privateKey));
    }

    @Test
    public void testGetPublicKeyFromString() throws Exception {
        PublicKey publicKey = DhKeyGenerator.getPublicKey("DH", DhKeyGenerator.getPublicKeyStr());
        Assert.assertEquals("DH", publicKey.getAlgorithm());
    }

    @Test
    public void testGetPrivateKeyFromString() throws Exception {
        PrivateKey privateKey = DhKeyGenerator.getPrivateKey("DH", DhKeyGenerator.getPrivateKeyStr());
        Assert.assertEquals("DH", privateKey.getAlgorithm());
    }

    @Test
    public void testGetParams() throws Exception {
        PublicKey publicKey = DhKeyGenerator.getPublicKey();
        DHParameterSpec params = DhKeyGenerator.getParams(publicKey);
        Assert.assertNotNull(params);
        Assert.assertEquals(2048, params.getP().bitLength());
    }

    @Test
    public void testGetParamsWithNonDhKey() {
        try {
            DhKeyGenerator.getParams(new PublicKey() {
                @Override
                public String getAlgorithm() {
                    return "RSA";
                }

                @Override
                public String getFormat() {
                    return null;
                }

                @Override
                public byte[] getEncoded() {
                    return null;
                }
            });
            Assert.fail("非 DH 公钥应抛出 IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("DH"));
        }
    }

    @Test
    public void testGetProperties() throws Exception {
        DhProperties properties = DhKeyGenerator.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertEquals("DH", properties.getAlgorithm());
        Assert.assertEquals(2048, properties.getKeyBits());
        Assert.assertNotNull(properties.getPublicKey());
        Assert.assertNotNull(properties.getPrivateKey());
        Assert.assertNotNull(properties.getPublicKeyStr());
        Assert.assertNotNull(properties.getPrivateKeyStr());
        Assert.assertNotNull(properties.getParams());
        Assert.assertEquals(2048, properties.getParams().getP().bitLength());
    }
}
