package org.tools.encrypt.symmetrical.idea;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * IdeaHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class IdeaHelperTest {
    /** 32 个 Hex 字符 = 16 字节 = 128 位 IDEA 密钥（load(KeyPair) 路径经 Hex 解码） */
    private static final String HEX_KEY = "b1d58f20dfe96a6659ad818370500e66";
    private static final String RAW_KEY = "0123456789abcdef";
    private static final String PLAINTEXT = "IDEA smoke 测试文本";

    private static IdeaProperties loadGlobal() throws IdeaException {
        IdeaProperties props = new IdeaProperties();
        props.setSecretKeyStr(RAW_KEY);
        props.setSecretKey(new SecretKeySpec(RAW_KEY.getBytes(StandardCharsets.UTF_8), "IDEA"));
        IdeaHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws IdeaException {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm("IDEA");
        keyPair.setAlgorithmMode("IDEA/ECB/ISO10126Padding");
        keyPair.setKeyBits(128);
        keyPair.setSecretKey(HEX_KEY);
        IdeaHelper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, IdeaHelper.decode(IdeaHelper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadProperties() throws IdeaException {
        loadGlobal();
        Assert.assertNotNull(IdeaHelper.getProperties());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws IdeaException {
        loadGlobal();
        String ciphertext = IdeaHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, IdeaHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithStringKey() throws IdeaException {
        // String 重载走 Hex 解码路径，需 32 个 Hex 字符（16 字节）
        loadGlobal();
        String ciphertext = IdeaHelper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, IdeaHelper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws IdeaException {
        loadGlobal();
        SecretKey key = new SecretKeySpec(RAW_KEY.getBytes(StandardCharsets.UTF_8), "IDEA");
        String ciphertext = IdeaHelper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, IdeaHelper.decode(key, ciphertext));
    }
}
