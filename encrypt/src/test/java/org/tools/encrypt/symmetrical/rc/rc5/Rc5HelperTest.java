package org.tools.encrypt.symmetrical.rc.rc5;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Rc5;
import org.tools.encrypt.entity.symmetrical.Rc5Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc5.Rc5Exception;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Rc5Helper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties，
 * 以及 round-trip、确定性、中文 UTF-8、CBC 模式与 IV 校验。
 *
 * <p>RC5 为 BC 私有向量，不编造标准测试向量，只验证自洽性。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc5HelperTest {
    /** 32 个 Hex 字符 = 16 字节 = 128 位 RC5 密钥 */
    private static final String HEX_KEY = "000102030405060708090a0b0c0d0e0f";
    /** RC5 分组长度 64 位，CBC 的 IV 必须为 8 字节 */
    private static final String IV = "12345678";
    private static final String PLAINTEXT = "RC5 smoke 测试文本";

    private static Rc5Properties loadGlobal() throws Rc5Exception {
        Rc5Properties props = new Rc5Properties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY));
        props.setIvParameter(IV);
        Rc5Helper.load(props);
        return props;
    }

    /**
     * 参数常量须与 CipherSupply 中硬编码的 RC5ParameterSpec 参数一致。
     */
    @Test
    public void testParameterConstants() {
        Assert.assertEquals(12, Rc5Helper.ROUNDS);
        Assert.assertEquals(8, Rc5Helper.BLOCK_SIZE);
        Assert.assertEquals(32, Rc5Helper.WORD_SIZE);
    }

    /**
     * 跨实现互操作：本库的密文必须能被直接使用标准参数
     * {@code RC5ParameterSpec(16, 12, 32)} 的 BC Cipher 解开。
     *
     * <p>回归背景：{@code RC5ParameterSpec} 的构造器顺序是
     * {@code (version, rounds, wordSize)}，曾误写成 {@code (12, 8, 32)}，
     * 即 version=12、8 轮，导致密文与标准 12 轮实现不互通。</p>
     *
     * @throws Exception 密钥解析或 JCE 操作失败时抛出
     */
    @Test
    public void testInteropWithStandardBcParameters() throws Exception {
        loadGlobal();
        String ciphertext = Rc5Helper.encode(PLAINTEXT);

        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        Cipher cipher = Cipher.getInstance("RC5", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(Hex.decodeHex(HEX_KEY.toCharArray()), AlgorithmEnum.RC5.getAlgorithm()),
                new RC5ParameterSpec(16, Rc5Helper.ROUNDS, Rc5Helper.WORD_SIZE));
        byte[] plain = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        Assert.assertEquals(PLAINTEXT, new String(plain, StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadKeyPair() throws Rc5Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm(AlgorithmEnum.RC5.getAlgorithm());
        keyPair.setAlgorithmMode(AlgorithmModeEnum.RC5.getAlgorithmMode());
        keyPair.setKeyBits(128);
        keyPair.setSecretKey(HEX_KEY);
        Rc5Helper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(Rc5Helper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Rc5 rc5 = new Rc5();
        rc5.setEnabled(true);
        rc5.setGroupEnabled(true);
        rc5.setSecretKey(HEX_KEY);
        rc5.setIvParameter(IV);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        email.setIvParameter("87654321");
        groupList.add(email);
        rc5.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Rc5 顶层配置补齐
        Rc5Helper.load(rc5, "email");

        String ciphertext = Rc5Helper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(ciphertext));
        Assert.assertEquals("87654321", Rc5Helper.getProperties().getIvParameter());
    }

    @Test
    public void testLoadProperties() throws Rc5Exception {
        loadGlobal();
        Assert.assertEquals(IV, Rc5Helper.getProperties().getIvParameter());
        Assert.assertEquals(AlgorithmEnum.RC5.getAlgorithm(), Rc5Helper.getProperties().getAlgorithm());
    }

    @Test
    public void testGetProperties() throws Rc5Exception {
        Rc5Properties props = loadGlobal();
        Assert.assertSame(props, Rc5Helper.getProperties());
        Assert.assertNotNull(Rc5Helper.getProperties().getSecretKey());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws Rc5Exception {
        loadGlobal();
        String ciphertext = Rc5Helper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws Rc5Exception {
        loadGlobal();
        String ciphertext = Rc5Helper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws Rc5Exception {
        loadGlobal();
        SecretKey key = Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY);
        String ciphertext = Rc5Helper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws Rc5Exception {
        SecretKey key = Rc5KeyGenerator.getSecretKey();
        String hexKey = Rc5KeyGenerator.getSecretKeyStr(key);

        Rc5Properties props = new Rc5Properties();
        props.setSecretKey(key);
        props.setSecretKeyStr(hexKey);
        props.setIvParameter(IV);
        Rc5Helper.load(props);

        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(hexKey, Rc5Helper.encode(PLAINTEXT)));
    }

    /**
     * 确定性：同一密钥、同一模式（ECB 无 IV）两次加密结果必须相同。
     *
     * @throws Rc5Exception 加载密钥或加密失败时抛出
     */
    @Test
    public void testEcbEncryptionIsDeterministic() throws Rc5Exception {
        loadGlobal();
        Assert.assertEquals(Rc5Helper.encode(PLAINTEXT), Rc5Helper.encode(PLAINTEXT));
    }

    /**
     * 字符集回归：明文与解密输出统一 UTF-8，非 ASCII 字符 round-trip 不乱码。
     *
     * @throws Rc5Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws Rc5Exception {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, Rc5Helper.decode(Rc5Helper.encode(plaintext)));
    }

    /**
     * CBC 模式 round-trip（8 字节 IV）。
     *
     * @throws Rc5Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testCbcModeRoundTrip() throws Rc5Exception {
        Rc5Properties props = new Rc5Properties();
        props.setAlgorithmMode(AlgorithmModeEnum.RC5_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY));
        props.setIvParameter(IV);
        Rc5Helper.load(props);

        String ciphertext = Rc5Helper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(ciphertext));
    }

    /**
     * 同一密钥下 ECB 与 CBC 的密文必须不同（CBC 首块受 IV 影响）。
     *
     * @throws Rc5Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testEcbAndCbcCiphertextsDiffer() throws Rc5Exception {
        Rc5Properties ecb = new Rc5Properties();
        ecb.setSecretKeyStr(HEX_KEY);
        ecb.setSecretKey(Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY));
        Rc5Helper.load(ecb);
        String ecbCiphertext = Rc5Helper.encode(PLAINTEXT);

        Rc5Properties cbc = new Rc5Properties();
        cbc.setAlgorithmMode(AlgorithmModeEnum.RC5_CBC_PKCS5PADDING.getAlgorithmMode());
        cbc.setSecretKeyStr(HEX_KEY);
        cbc.setSecretKey(Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY));
        cbc.setIvParameter(IV);
        Rc5Helper.load(cbc);
        String cbcCiphertext = Rc5Helper.encode(PLAINTEXT);

        Assert.assertNotEquals(ecbCiphertext, cbcCiphertext);
        Assert.assertEquals(PLAINTEXT, Rc5Helper.decode(cbcCiphertext));
    }

    @Test(expected = Rc5Exception.class)
    public void testCbcModeRequiresIv() throws Rc5Exception {
        Rc5Properties props = new Rc5Properties();
        props.setAlgorithmMode(AlgorithmModeEnum.RC5_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY));
        Rc5Helper.load(props);
    }

    @Test(expected = Rc5Exception.class)
    public void testCbcModeRejectsWrongIvLength() throws Rc5Exception {
        Rc5Properties props = new Rc5Properties();
        props.setAlgorithmMode(AlgorithmModeEnum.RC5_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), HEX_KEY));
        props.setIvParameter("1234567");
        Rc5Helper.load(props);
    }

    @Test(expected = Rc5Exception.class)
    public void testNonHexKeyRejected() throws Rc5Exception {
        Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), "这不是Hex密钥");
    }

    @Test(expected = Rc5Exception.class)
    public void testWrongKeyPairTypeRejected() throws Exception {
        Rc5Helper.load(new KeyPair(), "email");
    }
}
