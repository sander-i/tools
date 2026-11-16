package io.github.sander_i.encrypt.asymmetric.rsa;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.exception.EncryptException;

import java.security.KeyPair;

/**
 * RSA 冒烟测试
 *
 * <p>
 * 密钥对在运行时生成，不硬编码任何密钥，
 * 避免把真实密钥写进代码库。
 * </p>
 *
 * @author sander 2024/03/30
 */
public class RsaHelperTest extends TestCase {

    public void testEncode() throws EncryptException {
        KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        String publicKey = RsaKeyGenerator.getPublicKeyStr(keyPair.getPublic());
        String privateKey = RsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate());

        String raw = "1234567890";
        String encode = RsaHelper.encode(publicKey, raw);
        assertNotNull(encode);
        assertEquals(raw, RsaHelper.decode(privateKey, encode));
    }

    public void testDecode() throws EncryptException {
        KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        String publicKey = RsaKeyGenerator.getPublicKeyStr(keyPair.getPublic());
        String privateKey = RsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate());

        String raw = "RSA 解密测试 中文";
        String encode = RsaHelper.encode(publicKey, raw);
        assertEquals(raw, RsaHelper.decode(privateKey, encode));
    }

    public void testSign() throws EncryptException {
        KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        String privateKey = RsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate());

        String raw = "1234567890";
        String sign = RsaHelper.sign(privateKey, raw);
        assertNotNull(sign);
    }

    public void testVerify() throws EncryptException {
        KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        String publicKey = RsaKeyGenerator.getPublicKeyStr(keyPair.getPublic());
        String privateKey = RsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate());

        String raw = "1234567890";
        String sign = RsaHelper.sign(privateKey, raw);

        assertTrue(RsaHelper.verify(publicKey, raw, sign));
        assertFalse("篡改明文后应验签失败", RsaHelper.verify(publicKey, raw + "x", sign));
    }
}
