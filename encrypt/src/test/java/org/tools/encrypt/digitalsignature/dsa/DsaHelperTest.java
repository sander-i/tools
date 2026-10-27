package org.tools.encrypt.digitalsignature.dsa;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.digitalsignature.DsaProperties;
import org.tools.encrypt.exception.EncryptException;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * DsaHelper 冒烟测试
 * 覆盖全部公开方法：load×2 / sign×3 / verify×3
 *
 * @author sander 2026/10/24
 * @since 1.0.6.1
 */
public class DsaHelperTest {
    private static final String PLAINTEXT = "DSA smoke 测试文本";

    private static DsaProperties loadGlobal() throws EncryptException, NoSuchAlgorithmException {
        KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        DsaProperties props = new DsaProperties();
        props.setSignAlgorithm("SHA256withDSA");
        props.setPublicKey(keyPair.getPublic());
        props.setPrivateKey(keyPair.getPrivate());
        props.setPublicKeyStr(DsaKeyGenerator.getPublicKeyStr(keyPair.getPublic()));
        props.setPrivateKeyStr(DsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate()));
        DsaHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws Exception {
        KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        org.tools.encrypt.entity.KeyPair entity = new org.tools.encrypt.entity.KeyPair();
        entity.setSignAlgorithm("SHA256withDSA");
        entity.setPublicKey(DsaKeyGenerator.getPublicKeyStr(keyPair.getPublic()));
        entity.setPrivateKey(DsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate()));
        DsaHelper.load(entity);

        Assert.assertNotNull(DsaHelper.getProperties());
    }

    @Test
    public void testLoadProperties() throws Exception {
        loadGlobal();
        Assert.assertNotNull(DsaHelper.getProperties());
        Assert.assertEquals("SHA256withDSA", DsaHelper.getProperties().getSignAlgorithm());
    }

    @Test
    public void testSignVerifyWithGlobalConfig() throws Exception {
        loadGlobal();
        String sign = DsaHelper.sign(PLAINTEXT);
        Assert.assertTrue(DsaHelper.verify(PLAINTEXT, sign));
        Assert.assertFalse(DsaHelper.verify(PLAINTEXT + "tampered", sign));
    }

    @Test
    public void testSignVerifyWithStringKeys() throws Exception {
        loadGlobal();
        String privateKeyStr = DsaHelper.getProperties().getPrivateKeyStr();
        String publicKeyStr = DsaHelper.getProperties().getPublicKeyStr();
        String sign = DsaHelper.sign(privateKeyStr, PLAINTEXT);
        Assert.assertTrue(DsaHelper.verify(publicKeyStr, PLAINTEXT, sign));
    }

    @Test
    public void testSignVerifyWithKeyObjects() throws Exception {
        loadGlobal();
        PrivateKey privateKey = DsaKeyGenerator.getPrivateKey();
        PublicKey publicKey = DsaKeyGenerator.getPublicKey();
        String sign = DsaHelper.sign(privateKey, PLAINTEXT);
        Assert.assertTrue(DsaHelper.verify(publicKey, PLAINTEXT, sign));
    }
}
