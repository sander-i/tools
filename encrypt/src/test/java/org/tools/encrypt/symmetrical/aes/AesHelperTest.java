package org.tools.encrypt.symmetrical.aes;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Aes;
import org.tools.encrypt.entity.symmetrical.AesProperties;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.SecretKey;

import java.util.ArrayList;
import java.util.List;

/**
 * AesHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander 2026/10/24
 * @since 1.0.6.1
 */
public class AesHelperTest {
    /** 32 个 Hex 字符 = 16 字节 = AES-128 */
    private static final String HEX_KEY = "000102030405060708090a0b0c0d0e0f";
    private static final String IV = "0123456789abcdef";
    private static final String PLAINTEXT = "AES smoke 测试文本";

    private static AesProperties loadGlobal() throws AesException {
        AesProperties props = new AesProperties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(AesKeyGenerator.getSecretKey("AES", HEX_KEY));
        props.setIvParameter(IV);
        AesHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws AesException {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm("AES");
        keyPair.setAlgorithmMode("AES/CBC/PKCS5Padding");
        keyPair.setSecretKey(HEX_KEY);
        keyPair.setIvParameter(IV);
        AesHelper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, AesHelper.decode(AesHelper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Aes aes = new Aes();
        aes.setEnabled(true);
        aes.setGroupEnabled(true);
        aes.setSecretKey(HEX_KEY);
        aes.setIvParameter(IV);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        email.setIvParameter("8765432187654321");
        groupList.add(email);
        aes.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Aes 顶层配置补齐
        AesHelper.load(aes, "email");

        String ciphertext = AesHelper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, AesHelper.decode(ciphertext));
        Assert.assertEquals("8765432187654321", AesHelper.getProperties().getIvParameter());
    }

    @Test
    public void testLoadProperties() throws AesException {
        loadGlobal();
        Assert.assertEquals(IV, AesHelper.getProperties().getIvParameter());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws AesException {
        loadGlobal();
        String ciphertext = AesHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, AesHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws AesException {
        loadGlobal();
        String ciphertext = AesHelper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, AesHelper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws AesException {
        loadGlobal();
        SecretKey key = AesKeyGenerator.getSecretKey("AES", HEX_KEY);
        String ciphertext = AesHelper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, AesHelper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws AesException {
        SecretKey key = AesKeyGenerator.getSecretKey();
        String hexKey = AesKeyGenerator.getSecretKeyStr(key);

        AesProperties props = new AesProperties();
        props.setSecretKey(key);
        props.setIvParameter(IV);
        AesHelper.load(props);

        Assert.assertEquals(PLAINTEXT, AesHelper.decode(hexKey, AesHelper.encode(PLAINTEXT)));
    }

    @Test(expected = AesException.class)
    public void testNonHexKeyRejected() throws AesException {
        AesKeyGenerator.getSecretKey("AES", "这不是Hex密钥");
    }

    /**
     * 字符集回归：解密输出此前使用 JVM 默认 charset，GBK 平台上中文 round-trip 乱码；现已统一 UTF-8。
     *
     * @throws AesException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws AesException {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, AesHelper.decode(AesHelper.encode(plaintext)));
    }
}
