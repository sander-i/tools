package io.github.sander_i.encrypt;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.exception.EncryptException;
import io.github.sander_i.encrypt.symmetrical.aes.AesHelper;
import io.github.sander_i.encrypt.symmetrical.aes.AesKeyGenerator;
import io.github.sander_i.encrypt.symmetrical.blowfish.BlowfishHelper;
import io.github.sander_i.encrypt.symmetrical.blowfish.BlowfishKeyGenerator;
import io.github.sander_i.encrypt.symmetrical.des.des3.Des3Helper;
import io.github.sander_i.encrypt.symmetrical.des.des3.Des3KeyGenerator;
import io.github.sander_i.encrypt.symmetrical.rc.rc2.Rc2Helper;
import io.github.sander_i.encrypt.symmetrical.rc.rc2.Rc2KeyGenerator;
import io.github.sander_i.encrypt.symmetrical.rc.rc4.Rc4Helper;
import io.github.sander_i.encrypt.symmetrical.rc.rc4.Rc4KeyGenerator;

import javax.crypto.SecretKey;
import java.util.Arrays;

/**
 * 密钥字符串往返测试
 *
 * <p>
 * 验证「密钥对象 → 字符串 → 密钥对象」往返后字节完全一致，
 * 且往返后的密钥能正常加解密。这是对称密钥持久化到配置文件的正确性前提。
 * </p>
 *
 * @author sander
 */
public class SecretKeyCodecSmokeTest extends TestCase {

    /**
     * 带 hex: 前缀的编码，解码后字节完全一致
     */
    public void testEncodeDecodeRoundTrip() {
        byte[] raw = new byte[]{0x00, 0x01, 0x7f, (byte) 0x80, (byte) 0xff, 0x0a, 0x10, 0x20,
                0x30, 0x40, 0x50, 0x60, 0x70, 0x11, 0x22, 0x33};
        SecretKey key = SecretKeyCodec.decode("AES", SecretKeyCodec.HEX_PREFIX + hex(raw));

        String encoded = SecretKeyCodec.encode(key);
        assertTrue(encoded.startsWith(SecretKeyCodec.HEX_PREFIX));

        SecretKey back = SecretKeyCodec.decode("AES", encoded);
        assertTrue("往返后字节应一致", Arrays.equals(key.getEncoded(), back.getEncoded()));
        assertTrue(Arrays.equals(raw, back.getEncoded()));
    }

    /**
     * 明文密钥（无前缀）按 UTF-8 取字节，保持向后兼容
     */
    public void testPlainTextKeyUnchanged() {
        String plain = "BeYourself";
        SecretKey key = SecretKeyCodec.decode("AES", plain);
        assertTrue(Arrays.equals(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                key.getEncoded()));
    }

    /**
     * 纯十六进制字符的明文密钥不被误判为编码值
     * <p>
     * 这是加前缀的核心原因：{@code "abcdef12"} 既是合法明文，也是合法 Hex。
     * </p>
     */
    public void testHexLookalikePlainTextNotMisinterpreted() {
        String lookalike = "abcdef12";
        assertFalse(SecretKeyCodec.isEncoded(lookalike));
        SecretKey key = SecretKeyCodec.decode("AES", lookalike);
        // 按明文处理，8 字节
        assertEquals(8, key.getEncoded().length);
        assertTrue(Arrays.equals(lookalike.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                key.getEncoded()));
    }

    /**
     * 非法十六进制内容抛 EncryptException
     */
    public void testInvalidHexRejected() {
        try {
            SecretKeyCodec.decode("AES", SecretKeyCodec.HEX_PREFIX + "zzzz");
            fail("expected EncryptException");
        } catch (EncryptException expected) {
            // 预期
        }
    }

    /**
     * 空密钥拒绝
     */
    public void testEmptyRejected() {
        try {
            SecretKeyCodec.decode("AES", "");
            fail("expected EncryptException");
        } catch (EncryptException expected) {
            // 预期
        }
    }

    /**
     * null 编码返回 null
     */
    public void testEncodeNull() {
        assertNull(SecretKeyCodec.encode(null));
    }

