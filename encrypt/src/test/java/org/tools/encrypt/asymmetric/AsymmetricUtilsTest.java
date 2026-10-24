package org.tools.encrypt.asymmetric;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;
import org.tools.encrypt.exception.EncryptException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * AsymmetricUtils 冒烟测试
 * 覆盖全部公开方法：encode / decode（显式传参，不依赖全局配置）
 *
 * @author sander 2026/10/24
 */
public class AsymmetricUtilsTest {

    private static final String ALGORITHM = "RSA";
    private static final String MODE = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    private static final String PLAINTEXT = "AsymmetricUtils smoke 测试文本";
    /** 2048 位密钥：OAEP 加密单块明文上限 190 字节，解密按密文整块 256 字节分段 */
    private static final int MAX_ENCRYPT_BLOCK = 190;
    private static final int MAX_DECRYPT_BLOCK = 256;

    @Test
    public void testEncodeDecodeRoundTrip() throws EncryptException, Exception {
        RsaKeyGenerator.getKeyPair();
        PublicKey publicKey = RsaKeyGenerator.getPublicKey();
        PrivateKey privateKey = RsaKeyGenerator.getPrivateKey();

        String ciphertext = AsymmetricUtils.encode(ALGORITHM, MODE, publicKey, null, PLAINTEXT, MAX_ENCRYPT_BLOCK);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, AsymmetricUtils.decode(ALGORITHM, MODE, privateKey, null, ciphertext, MAX_DECRYPT_BLOCK));
    }

}
