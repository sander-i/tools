package org.tools.encrypt.symmetrical.rc.rc2;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Rc2;
import org.tools.encrypt.entity.symmetrical.Rc2Properties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc2.Rc2Exception;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Rc2Helper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc2HelperTest {
    /** 32 个 Hex 字符 = 16 字节 = 128 位 RC2 密钥 */
    private static final String HEX_KEY = "0123456789abcdef0123456789abcdef";
    private static final String IV = "12345678";
    private static final String PLAINTEXT = "RC2 smoke 测试文本";

    private static Rc2Properties loadGlobal() throws Rc2Exception {
        Rc2Properties props = new Rc2Properties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY));
        props.setIvParameter(IV);
        Rc2Helper.load(props);
        return props;
    }

    /**
     * ECB 与 CBC 必须使用相同的 effective key bits（密钥长度 × 8）。
     *
     * <p>回归背景：ECB 分支此前不传 {@code RC2ParameterSpec}，SunJCE 默认按 128 位
     * effective bits 处理，而 CBC 分支按密钥长度计算；5 字节密钥下两种模式密文无法互换。
     * 用直接 JCE 按 40 位 effective bits 解密，可验证库内 ECB 路径确实使用了 40 位。</p>
     *
     * @throws Exception 密钥解析或 JCE 操作失败时抛出
     */
    @Test
    public void testEcbUsesKeyLengthAsEffectiveKeyBits() throws Exception {
        // 10 个 Hex 字符 = 5 字节 = 40 位，是 SunJCE RC2 允许的最小密钥
        String shortHexKey = "0123456789";
        Rc2Properties props = new Rc2Properties();
        props.setSecretKeyStr(shortHexKey);
        props.setSecretKey(Rc2KeyGenerator.getSecretKey("RC2", shortHexKey));
        props.setIvParameter(IV);
        Rc2Helper.load(props);

        String ciphertext = Rc2Helper.encode(PLAINTEXT);

        Cipher cipher = Cipher.getInstance("RC2/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(Hex.decodeHex(shortHexKey.toCharArray()), "RC2"),
                new RC2ParameterSpec(40));
        byte[] plain = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        Assert.assertEquals(PLAINTEXT, new String(plain, StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadKeyPair() throws Rc2Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm("RC2");
        keyPair.setAlgorithmMode("RC2");
        keyPair.setKeyBits(128);
        keyPair.setSecretKey(HEX_KEY);
        Rc2Helper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(Rc2Helper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Rc2 rc2 = new Rc2();
        rc2.setEnabled(true);
        rc2.setGroupEnabled(true);
        rc2.setSecretKey(HEX_KEY);
        rc2.setIvParameter(IV);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        email.setIvParameter("87654321");
        groupList.add(email);
        rc2.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Rc2 顶层配置补齐
        Rc2Helper.load(rc2, "email");

        String ciphertext = Rc2Helper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(ciphertext));
        Assert.assertEquals("87654321", Rc2Helper.getProperties().getIvParameter());
    }

    @Test
    public void testLoadProperties() throws Rc2Exception {
        loadGlobal();
        Assert.assertEquals(IV, Rc2Helper.getProperties().getIvParameter());
        Assert.assertNotNull(Rc2Helper.getProperties().getSecretKey());
    }

    /**
     * 只提供 Hex 密钥字符串时应自动解析为密钥对象，并能完成 round-trip。
     *
     * @throws Rc2Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testLoadPropertiesWithSecretKeyStrOnly() throws Rc2Exception {
        Rc2Properties props = new Rc2Properties();
        props.setSecretKeyStr(HEX_KEY);
        Rc2Helper.load(props);

        Assert.assertNotNull(Rc2Helper.getProperties().getSecretKey());
        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(Rc2Helper.encode(PLAINTEXT)));
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws Rc2Exception {
        loadGlobal();
        String ciphertext = Rc2Helper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws Rc2Exception {
        loadGlobal();
        String ciphertext = Rc2Helper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws Rc2Exception {
        loadGlobal();
        SecretKey key = Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY);
        String ciphertext = Rc2Helper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey();
        String hexKey = Rc2KeyGenerator.getSecretKeyStr(key);

        Rc2Properties props = new Rc2Properties();
        props.setSecretKey(key);
        props.setSecretKeyStr(hexKey);
        props.setIvParameter(IV);
        Rc2Helper.load(props);

        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(hexKey, Rc2Helper.encode(PLAINTEXT)));
    }

    /**
     * CBC 模式：IV 为 8 字节，走 {@code RC2/CBC/PKCS5Padding}。
     *
     * @throws Rc2Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testCbcMode() throws Rc2Exception {
        Rc2Properties props = new Rc2Properties();
        props.setAlgorithmMode(AlgorithmModeEnum.RC2_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY));
        props.setSecretKeyStr(HEX_KEY);
        props.setIvParameter(IV);
        Rc2Helper.load(props);

        String ciphertext = Rc2Helper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc2Helper.decode(ciphertext));
    }

    @Test(expected = Rc2Exception.class)
    public void testCbcModeRequiresIv() throws Rc2Exception {
        Rc2Properties props = new Rc2Properties();
        props.setAlgorithmMode(AlgorithmModeEnum.RC2_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY));
        Rc2Helper.load(props);
    }

    @Test(expected = Rc2Exception.class)
    public void testCbcModeRejectsWrongIvLength() throws Rc2Exception {
        Rc2Properties props = new Rc2Properties();
        props.setAlgorithmMode(AlgorithmModeEnum.RC2_CBC_PKCS5PADDING.getAlgorithmMode());
        props.setSecretKey(Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY));
        props.setIvParameter("1234567");
        Rc2Helper.load(props);
    }

    @Test(expected = Rc2Exception.class)
    public void testNonHexKeyRejected() throws Rc2Exception {
        Rc2KeyGenerator.getSecretKey("RC2", "这不是Hex密钥");
    }

    /**
     * 字符集回归：明文与解密输出统一 UTF-8，非 ASCII 字符 round-trip 不乱码。
     *
     * @throws Rc2Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws Rc2Exception {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, Rc2Helper.decode(Rc2Helper.encode(plaintext)));
    }

    /**
     * RFC 2268 已知答案：零密钥、零明文、有效密钥位 63 时，密文应为 ebb773f993278eff。
     * 该用例直接验证 JCE 的 RC2 实现（不经 Helper）。
     *
     * @throws Exception JCE 操作失败时抛出
     */
    @Test
    public void testKnownAnswerRfc2268() throws Exception {
        byte[] zeroKey = new byte[8];
        byte[] zeroPlain = new byte[8];
        Cipher cipher = Cipher.getInstance("RC2/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(zeroKey, "RC2"), new RC2ParameterSpec(63));

        Assert.assertEquals("ebb773f993278eff", Hex.encodeHexString(cipher.doFinal(zeroPlain)));
    }

    /**
     * 不支持的算法模式必须在装载阶段被拒绝，而不是拖到 encode 阶段抛裸 NoSuchElementException。
     *
     * @throws Rc2Exception 装载失败时抛出
     */
    @Test(expected = Rc2Exception.class)
    public void testUnsupportedModeRejected() throws Rc2Exception {
        Rc2Properties props = new Rc2Properties();
        props.setAlgorithmMode("RC2/GCM/NoPadding");
        props.setSecretKey(Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY));
        Rc2Helper.load(props);
    }

    /**
     * 非法 Base64 密文应包装为 Rc2Exception，而不是抛出裸 IllegalArgumentException。
     *
     * @throws Rc2Exception 装载配置失败时抛出
     */
    @Test(expected = Rc2Exception.class)
    public void testInvalidBase64RejectedAsAlgorithmException() throws Rc2Exception {
        loadGlobal();
        Rc2Helper.decode("not-base64!!");
    }
}
