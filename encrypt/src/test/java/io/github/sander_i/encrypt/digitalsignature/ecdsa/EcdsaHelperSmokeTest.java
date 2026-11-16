package io.github.sander_i.encrypt.digitalsignature.ecdsa;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.digitalsignature.Ecdsa;
import io.github.sander_i.encrypt.exception.digitalsignature.ecdsa.EcdsaException;

import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.List;

/**
 * ECDSA 冒烟测试
 *
 * @author sander
 */
public class EcdsaHelperSmokeTest extends TestCase {

    private Ecdsa newConfig() throws EcdsaException {
        java.security.KeyPair keyPair = EcdsaKeyGenerator.getKeyPair();
        Ecdsa ecdsa = new Ecdsa();
        ecdsa.setEnabled(true);
        ecdsa.setPublicKey(EcdsaKeyGenerator.getPublicKeyStr(keyPair.getPublic()));
        ecdsa.setPrivateKey(EcdsaKeyGenerator.getPrivateKeyStr(keyPair.getPrivate()));
        return ecdsa;
    }

    /**
     * 签名 + 验签通过
     */
    public void testSignAndVerify() throws EcdsaException {
        EcdsaHelper.load(newConfig());
        String raw = "Hello ECDSA 中文测试 1234567890";
        String sign = EcdsaHelper.sign(raw);
        assertNotNull(sign);
        assertTrue(EcdsaHelper.verify(raw, sign));
    }

    /**
     * 篡改明文后验签失败
     */
    public void testVerifyFailsOnTamperedData() throws EcdsaException {
        EcdsaHelper.load(newConfig());
        String sign = EcdsaHelper.sign("original");
        assertFalse(EcdsaHelper.verify("tampered", sign));
    }

    /**
     * 篡改签名后验签失败
     * <p>
     * ECDSA 签名是 DER 编码，篡改后可能变成非法编码，
     * 此时抛异常而非返回 false，两者都表示验签未通过。
     * </p>
     */
    public void testVerifyFailsOnTamperedSign() throws EcdsaException {
        EcdsaHelper.load(newConfig());
        String sign = EcdsaHelper.sign("data");
        String bad = sign.substring(0, sign.length() - 4) + "AAAA";
        try {
            assertFalse(EcdsaHelper.verify("data", bad));
        } catch (EcdsaException expected) {
            // 非法 DER 编码，同样表示验签未通过
        }
    }

    /**
     * 用公钥/私钥字符串直接签名验签
     */
    public void testSignVerifyWithKeyStrings() throws EcdsaException {
        Ecdsa ecdsa = newConfig();
        EcdsaHelper.load(ecdsa);

        String raw = "direct key test";
        String sign = EcdsaHelper.sign(ecdsa.getPrivateKey(), raw);
        assertTrue(EcdsaHelper.verify(ecdsa.getPublicKey(), raw, sign));
    }

    /**
     * 不同密钥对不能互相验签
     */
    public void testCrossKeyVerifyFails() throws EcdsaException {
        Ecdsa a = newConfig();
        Ecdsa b = newConfig();

        EcdsaHelper.load(a);
        String sign = EcdsaHelper.sign(a.getPrivateKey(), "cross");

        EcdsaHelper.load(b);
        assertFalse(EcdsaHelper.verify(b.getPublicKey(), "cross", sign));
    }

    /**
     * 三种签名算法均可工作
     */
    public void testAllSignAlgorithms() throws Exception {
        for (String alg : new String[]{"SHA256withECDSA", "SHA384withECDSA", "SHA512withECDSA"}) {
            Ecdsa ecdsa = newConfig();
            ecdsa.setSignAlgorithm(alg);
            EcdsaHelper.load(ecdsa);

            String raw = "alg test " + alg;
            String sign = EcdsaHelper.sign(raw);
            assertTrue(alg, EcdsaHelper.verify(raw, sign));
        }
    }

    /**
     * 不同曲线密钥对可生成并签名
     */
    public void testCurves() throws Exception {
        for (String curve : new String[]{"secp256r1", "secp384r1", "secp521r1"}) {
            KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
            g.initialize(new ECGenParameterSpec(curve));
            java.security.KeyPair kp = g.generateKeyPair();

            Ecdsa ecdsa = new Ecdsa();
            ecdsa.setEnabled(true);
            ecdsa.setPublicKey(EcdsaKeyGenerator.getPublicKeyStr(kp.getPublic()));
            ecdsa.setPrivateKey(EcdsaKeyGenerator.getPrivateKeyStr(kp.getPrivate()));
            ecdsa.setSignAlgorithm("SHA256withECDSA");
            EcdsaHelper.load(ecdsa);

            String raw = "curve " + curve;
            assertTrue(curve, EcdsaHelper.verify(raw, EcdsaHelper.sign(raw)));
        }
    }

    /**
     * 分组配置
     */
    public void testGroup() throws Exception {
        Ecdsa base = newConfig();
        base.setGroupEnabled(true);

        java.security.KeyPair gkp = EcdsaKeyGenerator.getKeyPair();
        KeyPair group = new KeyPair();
        group.setName("email");
        group.setPublicKey(EcdsaKeyGenerator.getPublicKeyStr(gkp.getPublic()));
        group.setPrivateKey(EcdsaKeyGenerator.getPrivateKeyStr(gkp.getPrivate()));
        List<KeyPair> list = new ArrayList<>();
        list.add(group);
        base.setGroup(list);

        EcdsaHelper.load(base, "email");
        String raw = "group sign";
        assertTrue(EcdsaHelper.verify(raw, EcdsaHelper.sign(raw)));
    }

    /**
     * enabled=false 拒绝
     */
    public void testDisabledRejected() throws Exception {
        Ecdsa ecdsa = newConfig();
        ecdsa.setEnabled(false);
        try {
            EcdsaHelper.load(ecdsa, "any");
            fail("expected EcdsaException");
        } catch (EcdsaException expected) {
            // 预期
        }
    }

    /**
     * 空密钥拒绝
     */
    public void testEmptyKeysRejected() throws Exception {
        Ecdsa ecdsa = newConfig();
        ecdsa.setPublicKey("");
        ecdsa.setPrivateKey("");
        try {
            EcdsaHelper.load(ecdsa);
            fail("expected EcdsaException");
        } catch (EcdsaException expected) {
            // 预期
        }
    }

    /**
     * 密钥生成器产出的公钥能还原
     */
    public void testKeyGeneratorRoundTrip() throws Exception {
        java.security.KeyPair kp = EcdsaKeyGenerator.getKeyPair();
        String pub = EcdsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = EcdsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());
        assertNotNull(EcdsaKeyGenerator.getPublicKey(pub));
        assertNotNull(EcdsaKeyGenerator.getPrivateKey(pri));
    }
}
