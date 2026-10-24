package org.tools.encrypt;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * CipherUtils 冒烟测试
 * 覆盖全部公开方法：actuatorCheck / actuator×4（对称无 IV、对称带 IV、非对称加密、非对称解密）
 *
 * @author sander 2026/10/24
 */
public class CipherUtilsTest {

    private static final String PLAINTEXT = "CipherUtils smoke 测试文本";

    @Test
    public void testActuatorCheck() {
        // 合法入参不抛异常
        SecretKey key = new SecretKeySpec(new byte[16], "AES");
        CipherUtils.actuatorCheck("AES", "AES/CBC/PKCS5Padding", key, new byte[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActuatorCheckNullRejected() {
        CipherUtils.actuatorCheck(null, "AES/CBC/PKCS5Padding", new SecretKeySpec(new byte[16], "AES"), new byte[1]);
    }

    @Test
    public void testActuatorSymmetricWithoutIv() throws Exception {
        SecretKey key = new SecretKeySpec(new byte[16], "AES");
        // ECB 模式（"AES" = AES/ECB/PKCS5Padding），无需 IV
        byte[] ciphertext = CipherUtils.actuator("AES", "AES", key, javax.crypto.Cipher.ENCRYPT_MODE, PLAINTEXT.getBytes(StandardCharsets.UTF_8));
        Assert.assertNotEquals(PLAINTEXT, new String(ciphertext));
        byte[] plaintext = CipherUtils.actuator("AES", "AES", key, javax.crypto.Cipher.DECRYPT_MODE, ciphertext);
        Assert.assertEquals(PLAINTEXT, new String(plaintext, StandardCharsets.UTF_8));
    }

    @Test
    public void testActuatorSymmetricWithIv() throws Exception {
        SecretKey key = new SecretKeySpec(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, "AES");
        String iv = "0123456789abcdef";
        byte[] ciphertext = CipherUtils.actuator("AES", "AES/CBC/PKCS5Padding", key, iv, javax.crypto.Cipher.ENCRYPT_MODE, PLAINTEXT.getBytes(StandardCharsets.UTF_8));
        byte[] plaintext = CipherUtils.actuator("AES", "AES/CBC/PKCS5Padding", key, iv, javax.crypto.Cipher.DECRYPT_MODE, ciphertext);
        Assert.assertEquals(PLAINTEXT, new String(plaintext, StandardCharsets.UTF_8));
    }

    @Test
    public void testActuatorAsymmetric() throws Exception {
        RsaKeyGenerator.getKeyPair();
        PublicKey publicKey = RsaKeyGenerator.getPublicKey();
        PrivateKey privateKey = RsaKeyGenerator.getPrivateKey();
        // 2048 位 OAEP：加密单块明文上限 190 字节，解密按密文整块 256 字节分段
        byte[] ciphertext = CipherUtils.actuator("RSA", "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING",
                publicKey, null, PLAINTEXT.getBytes(StandardCharsets.UTF_8), 190);
        String plaintext = CipherUtils.actuator("RSA", "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING",
                privateKey, null, ciphertext, 256);
        Assert.assertEquals(PLAINTEXT, plaintext);
    }

}
