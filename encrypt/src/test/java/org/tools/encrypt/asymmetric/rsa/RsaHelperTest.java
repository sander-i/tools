package org.tools.encrypt.asymmetric.rsa;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.asymmetric.RsaProperties;
import org.tools.encrypt.exception.EncryptException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RsaHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / sign×3 / verify×3 / getProperties
 *
 * @author sander 2026/10/24
 * @since 1.0.6.1
 */
public class RsaHelperTest {
    private static final String PLAINTEXT = "RSA smoke 测试文本";

    private static String publicKeyStr() {
        return RsaKeyGenerator.getPublicKeyStr();
    }

    private static String privateKeyStr() {
        return RsaKeyGenerator.getPrivateKeyStr();
    }

    private static RsaProperties loadGlobal() throws EncryptException, Exception {
        RsaKeyGenerator.getKeyPair();
        RsaProperties props = new RsaProperties();
        props.setPublicKeyStr(publicKeyStr());
        props.setPrivateKeyStr(privateKeyStr());
        props.setPublicKey(RsaKeyGenerator.getPublicKey(publicKeyStr()));
        props.setPrivateKey(RsaKeyGenerator.getPrivateKey(privateKeyStr()));
        RsaHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm("RSA");
        keyPair.setAlgorithmMode("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        keyPair.setSignAlgorithm("SHA256withRSA");
        keyPair.setKeyBits(2048);
        keyPair.setPublicKey(publicKeyStr());
        keyPair.setPrivateKey(privateKeyStr());
        RsaHelper.load(keyPair);

        // 实体上的配置现会传递到激活属性
        Assert.assertEquals("SHA256withRSA", RsaHelper.getProperties().getSignAlgorithm());
        Assert.assertEquals(2048, RsaHelper.getProperties().getKeyBits());
    }

    @Test
    public void testLoadProperties() throws Exception {
        loadGlobal();
        Assert.assertNotNull(RsaHelper.getProperties());
        Assert.assertEquals("RSA", RsaHelper.getProperties().getAlgorithm());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws Exception {
        loadGlobal();
        String ciphertext = RsaHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, RsaHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithStringKeys() throws Exception {
        loadGlobal();
        String ciphertext = RsaHelper.encode(publicKeyStr(), PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, RsaHelper.decode(privateKeyStr(), ciphertext));
    }

    @Test
    public void testEncodeDecodeWithKeyObjects() throws Exception {
        loadGlobal();
        PublicKey publicKey = RsaKeyGenerator.getPublicKey(publicKeyStr());
        PrivateKey privateKey = RsaKeyGenerator.getPrivateKey(privateKeyStr());
        String ciphertext = RsaHelper.encode(publicKey, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, RsaHelper.decode(privateKey, ciphertext));
    }

    @Test
    public void testSignVerifyWithGlobalConfig() throws Exception {
        loadGlobal();
        String sign = RsaHelper.sign(PLAINTEXT);
        Assert.assertTrue(RsaHelper.verify(PLAINTEXT, sign));
        Assert.assertFalse(RsaHelper.verify(PLAINTEXT + "tampered", sign));
    }

    @Test
    public void testSignVerifyWithStringKeys() throws Exception {
        loadGlobal();
        String sign = RsaHelper.sign(privateKeyStr(), PLAINTEXT);
        Assert.assertTrue(RsaHelper.verify(publicKeyStr(), PLAINTEXT, sign));
    }

    @Test
    public void testSignVerifyWithKeyObjects() throws Exception {
        loadGlobal();
        PrivateKey privateKey = RsaKeyGenerator.getPrivateKey(privateKeyStr());
        PublicKey publicKey = RsaKeyGenerator.getPublicKey(publicKeyStr());
        String sign = RsaHelper.sign(privateKey, PLAINTEXT);
        Assert.assertTrue(RsaHelper.verify(publicKey, PLAINTEXT, sign));
    }
}
