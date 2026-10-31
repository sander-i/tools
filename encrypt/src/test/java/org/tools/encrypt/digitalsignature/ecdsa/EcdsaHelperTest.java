package org.tools.encrypt.digitalsignature.ecdsa;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.digitalsignature.EcdsaProperties;
import org.tools.encrypt.exception.digitalsignature.ecdsa.EcdsaException;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * EcdsaHelper 冒烟测试
 * 覆盖全部公开方法：load×2 / sign×3 / verify×3 / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class EcdsaHelperTest {
    private static final String PLAINTEXT = "ECDSA smoke 测试文本";

    /**
     * 装载全局配置并返回配置对象
     *
     * @return ECDSA 配置
     * @throws EcdsaException 密钥生成或装载失败
     */
    private static EcdsaProperties loadGlobal() throws EcdsaException {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        EcdsaProperties props = new EcdsaProperties();
        props.setSignAlgorithm("SHA256withECDSA");
        props.setPublicKey(keyPair.getPublic());
        props.setPrivateKey(keyPair.getPrivate());
        props.setPublicKeyStr(EcdsaKeyGenerator.getPublicKeyStr(keyPair.getPublic()));
        props.setPrivateKeyStr(EcdsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate()));
        EcdsaHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws Exception {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        org.tools.encrypt.entity.KeyPair entity = new org.tools.encrypt.entity.KeyPair();
        entity.setSignAlgorithm("SHA256withECDSA");
        entity.setPublicKey(EcdsaKeyGenerator.getPublicKeyStr(keyPair.getPublic()));
        entity.setPrivateKey(EcdsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate()));
        EcdsaHelper.load(entity);

        Assert.assertNotNull(EcdsaHelper.getProperties());
    }

    @Test
    public void testLoadProperties() throws Exception {
        loadGlobal();
        Assert.assertNotNull(EcdsaHelper.getProperties());
        Assert.assertEquals("SHA256withECDSA", EcdsaHelper.getProperties().getSignAlgorithm());
    }

    /**
     * KeyPair 未指定签名算法时应沿用默认 SHA256withECDSA，而不是把 null 写进配置。
     *
     * @throws Exception 密钥生成或装载失败时抛出
     */
    @Test
    public void testLoadKeyPairWithoutSignAlgorithmUsesDefault() throws Exception {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        org.tools.encrypt.entity.KeyPair entity = new org.tools.encrypt.entity.KeyPair();
        entity.setPublicKey(EcdsaKeyGenerator.getPublicKeyStr(keyPair.getPublic()));
        entity.setPrivateKey(EcdsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate()));
        EcdsaHelper.load(entity);

        Assert.assertEquals("SHA256withECDSA", EcdsaHelper.getProperties().getSignAlgorithm());
    }

    /**
     * 未提供任何公私钥字符串的 KeyPair 应在解析前被拒绝，而不是抛裸 NPE。
     *
     * @throws Exception 装载失败时抛出
     */
    @Test
    public void testLoadKeyPairWithoutKeysRejected() throws Exception {
        try {
            EcdsaHelper.load(new org.tools.encrypt.entity.KeyPair());
            Assert.fail("未提供公私钥应抛 EcdsaException");
        } catch (EcdsaException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    /**
     * 签名算法为 null 时应在预检前给出明确异常，而不是在 Signature.getInstance 处抛 NPE。
     *
     * @throws Exception 密钥生成失败时抛出
     */
    @Test
    public void testLoadPropertiesWithNullSignAlgorithmRejected() throws Exception {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        EcdsaProperties properties = new EcdsaProperties();
        properties.setPublicKey(keyPair.getPublic());
        properties.setPrivateKey(keyPair.getPrivate());
        properties.setSignAlgorithm(null);
        try {
            EcdsaHelper.load(properties);
            Assert.fail("signAlgorithm 为空应抛 EcdsaException");
        } catch (EcdsaException e) {
            Assert.assertTrue(e.getMessage().contains("signAlgorithm"));
        }
    }

    /**
     * 静态配置预初始化为默认值，未 load 时 getProperties 也不应为 null。
     */
    @Test
    public void testGetPropertiesNotNullBeforeLoad() {
        Assert.assertNotNull(EcdsaHelper.getProperties());
    }

    /**
     * 只提供公钥的配置应被拒绝。
     *
     * @throws Exception 密钥生成失败时抛出
     */
    @Test
    public void testLoadPropertiesWithOnlyPublicKeyRejected() throws Exception {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        EcdsaProperties properties = new EcdsaProperties();
        properties.setPublicKey(keyPair.getPublic());
        try {
            EcdsaHelper.load(properties);
            Assert.fail("只提供公钥应抛 EcdsaException");
        } catch (EcdsaException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    /**
     * 只提供私钥的配置应被拒绝。
     *
     * @throws Exception 密钥生成失败时抛出
     */
    @Test
    public void testLoadPropertiesWithOnlyPrivateKeyRejected() throws Exception {
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        EcdsaProperties properties = new EcdsaProperties();
        properties.setPrivateKey(keyPair.getPrivate());
        try {
            EcdsaHelper.load(properties);
            Assert.fail("只提供私钥应抛 EcdsaException");
        } catch (EcdsaException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    /**
     * 预检失败的配置不得覆盖已成功装载的配置。
     *
     * @throws Exception 密钥生成或装载失败时抛出
     */
    @Test
    public void testFailedLoadKeepsPreviousProperties() throws Exception {
        loadGlobal();
        KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        EcdsaProperties bogus = new EcdsaProperties();
        bogus.setSignAlgorithm("SHA256withBogus");
        bogus.setPublicKey(keyPair.getPublic());
        bogus.setPrivateKey(keyPair.getPrivate());
        try {
            EcdsaHelper.load(bogus);
            Assert.fail("非法签名算法应抛 EcdsaException");
        } catch (EcdsaException e) {
            Assert.assertNotNull(e.getCause());
        }

        Assert.assertEquals("SHA256withECDSA", EcdsaHelper.getProperties().getSignAlgorithm());
    }

    @Test
    public void testSignVerifyWithGlobalConfig() throws Exception {
        loadGlobal();
        String sign = EcdsaHelper.sign(PLAINTEXT);
        Assert.assertTrue(EcdsaHelper.verify(PLAINTEXT, sign));
        Assert.assertFalse(EcdsaHelper.verify(PLAINTEXT + "tampered", sign));
    }

    @Test(expected = EcdsaException.class)
    public void testVerifyTamperedSignFails() throws Exception {
        loadGlobal();
        // 结构非法的签名（Base64 解码后仅 3 字节，不是合法 DER SEQUENCE），
        // JCA 抛 SignatureException，包装为 EcdsaException
        EcdsaHelper.verify(PLAINTEXT, "AAAA");
    }

    /**
     * 签名结构合法但密钥不匹配时，验签应返回 false 而非抛异常。
     *
     * @throws Exception 密钥生成或签名失败时抛出
     */
    @Test
    public void testVerifyWithMismatchedKeyReturnsFalse() throws Exception {
        loadGlobal();
        String sign = EcdsaHelper.sign(PLAINTEXT);
        // EcdsaKeyGenerator 会缓存密钥对，这里用独立 KeyPairGenerator 生成另一把公钥
        java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(new java.security.spec.ECGenParameterSpec("secp256r1"));
        PublicKey otherPublicKey = keyPairGenerator.generateKeyPair().getPublic();
        Assert.assertFalse(EcdsaHelper.verify(otherPublicKey, PLAINTEXT, sign));
    }

    @Test
    public void testSignVerifyWithStringKeys() throws Exception {
        loadGlobal();
        String privateKeyStr = EcdsaHelper.getProperties().getPrivateKeyStr();
        String publicKeyStr = EcdsaHelper.getProperties().getPublicKeyStr();
        String sign = EcdsaHelper.sign(privateKeyStr, PLAINTEXT);
        Assert.assertTrue(EcdsaHelper.verify(publicKeyStr, PLAINTEXT, sign));
        Assert.assertFalse(EcdsaHelper.verify(publicKeyStr, PLAINTEXT + "tampered", sign));
    }

    @Test
    public void testSignVerifyWithKeyObjects() throws Exception {
        loadGlobal();
        PrivateKey privateKey = EcdsaKeyGenerator.getPrivateKey();
        PublicKey publicKey = EcdsaKeyGenerator.getPublicKey();
        String sign = EcdsaHelper.sign(privateKey, PLAINTEXT);
        Assert.assertTrue(EcdsaHelper.verify(publicKey, PLAINTEXT, sign));
    }

    @Test
    public void testStringAndObjectKeyConsistency() throws Exception {
        loadGlobal();
        String privateKeyStr = EcdsaHelper.getProperties().getPrivateKeyStr();
        String publicKeyStr = EcdsaHelper.getProperties().getPublicKeyStr();
        PrivateKey privateKey = EcdsaKeyGenerator.getPrivateKey();
        PublicKey publicKey = EcdsaKeyGenerator.getPublicKey();

        // 对象签名 → 字符串公钥验签
        String signByObject = EcdsaHelper.sign(privateKey, PLAINTEXT);
        Assert.assertTrue(EcdsaHelper.verify(publicKeyStr, PLAINTEXT, signByObject));

        // 字符串私钥签名 → 对象公钥验签
        String signByString = EcdsaHelper.sign(privateKeyStr, PLAINTEXT);
        Assert.assertTrue(EcdsaHelper.verify(publicKey, PLAINTEXT, signByString));
    }

    @Test
    public void testChineseRoundTrip() throws Exception {
        loadGlobal();
        String chinese = "中文 UTF-8 原文：数字签名往返测试 ✓";
        String sign = EcdsaHelper.sign(chinese);
        Assert.assertTrue(EcdsaHelper.verify(chinese, sign));
    }
}
