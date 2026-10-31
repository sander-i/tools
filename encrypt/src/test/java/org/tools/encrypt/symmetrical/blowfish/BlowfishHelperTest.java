package org.tools.encrypt.symmetrical.blowfish;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Blowfish;
import org.tools.encrypt.entity.symmetrical.BlowfishProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.blowfish.BlowfishException;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * BlowfishHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander
 * @since 2.0.1
 */
public class BlowfishHelperTest {
    /** 16 个 Hex 字符 = 8 字节 = 64 位 Blowfish 密钥 */
    private static final String HEX_KEY = "0123456789abcdef";
    /** Blowfish 分组长度 64 位，CBC 的 IV 必须为 8 字节 */
    private static final String IV = "12345678";
    private static final String PLAINTEXT = "Blowfish smoke 测试文本";

    private static BlowfishProperties loadGlobal() throws BlowfishException {
        BlowfishProperties props = new BlowfishProperties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), HEX_KEY));
        props.setIvParameter(IV);
        BlowfishHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws BlowfishException {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm(AlgorithmEnum.BLOWFISH.getAlgorithm());
        keyPair.setAlgorithmMode(AlgorithmModeEnum.BLOWFISH.getAlgorithmMode());
        keyPair.setKeyBits(128);
        keyPair.setSecretKey(HEX_KEY);
        BlowfishHelper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(BlowfishHelper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Blowfish blowfish = new Blowfish();
        blowfish.setEnabled(true);
        blowfish.setGroupEnabled(true);
        blowfish.setSecretKey(HEX_KEY);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        email.setIvParameter("87654321");
        groupList.add(email);
        blowfish.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Blowfish 顶层配置补齐
        BlowfishHelper.load(blowfish, "email");

        String ciphertext = BlowfishHelper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(ciphertext));
        Assert.assertEquals("87654321", BlowfishHelper.getProperties().getIvParameter());
    }

    @Test
    public void testLoadProperties() throws BlowfishException {
        loadGlobal();
        Assert.assertEquals(IV, BlowfishHelper.getProperties().getIvParameter());
        Assert.assertEquals(AlgorithmEnum.BLOWFISH.getAlgorithm(), BlowfishHelper.getProperties().getAlgorithm());
    }

    @Test
    public void testGetProperties() throws BlowfishException {
        BlowfishProperties props = loadGlobal();
        Assert.assertSame(props, BlowfishHelper.getProperties());
        Assert.assertNotNull(BlowfishHelper.getProperties().getSecretKey());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws BlowfishException {
        loadGlobal();
        String ciphertext = BlowfishHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws BlowfishException {
        loadGlobal();
        String ciphertext = BlowfishHelper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws BlowfishException {
        loadGlobal();
        SecretKey key = BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), HEX_KEY);
        String ciphertext = BlowfishHelper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey();
        String hexKey = BlowfishKeyGenerator.getSecretKeyStr(key);

        BlowfishProperties props = new BlowfishProperties();
        props.setSecretKey(key);
        BlowfishHelper.load(props);

        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(hexKey, BlowfishHelper.encode(PLAINTEXT)));
    }

    /**
     * 字符集回归：含中文的明文必须按 UTF-8 round-trip。
     *
     * @throws BlowfishException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws BlowfishException {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, BlowfishHelper.decode(BlowfishHelper.encode(plaintext)));
    }

    /**
     * CBC 模式 round-trip（8 字节 IV）。
     *
     * @throws BlowfishException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testCbcModeRoundTrip() throws BlowfishException {
        BlowfishProperties props = new BlowfishProperties();
        props.setAlgorithmMode(AlgorithmModeEnum.BLOWFISH_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), HEX_KEY));
        props.setIvParameter(IV);
        BlowfishHelper.load(props);

        String ciphertext = BlowfishHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(ciphertext));
    }

    /**
     * CBC 模式下缺少 IV 必须拒绝装载（否则 encode 会在 initCipher 中抛 NPE）。
     *
     * @throws BlowfishException 装载失败时抛出
     */
    @Test(expected = BlowfishException.class)
    public void testCbcModeRequiresIv() throws BlowfishException {
        BlowfishProperties props = new BlowfishProperties();
        props.setAlgorithmMode(AlgorithmModeEnum.BLOWFISH_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), HEX_KEY));
        BlowfishHelper.load(props);
    }

    /**
     * CBC 模式下 IV 按 UTF-8 编码必须恰好 8 字节。
     *
     * @throws BlowfishException 装载失败时抛出
     */
    @Test(expected = BlowfishException.class)
    public void testCbcModeRejectsWrongIvLength() throws BlowfishException {
        BlowfishProperties props = new BlowfishProperties();
        props.setAlgorithmMode(AlgorithmModeEnum.BLOWFISH_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), HEX_KEY));
        props.setIvParameter("1234567");
        BlowfishHelper.load(props);
    }

    /**
     * 只提供 Hex 密钥字符串时应自动解析为密钥对象，并能完成 round-trip。
     *
     * @throws BlowfishException 加载密钥或编解码失败时抛出
     */
    @Test
    public void testLoadPropertiesWithSecretKeyStrOnly() throws BlowfishException {
        BlowfishProperties props = new BlowfishProperties();
        props.setSecretKeyStr(HEX_KEY);
        props.setIvParameter(IV);
        BlowfishHelper.load(props);

        Assert.assertNotNull(BlowfishHelper.getProperties().getSecretKey());
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(BlowfishHelper.encode(PLAINTEXT)));
    }

    /**
     * 已知答案向量：全 0 密钥加密全 0 明文，结果应为 4ef997456198dd78
     * （直接测 JCE，不经过 Helper）。
     *
     * @throws Exception JCE 初始化或加解密失败时抛出
     */
    @Test
    public void testKnownAnswerVector() throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
        SecretKeySpec key = new SecretKeySpec(new byte[8], AlgorithmEnum.BLOWFISH.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);

        String ciphertextHex = Hex.encodeHexString(cipher.doFinal(new byte[8]));
        Assert.assertEquals("4ef997456198dd78", ciphertextHex);
    }

    @Test(expected = BlowfishException.class)
    public void testNonHexKeyRejected() throws BlowfishException {
        BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), "这不是Hex密钥");
    }

    @Test(expected = BlowfishException.class)
    public void testWrongKeyPairTypeRejected() throws Exception {
        BlowfishHelper.load(new KeyPair(), "email");
    }

    /**
     * 裸 KeyPair 只设置 secretKey 时，algorithm / algorithmMode / keyBits 必须沿用配置默认值，
     * 不能被 null 覆盖（否则 new SecretKeySpec(bytes, null) 会抛裸 IllegalArgumentException）。
     *
     * @throws BlowfishException 装载或编解码失败时抛出
     */
    @Test
    public void testLoadKeyPairWithOnlySecretKeyUsesDefaults() throws BlowfishException {
        KeyPair keyPair = new KeyPair();
        keyPair.setSecretKey(HEX_KEY);
        BlowfishHelper.load(keyPair);

        Assert.assertEquals(AlgorithmEnum.BLOWFISH.getAlgorithm(), BlowfishHelper.getProperties().getAlgorithm());
        Assert.assertEquals(AlgorithmModeEnum.BLOWFISH.getAlgorithmMode(),
                BlowfishHelper.getProperties().getAlgorithmMode());
        Assert.assertEquals(PLAINTEXT, BlowfishHelper.decode(BlowfishHelper.encode(PLAINTEXT)));
    }

    /**
     * 非法 Base64 密文应包装为 BlowfishException，而不是抛出裸 IllegalArgumentException。
     *
     * @throws BlowfishException 装载配置失败时抛出
     */
    @Test(expected = BlowfishException.class)
    public void testInvalidBase64RejectedAsAlgorithmException() throws BlowfishException {
        loadGlobal();
        BlowfishHelper.decode("not-base64!!");
    }

    /**
     * ECB 密文回归基准：密钥 0123456789abcdef、模式 Blowfish（ECB/PKCS5Padding）、
     * 明文 {@link #PLAINTEXT} 的 Base64 密文固定为 /kkdp6PtHJFP+clisKXtL15agG0RDglnZw5WgxZW0L8=。
     *
     * <p>本库回归基准，非标准向量；用于锁定当前 JCE 行为，防止实现漂移。</p>
     *
     * @throws BlowfishException 装载或加密失败时抛出
     */
    @Test
    public void testEcbCiphertextRegression() throws BlowfishException {
        loadGlobal();
        Assert.assertEquals("/kkdp6PtHJFP+clisKXtL15agG0RDglnZw5WgxZW0L8=", BlowfishHelper.encode(PLAINTEXT));
    }
}
