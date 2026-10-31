package org.tools.encrypt.symmetrical.skipjack;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.SkipjackProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.skipjack.SkipjackException;

import javax.crypto.SecretKey;

/**
 * SkipjackKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKeyStr / getSecretKey(String,String) / getProperties
 *
 * @author sander
 * @since 2.0.1
 */
public class SkipjackKeyGeneratorTest {
    /** 20 个 Hex 字符 = 10 字节 = 80 位 Skipjack 密钥 */
    private static final String HEX_KEY = "00998877665544332211";

    @Test
    public void testGetSecretKey() throws SkipjackException {
        SecretKey key = SkipjackKeyGenerator.getSecretKey();
        Assert.assertEquals(AlgorithmEnum.SKIPJACK.getAlgorithm(), key.getAlgorithm());
        // Skipjack 固定 80 位 = 10 字节
        Assert.assertEquals(10, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyStr() throws SkipjackException {
        String hex = SkipjackKeyGenerator.getSecretKeyStr(SkipjackKeyGenerator.getSecretKey());
        Assert.assertEquals(20, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{20}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws SkipjackException {
        SecretKey key = SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), HEX_KEY);
        Assert.assertEquals(10, key.getEncoded().length);
        Assert.assertEquals(HEX_KEY, SkipjackKeyGenerator.getSecretKeyStr(key));
    }

    @Test
    public void testGetProperties() throws SkipjackException {
        SkipjackProperties properties = SkipjackKeyGenerator.getProperties();
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
        Assert.assertEquals(AlgorithmEnum.SKIPJACK.getAlgorithm(), properties.getAlgorithm());
        Assert.assertEquals(80, properties.getKeyBits());
        Assert.assertEquals(10, properties.getSecretKey().getEncoded().length);
    }

    @Test(expected = SkipjackException.class)
    public void testNonHexKeyRejected() throws SkipjackException {
        SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), "这不是Hex密钥");
    }

    @Test
    public void testWrongLengthKeyRejected() {
        try {
            SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), "0123456789abcdef");
            Assert.fail("8 字节密钥应被拒绝");
        } catch (SkipjackException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }
}
