package org.tools.encrypt.digitalsignature;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;

import java.security.KeyPair;
import java.security.Signature;

/**
 * SignatureUtils 冒烟测试
 * 覆盖全部公开方法：sign×2 / verify×2（并发场景见 SignatureUtilsConcurrencyTest）
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class SignatureUtilsTest {
    private static final String PLAINTEXT = "signature smoke 测试文本";

    @Test
    public void testSignAndVerify() throws Exception {
        KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        String sign = SignatureUtils.sign("SHA256withRSA", keyPair.getPrivate(), PLAINTEXT);
        Assert.assertTrue(SignatureUtils.verify("SHA256withRSA", keyPair.getPublic(), PLAINTEXT, sign));
        Assert.assertFalse(SignatureUtils.verify("SHA256withRSA", keyPair.getPublic(), PLAINTEXT + "x", sign));
    }

    @Test
    public void testSignAndVerifyWithSignatureInstance() throws Exception {
        KeyPair keyPair = RsaKeyGenerator.getKeyPair();

        Signature signSignature = Signature.getInstance("SHA256withRSA");
        signSignature.initSign(keyPair.getPrivate());
        String sign = SignatureUtils.sign(signSignature, PLAINTEXT);

        Signature verifySignature = Signature.getInstance("SHA256withRSA");
        verifySignature.initVerify(keyPair.getPublic());
        Assert.assertTrue(SignatureUtils.verify(verifySignature, PLAINTEXT, sign));
    }
}
