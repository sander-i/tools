package io.github.sander_i.encrypt.digitalsignature.dsa;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.digitalsignature.DsaProperties;
import io.github.sander_i.encrypt.enums.SignAlgorithmEnum;
import io.github.sander_i.encrypt.exception.EncryptException;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * DSA 签名测试
 *
 * <p>
 * 覆盖三条加载路径：DsaProperties、项目自有 KeyPair、以及直接用密钥对象签名验签。
 * KeyPair 路径此前完全没有测试覆盖，且实际不可用（只设了字符串字段，
 * 校验的却是密钥对象字段），本类补上回归用例。
 * </p>
 *
 * @author sander 2024/04/14
 */
public class DsaHelperTest extends TestCase {

    public void testSign() throws EncryptException, NoSuchAlgorithmException {
        java.security.KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();

        DsaProperties properties = new DsaProperties();
        properties.setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());
        properties.setPrivateKey(aPrivate);
        properties.setPublicKey(aPublic);
        DsaHelper.load(properties);

        String raw = "1234567890";
        String sign = DsaHelper.sign(aPrivate, raw);
        assertNotNull("签名不应为空", sign);
        assertTrue("应能通过验签", DsaHelper.verify(aPublic, raw, sign));
    }

    public void testVerify() throws EncryptException, NoSuchAlgorithmException {
        java.security.KeyPair keyPair = DsaKeyGenerator.getKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();

        DsaProperties properties = new DsaProperties();
        properties.setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());
        properties.setPrivateKey(aPrivate);
        properties.setPublicKey(aPublic);
        DsaHelper.load(properties);

        String raw = "1234567890";
        String sign = DsaHelper.sign(aPrivate, raw);
        assertTrue(DsaHelper.verify(aPublic, raw, sign));
        assertFalse("篡改明文后验签应失败", DsaHelper.verify(aPublic, raw + "x", sign));
    }

    /**
     * 回归：load(KeyPair) 曾因未解析密钥对象而必然失败
     */
    public void testLoadKeyPair() throws Exception {
        java.security.KeyPair kp = DsaKeyGenerator.getKeyPair();

        KeyPair cfg = new KeyPair();
        cfg.setPublicKey(DsaKeyGenerator.getPublicKeyStr(kp.getPublic()));
        cfg.setPrivateKey(DsaKeyGenerator.getPrivateKeyStr(kp.getPrivate()));
        cfg.setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());

        DsaHelper.load(cfg);

        String raw = "KeyPair 加载路径 中文";
        String sign = DsaHelper.sign(raw);
        assertNotNull(sign);
        assertTrue("加载后应能签名并验签", DsaHelper.verify(raw, sign));
    }

    /**
     * 用密钥字符串直接签名验签
     */
    public void testSignVerifyWithKeyStrings() throws Exception {
        java.security.KeyPair kp = DsaKeyGenerator.getKeyPair();
        String pub = DsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = DsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());

        String raw = "字符串密钥测试";
        String sign = DsaHelper.sign(pri, raw);
        assertTrue(DsaHelper.verify(pub, raw, sign));
        assertFalse(DsaHelper.verify(pub, raw + "x", sign));
    }

    /**
     * 空配置应拒绝
     */
    public void testLoadRejectsEmpty() {
        try {
            DsaHelper.load((DsaProperties) null);
            fail("expected EncryptException");
        } catch (Exception expected) {
            // 预期
        }
    }
}
