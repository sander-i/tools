package org.tools.encrypt.symmetrical.rc.rc4;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Rc4;
import org.tools.encrypt.entity.symmetrical.Rc4Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc4.Rc4Exception;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Rc4Helper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc4HelperTest {
    /** 32 个 Hex 字符 = 16 字节 = 128 位 RC4 密钥（RFC 6229 测试密钥） */
    private static final String HEX_KEY = "0102030405060708090a0b0c0d0e0f10";
    private static final String PLAINTEXT = "RC4 smoke 测试文本";

    private static Rc4Properties loadGlobal() throws Rc4Exception {
        Rc4Properties props = new Rc4Properties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), HEX_KEY));
        Rc4Helper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws Rc4Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm(AlgorithmEnum.RC4.getAlgorithm());
        keyPair.setAlgorithmMode(AlgorithmModeEnum.RC4.getAlgorithmMode());
        keyPair.setKeyBits(128);
        keyPair.setSecretKey(HEX_KEY);
        Rc4Helper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(Rc4Helper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Rc4 rc4 = new Rc4();
        rc4.setEnabled(true);
        rc4.setGroupEnabled(true);
        rc4.setSecretKey(HEX_KEY);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        groupList.add(email);
        rc4.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Rc4 顶层配置补齐
        Rc4Helper.load(rc4, "email");

        String ciphertext = Rc4Helper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(ciphertext));
        Assert.assertEquals(AlgorithmModeEnum.RC4.getAlgorithmMode(),
                Rc4Helper.getProperties().getAlgorithmMode());
    }

    @Test
    public void testLoadProperties() throws Rc4Exception {
        loadGlobal();
        Assert.assertEquals(AlgorithmEnum.RC4.getAlgorithm(), Rc4Helper.getProperties().getAlgorithm());
        Assert.assertEquals(AlgorithmModeEnum.RC4.getAlgorithmMode(),
                Rc4Helper.getProperties().getAlgorithmMode());
        // 流密码不使用 IV
        Assert.assertNull(Rc4Helper.getProperties().getIvParameter());
    }

    /**
     * 仅提供 secretKeyStr 时，load(Rc4Properties) 应自动解析为密钥对象。
     *
     * @throws Rc4Exception 配置或密钥非法时抛出
     */
    @Test
    public void testLoadPropertiesParsesSecretKeyStr() throws Rc4Exception {
        Rc4Properties props = new Rc4Properties();
        props.setSecretKeyStr(HEX_KEY);
        Rc4Helper.load(props);

        Assert.assertNotNull(Rc4Helper.getProperties().getSecretKey());
        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(HEX_KEY, Rc4Helper.encode(PLAINTEXT)));
    }

    @Test
    public void testGetProperties() throws Rc4Exception {
        Rc4Properties props = loadGlobal();
        Assert.assertSame(props, Rc4Helper.getProperties());
        Assert.assertNotNull(Rc4Helper.getProperties().getSecretKey());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws Rc4Exception {
        loadGlobal();
        String ciphertext = Rc4Helper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws Rc4Exception {
        loadGlobal();
        String ciphertext = Rc4Helper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws Rc4Exception {
        loadGlobal();
        SecretKey key = Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), HEX_KEY);
        String ciphertext = Rc4Helper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey();
        String hexKey = Rc4KeyGenerator.getSecretKeyStr(key);

        Rc4Properties props = new Rc4Properties();
        props.setSecretKey(key);
        props.setSecretKeyStr(hexKey);
        Rc4Helper.load(props);

        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(hexKey, Rc4Helper.encode(PLAINTEXT)));
    }

    /**
     * 字符集回归：含中文的明文必须按 UTF-8 round-trip。
     *
     * @throws Rc4Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws Rc4Exception {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, Rc4Helper.decode(Rc4Helper.encode(plaintext)));
    }

    /**
     * 流密码特征：Base64 解码后的密文字节数恒等于明文的 UTF-8 字节数。
     *
     * @throws Rc4Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testCiphertextLengthEqualsPlaintextLength() throws Rc4Exception {
        loadGlobal();
        byte[] ciphertext = Base64.getDecoder().decode(Rc4Helper.encode(PLAINTEXT));
        Assert.assertEquals(PLAINTEXT.getBytes(StandardCharsets.UTF_8).length, ciphertext.length);
    }

    /**
     * 流密码特征：无 IV、无随机源，同一密钥加密同一明文必须得到相同密文。
     *
     * @throws Rc4Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testCiphertextIsDeterministic() throws Rc4Exception {
        loadGlobal();
        Assert.assertEquals(Rc4Helper.encode(HEX_KEY, PLAINTEXT), Rc4Helper.encode(HEX_KEY, PLAINTEXT));
    }

    /**
     * RFC 6229 已知答案（128 位密钥 0102..0f10，明文为 16 个 0x00 字节，
     * 密文前 16 字节应为 9ac7cc9a609d1ef7b2932899cde41b97）。
     * 该用例直接验证 JCE 的 RC4 实现（不经 Helper）。
     *
     * @throws Exception JCE 初始化或加解密失败时抛出
     */
    @Test
    public void testKnownAnswerRfc6229() throws Exception {
        byte[] key = Hex.decodeHex(HEX_KEY.toCharArray());
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AlgorithmEnum.RC4.getAlgorithm()));

        Assert.assertEquals("9ac7cc9a609d1ef7b2932899cde41b97", Hex.encodeHexString(cipher.doFinal(new byte[16])));
    }

    /**
     * RFC 6229 已知答案经 Helper 端到端验证（16 个 NUL 字符的 UTF-8 编码即 16 个 0x00 字节）。
     *
     * @throws Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testKnownAnswerThroughHelper() throws Exception {
        loadGlobal();
        String ciphertext = Rc4Helper.encode(HEX_KEY, new String(new char[16]));
        Assert.assertEquals("9ac7cc9a609d1ef7b2932899cde41b97",
                Hex.encodeHexString(Base64.getDecoder().decode(ciphertext)));
    }

    @Test(expected = Rc4Exception.class)
    public void testNonHexKeyRejected() throws Rc4Exception {
        Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), "这不是Hex密钥");
    }

    @Test(expected = Rc4Exception.class)
    public void testWrongKeyPairTypeRejected() throws Exception {
        Rc4Helper.load(new KeyPair(), "email");
    }

    /**
     * 裸 KeyPair 只设置 secretKey 时，algorithm / algorithmMode 必须沿用配置默认值。
     *
     * @throws Rc4Exception 装载或编解码失败时抛出
     */
    @Test
    public void testLoadKeyPairWithOnlySecretKeyUsesDefaults() throws Rc4Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setSecretKey(HEX_KEY);
        Rc4Helper.load(keyPair);

        Assert.assertEquals(AlgorithmEnum.RC4.getAlgorithm(), Rc4Helper.getProperties().getAlgorithm());
        Assert.assertEquals(AlgorithmModeEnum.RC4.getAlgorithmMode(),
                Rc4Helper.getProperties().getAlgorithmMode());
        Assert.assertEquals(PLAINTEXT, Rc4Helper.decode(Rc4Helper.encode(PLAINTEXT)));
    }

    /**
     * RC4 只支持 {@code RC4} 一种模式，其它模式必须在装载阶段被拒绝。
     *
     * @throws Rc4Exception 装载失败时抛出
     */
    @Test(expected = Rc4Exception.class)
    public void testUnsupportedModeRejected() throws Rc4Exception {
        Rc4Properties props = new Rc4Properties();
        props.setAlgorithmMode("RC4/CBC/NoPadding");
        props.setSecretKey(Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), HEX_KEY));
        Rc4Helper.load(props);
    }

    /**
     * 非法 Base64 密文应包装为 Rc4Exception，而不是抛出裸 IllegalArgumentException。
     *
     * @throws Rc4Exception 装载配置失败时抛出
     */
    @Test(expected = Rc4Exception.class)
    public void testInvalidBase64RejectedAsAlgorithmException() throws Rc4Exception {
        loadGlobal();
        Rc4Helper.decode("not-base64!!");
    }
}
