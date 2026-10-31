package org.tools.encrypt.symmetrical.skipjack;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Skipjack;
import org.tools.encrypt.entity.symmetrical.SkipjackProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.skipjack.SkipjackException;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

/**
 * SkipjackHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * <p>不编造标准向量：仅做 round-trip 与确定性断言。</p>
 *
 * @author sander
 * @since 2.0.1
 */
public class SkipjackHelperTest {
    /** 20 个 Hex 字符 = 10 字节 = 80 位 Skipjack 密钥 */
    private static final String HEX_KEY = "00998877665544332211";
    /** Skipjack 分组长度 64 位，CBC 的 IV 必须为 8 字节 */
    private static final String IV = "12345678";
    private static final String PLAINTEXT = "Skipjack smoke 测试文本";

    private static SkipjackProperties loadGlobal() throws SkipjackException {
        SkipjackProperties props = new SkipjackProperties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), HEX_KEY));
        props.setIvParameter(IV);
        SkipjackHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws SkipjackException {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm(AlgorithmEnum.SKIPJACK.getAlgorithm());
        keyPair.setAlgorithmMode(AlgorithmModeEnum.SKIPJACK.getAlgorithmMode());
        keyPair.setKeyBits(80);
        keyPair.setSecretKey(HEX_KEY);
        SkipjackHelper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(SkipjackHelper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Skipjack skipjack = new Skipjack();
        skipjack.setEnabled(true);
        skipjack.setGroupEnabled(true);
        skipjack.setSecretKey(HEX_KEY);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        email.setIvParameter("87654321");
        groupList.add(email);
        skipjack.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Skipjack 顶层配置补齐
        SkipjackHelper.load(skipjack, "email");

        String ciphertext = SkipjackHelper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(ciphertext));
        Assert.assertEquals("87654321", SkipjackHelper.getProperties().getIvParameter());
    }

    @Test
    public void testLoadProperties() throws SkipjackException {
        loadGlobal();
        Assert.assertEquals(IV, SkipjackHelper.getProperties().getIvParameter());
        Assert.assertEquals(AlgorithmEnum.SKIPJACK.getAlgorithm(), SkipjackHelper.getProperties().getAlgorithm());
        Assert.assertEquals(AlgorithmModeEnum.SKIPJACK.getAlgorithmMode(),
                SkipjackHelper.getProperties().getAlgorithmMode());
    }

    @Test
    public void testGetProperties() throws SkipjackException {
        SkipjackProperties props = loadGlobal();
        Assert.assertSame(props, SkipjackHelper.getProperties());
        Assert.assertNotNull(SkipjackHelper.getProperties().getSecretKey());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws SkipjackException {
        loadGlobal();
        String ciphertext = SkipjackHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws SkipjackException {
        loadGlobal();
        String ciphertext = SkipjackHelper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws SkipjackException {
        loadGlobal();
        SecretKey key = SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), HEX_KEY);
        String ciphertext = SkipjackHelper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws SkipjackException {
        SecretKey key = SkipjackKeyGenerator.getSecretKey();
        String hexKey = SkipjackKeyGenerator.getSecretKeyStr(key);

        SkipjackProperties props = new SkipjackProperties();
        props.setSecretKey(key);
        SkipjackHelper.load(props);

        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(hexKey, SkipjackHelper.encode(PLAINTEXT)));
    }

    /**
     * 确定性：同一密钥、同一模式下两次加密结果必须相同（ECB 无随机 IV）。
     *
     * @throws SkipjackException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testEncryptionIsDeterministic() throws SkipjackException {
        loadGlobal();
        Assert.assertEquals(SkipjackHelper.encode(PLAINTEXT), SkipjackHelper.encode(PLAINTEXT));
    }

    /**
     * 字符集回归：含中文的明文必须按 UTF-8 round-trip。
     *
     * @throws SkipjackException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws SkipjackException {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, SkipjackHelper.decode(SkipjackHelper.encode(plaintext)));
    }

    /**
     * CBC 模式 round-trip（8 字节 IV）。
     *
     * @throws SkipjackException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testCbcModeRoundTrip() throws SkipjackException {
        SkipjackProperties props = new SkipjackProperties();
        props.setAlgorithmMode(AlgorithmModeEnum.SKIPJACK_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), HEX_KEY));
        props.setIvParameter(IV);
        SkipjackHelper.load(props);

        String ciphertext = SkipjackHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, SkipjackHelper.decode(ciphertext));
    }

    /**
     * CBC 模式缺 IV 必须被拒绝。
     *
     * @throws SkipjackException 预期抛出
     */
    @Test(expected = SkipjackException.class)
    public void testCbcWithoutIvRejected() throws SkipjackException {
        SkipjackProperties props = new SkipjackProperties();
        props.setAlgorithmMode(AlgorithmModeEnum.SKIPJACK_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), HEX_KEY));
        SkipjackHelper.load(props);
    }

    /**
     * CBC 模式 IV 长度不是 8 字节必须被拒绝。
     *
     * @throws SkipjackException 预期抛出
     */
    @Test(expected = SkipjackException.class)
    public void testCbcWrongIvLengthRejected() throws SkipjackException {
        SkipjackProperties props = new SkipjackProperties();
        props.setAlgorithmMode(AlgorithmModeEnum.SKIPJACK_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), HEX_KEY));
        props.setIvParameter("1234567");
        SkipjackHelper.load(props);
    }

    /**
     * 非 10 字节（80 位）密钥必须被拒绝。
     *
     * @throws SkipjackException 预期抛出
     */
    @Test(expected = SkipjackException.class)
    public void testWrongLengthKeyRejected() throws SkipjackException {
        SkipjackKeyGenerator.getSecretKey(AlgorithmEnum.SKIPJACK.getAlgorithm(), "0123456789abcdef");
    }

    @Test(expected = SkipjackException.class)
    public void testWrongKeyPairTypeRejected() throws Exception {
        SkipjackHelper.load(new KeyPair(), "email");
    }
}
