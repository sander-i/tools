package org.tools.encrypt.symmetrical.des.des3;

import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Des3;
import org.tools.encrypt.entity.symmetrical.Des3Properties;
import org.tools.encrypt.exception.symmetrical.des3.Des3Exception;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Des3Helper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander
 * @since 2.0.1
 */
public class Des3HelperTest {
    /** 48 个 Hex 字符 = 24 字节 = 三密钥 3DES */
    private static final String HEX_KEY = "0123456789abcdeffedcba987654321089abcdef01234567";
    private static final String IV = "12345678";
    private static final String PLAINTEXT = "3DES smoke 测试文本";

    private static Des3Properties loadGlobal() throws Des3Exception {
        Des3Properties props = new Des3Properties();
        props.setSecretKeyStr(HEX_KEY);
        props.setSecretKey(Des3KeyGenerator.getSecretKey("DESede", HEX_KEY));
        props.setIvParameter(IV);
        Des3Helper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws Des3Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm("DESede");
        keyPair.setAlgorithmMode("DESede/CBC/PKCS5Padding");
        keyPair.setSecretKey(HEX_KEY);
        keyPair.setIvParameter(IV);
        Des3Helper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(Des3Helper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        Des3 des3 = new Des3();
        des3.setEnabled(true);
        des3.setGroupEnabled(true);
        des3.setSecretKey(HEX_KEY);
        des3.setIvParameter(IV);

        List<KeyPair> groupList = new ArrayList<>();
        KeyPair email = new KeyPair();
        email.setName("email");
        email.setSecretKey(HEX_KEY);
        email.setIvParameter("87654321");
        groupList.add(email);
        des3.setGroup(groupList);

        // 分组加载：缺省的 algorithm / algorithmMode 由 Des3 顶层配置补齐
        Des3Helper.load(des3, "email");

        String ciphertext = Des3Helper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(ciphertext));
        Assert.assertEquals("87654321", Des3Helper.getProperties().getIvParameter());
    }