    /**
     * AES：生成 → 转字符串 → 转回 → 加解密可用
     */
    public void testAesKeyRoundTripUsable() throws Exception {
        SecretKey original = AesKeyGenerator.getSecretKey();
        String encoded = AesKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = AesKeyGenerator.getSecretKey("AES", encoded);

        assertTrue("AES 密钥往返后应一致",
                Arrays.equals(original.getEncoded(), restored.getEncoded()));

        io.github.sander_i.encrypt.entity.symmetrical.Aes cfg =
                new io.github.sander_i.encrypt.entity.symmetrical.Aes();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter("1234567812345678");
        AesHelper.load(cfg);

        String raw = "AES 密钥往返测试 中文";
        assertEquals(raw, AesHelper.decode(AesHelper.encode(raw)));
        assertEquals(encoded, AesHelper.getProperties().getSecretKeyStr());
    }

    /**
     * 3DES：密钥往返后加解密可用
     */
    public void testDes3KeyRoundTripUsable() throws Exception {
        SecretKey original = Des3KeyGenerator.getSecretKey();
        String encoded = Des3KeyGenerator.getSecretKeyStr(original);
        SecretKey restored = Des3KeyGenerator.getSecretKey("DESede", encoded);

        assertTrue(Arrays.equals(original.getEncoded(), restored.getEncoded()));

        io.github.sander_i.encrypt.entity.symmetrical.Des3 cfg =
                new io.github.sander_i.encrypt.entity.symmetrical.Des3();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter("12345678");
        Des3Helper.load(cfg);

        String raw = "3DES 密钥往返测试 中文";
        assertEquals(raw, Des3Helper.decode(Des3Helper.encode(raw)));
        assertEquals(encoded, Des3Helper.getProperties().getSecretKeyStr());
    }

    /**
     * Blowfish：密钥往返后加解密可用
     */
    public void testBlowfishKeyRoundTripUsable() throws Exception {
        SecretKey original = BlowfishKeyGenerator.getSecretKey();
        String encoded = BlowfishKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = BlowfishKeyGenerator.getSecretKey("Blowfish", encoded);

        assertTrue(Arrays.equals(original.getEncoded(), restored.getEncoded()));

        io.github.sander_i.encrypt.entity.symmetrical.Blowfish cfg =
                new io.github.sander_i.encrypt.entity.symmetrical.Blowfish();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter("12345678");
        BlowfishHelper.load(cfg);

        String raw = "Blowfish 密钥往返测试 中文";
        assertEquals(raw, BlowfishHelper.decode(BlowfishHelper.encode(raw)));
    }

    /**
     * RC2：密钥往返后加解密可用
     */
    public void testRc2KeyRoundTripUsable() throws Exception {
        SecretKey original = Rc2KeyGenerator.getSecretKey();
        String encoded = Rc2KeyGenerator.getSecretKeyStr(original);
        SecretKey restored = Rc2KeyGenerator.getSecretKey("RC2", encoded);

        assertTrue(Arrays.equals(original.getEncoded(), restored.getEncoded()));

        io.github.sander_i.encrypt.entity.symmetrical.Rc2 cfg =
                new io.github.sander_i.encrypt.entity.symmetrical.Rc2();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter("12345678");
        Rc2Helper.load(cfg);

        String raw = "RC2 密钥往返测试 中文";
        assertEquals(raw, Rc2Helper.decode(Rc2Helper.encode(raw)));
    }

    /**
     * RC4：密钥往返后加解密可用
     */
    public void testRc4KeyRoundTripUsable() throws Exception {
        SecretKey original = Rc4KeyGenerator.getSecretKey();
        String encoded = Rc4KeyGenerator.getSecretKeyStr(original);
        SecretKey restored = Rc4KeyGenerator.getSecretKey("RC4", encoded);

        assertTrue(Arrays.equals(original.getEncoded(), restored.getEncoded()));

        io.github.sander_i.encrypt.entity.symmetrical.Rc4 cfg =
                new io.github.sander_i.encrypt.entity.symmetrical.Rc4();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        Rc4Helper.load(cfg);

        String raw = "RC4 密钥往返测试 中文";
        assertEquals(raw, Rc4Helper.decode(Rc4Helper.encode(raw)));
    }

    /**
     * 明文密钥仍可用于加解密（向后兼容）
     */
    public void testPlainTextKeyStillWorks() throws Exception {
        io.github.sander_i.encrypt.entity.symmetrical.Des3 cfg =
                new io.github.sander_i.encrypt.entity.symmetrical.Des3();
        cfg.setEnabled(true);
        cfg.setSecretKey("123456789012345678901234");
        cfg.setIvParameter("12345678");
        Des3Helper.load(cfg);

        String raw = "plain key 兼容测试";
        assertEquals(raw, Des3Helper.decode(Des3Helper.encode(raw)));
    }

    private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