    @Test
    public void testLoadProperties() throws Des3Exception {
        loadGlobal();
        Assert.assertEquals(IV, Des3Helper.getProperties().getIvParameter());
        Assert.assertEquals("DESede", Des3Helper.getProperties().getAlgorithm());
        Assert.assertEquals("DESede/CBC/PKCS5Padding", Des3Helper.getProperties().getAlgorithmMode());
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws Des3Exception {
        loadGlobal();
        String ciphertext = Des3Helper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithHexKey() throws Des3Exception {
        loadGlobal();
        String ciphertext = Des3Helper.encode(HEX_KEY, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(HEX_KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws Des3Exception {
        loadGlobal();
        SecretKey key = Des3KeyGenerator.getSecretKey("DESede", HEX_KEY);
        String ciphertext = Des3Helper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(key, ciphertext));
    }

    @Test
    public void testGeneratedKeyRoundTrip() throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey();
        String hexKey = Des3KeyGenerator.getSecretKeyStr(key);

        Des3Properties props = new Des3Properties();
        props.setSecretKey(key);
        props.setIvParameter(IV);
        Des3Helper.load(props);

        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(hexKey, Des3Helper.encode(PLAINTEXT)));
    }

    /**
     * 中文明文按 UTF-8 编解码，round-trip 不得乱码。
     *
     * @throws Des3Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testChineseRoundTripUsesUtf8Consistently() throws Des3Exception {
        loadGlobal();
        String plaintext = "中文明文 round-trip";
        Assert.assertEquals(plaintext, Des3Helper.decode(Des3Helper.encode(plaintext)));
    }

    /**
     * CBC 模式下 IV 为空必须拒绝装载。
     */
    @Test(expected = Des3Exception.class)
    public void testCbcModeRequiresIv() throws Des3Exception {
        Des3Properties props = new Des3Properties();
        props.setSecretKey(Des3KeyGenerator.getSecretKey("DESede", HEX_KEY));
        Des3Helper.load(props);
    }

    /**
     * CBC 模式下 IV 按 UTF-8 编码必须恰好 8 字节。
     */
    @Test
    public void testCbcModeRequiresEightByteIv() throws Des3Exception {
        Des3Properties props = new Des3Properties();
        props.setSecretKey(Des3KeyGenerator.getSecretKey("DESede", HEX_KEY));
        props.setIvParameter("1234567");
        try {
            Des3Helper.load(props);
            Assert.fail("7 字节 IV 应被拒绝");
        } catch (Des3Exception e) {
            Assert.assertTrue(e.getMessage().contains("8 字节"));
        }

        props.setIvParameter("密码密码");
        try {
            Des3Helper.load(props);
            Assert.fail("12 字节（UTF-8 中文）IV 应被拒绝");
        } catch (Des3Exception e) {
            Assert.assertTrue(e.getMessage().contains("8 字节"));
        }
    }

    /**
     * 非 {@code DESede/CBC/PKCS5Padding} 的算法模式必须拒绝装载。
     *
     * <p>原实现只判断模式名是否含 {@code /CBC/}，非法模式会装载成功，
     * 直到 {@code encode} 阶段才因枚举查找失败抛出 {@code NoSuchElementException}。</p>
     */
    @Test
    public void testUnsupportedAlgorithmModeRejected() throws Des3Exception {
        Des3Properties props = new Des3Properties();
        props.setAlgorithmMode("DESede");
        props.setSecretKey(Des3KeyGenerator.getSecretKey("DESede", HEX_KEY));
        props.setIvParameter(IV);
        try {
            Des3Helper.load(props);
            Assert.fail("非法算法模式应被拒绝");
        } catch (Des3Exception e) {
            Assert.assertTrue(e.getMessage().contains("DESede/CBC/PKCS5Padding"));
        }
    }

    /**
     * 只提供 Hex 密钥字符串时应自动解析为密钥对象，并能完成 round-trip。
     *
     * @throws Des3Exception 加载密钥或编解码失败时抛出
     */
    @Test
    public void testLoadPropertiesWithSecretKeyStrOnly() throws Des3Exception {
        Des3Properties props = new Des3Properties();
        props.setSecretKeyStr(HEX_KEY);
        props.setIvParameter(IV);
        Des3Helper.load(props);

        Assert.assertNotNull(Des3Helper.getProperties().getSecretKey());
        Assert.assertEquals(PLAINTEXT, Des3Helper.decode(Des3Helper.encode(PLAINTEXT)));
    }

    /**
     * 非 Hex 密钥字符串必须拒绝。
     */
    @Test(expected = Des3Exception.class)
    public void testNonHexKeyRejected() throws Des3Exception {
        Des3KeyGenerator.getSecretKey("DESede", "这不是Hex密钥");
    }

    /**
     * 已知答案：直接使用 JCE 的 DESede/ECB/NoPadding，key 为 24 字节三密钥 3DES，
     * 明文 1234567890abcdef 的密文应为 8eaeb1ea7f2755a5。本用例不经 Helper。
     *
     * @throws Exception 编解码或 JCE 操作失败时抛出
     */
    @Test
    public void testKnownAnswerWithJce() throws Exception {
        byte[] keyBytes = Hex.decodeHex(HEX_KEY.toCharArray());
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] ciphertext = cipher.doFinal(Hex.decodeHex("1234567890abcdef".toCharArray()));
        Assert.assertEquals("8eaeb1ea7f2755a5", Hex.encodeHexString(ciphertext));
        Assert.assertEquals(24, keyBytes.length);
    }

    /**
     * CBC 密文回归基准：固定 48 Hex 密钥、IV 12345678、模式 DESede/CBC/PKCS5Padding、
     * 明文 {@link #PLAINTEXT} 的 Base64 密文固定为 XNo1kE0TQ5iH7NZnBLvMMQsfl0dGBqlm。
     *
     * <p>本库回归基准，非标准向量；用于锁定当前 JCE 行为，防止实现漂移。</p>
     *
     * @throws Des3Exception 装载或加密失败时抛出
     */
    @Test
    public void testCbcCiphertextRegression() throws Des3Exception {
        loadGlobal();
        Assert.assertEquals("XNo1kE0TQ5iH7NZnBLvMMQsfl0dGBqlm", Des3Helper.encode(PLAINTEXT));
    }
}
