package io.github.sander_i.encrypt;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.asymmetric.dh.DhHelper;
import io.github.sander_i.encrypt.asymmetric.ecc.EccKeyGenerator;
import io.github.sander_i.encrypt.asymmetric.ecc.EcdhHelper;
import io.github.sander_i.encrypt.asymmetric.rsa.RsaHelper;
import io.github.sander_i.encrypt.asymmetric.rsa.RsaKeyGenerator;
import io.github.sander_i.encrypt.basic.base.Base64Utils;
import io.github.sander_i.encrypt.basic.crc32.Crc32Utils;
import io.github.sander_i.encrypt.basic.md5.MD5Utils;
import io.github.sander_i.encrypt.basic.sha.ShaUtils;
import io.github.sander_i.encrypt.digitalsignature.dsa.DsaHelper;
import io.github.sander_i.encrypt.digitalsignature.dsa.DsaKeyGenerator;
import io.github.sander_i.encrypt.digitalsignature.ecdsa.EcdsaHelper;
import io.github.sander_i.encrypt.digitalsignature.ecdsa.EcdsaKeyGenerator;
import io.github.sander_i.encrypt.entity.symmetrical.Aes;
import io.github.sander_i.encrypt.entity.symmetrical.Blowfish;
import io.github.sander_i.encrypt.entity.symmetrical.Des;
import io.github.sander_i.encrypt.entity.symmetrical.Des3;
import io.github.sander_i.encrypt.entity.symmetrical.Idea;
import io.github.sander_i.encrypt.entity.symmetrical.Rc2;
import io.github.sander_i.encrypt.entity.symmetrical.Rc4;
import io.github.sander_i.encrypt.symmetrical.aes.AesHelper;
import io.github.sander_i.encrypt.symmetrical.aes.AesKeyGenerator;
import io.github.sander_i.encrypt.symmetrical.blowfish.BlowfishHelper;
import io.github.sander_i.encrypt.symmetrical.blowfish.BlowfishKeyGenerator;
import io.github.sander_i.encrypt.symmetrical.des.DesHelper;
import io.github.sander_i.encrypt.symmetrical.des.DesKeyGenerator;
import io.github.sander_i.encrypt.symmetrical.des.des3.Des3Helper;
import io.github.sander_i.encrypt.symmetrical.des.des3.Des3KeyGenerator;
import io.github.sander_i.encrypt.symmetrical.idea.IdeaHelper;
import io.github.sander_i.encrypt.symmetrical.idea.IdeaKeyGenerator;
import io.github.sander_i.encrypt.symmetrical.rc.rc2.Rc2Helper;
import io.github.sander_i.encrypt.symmetrical.rc.rc2.Rc2KeyGenerator;
import io.github.sander_i.encrypt.symmetrical.rc.rc4.Rc4Helper;
import io.github.sander_i.encrypt.symmetrical.rc.rc4.Rc4KeyGenerator;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Arrays;

/**
 * 全量加解密与密钥往返验证
 *
 * <p>
 * 覆盖 encrypt 模块全部算法族：
 * 哈希（MD5/SHA 系列）、编码（Base64）、校验（CRC32）、
 * 对称加密（AES/DES/IDEA/3DES/Blowfish/RC2/RC4）、
 * 非对称加密（RSA）、密钥协商（ECDH/DH）、数字签名（DSA/ECDSA）。
 * </p>
 *
 * <p>
 * 每个对称算法都验证两件事：
 * 一是加解密往返正确（含中文），
 * 二是「密钥对象 → 字符串 → 密钥对象」往返后字节一致且仍可用于加解密。
 * </p>
 *
 * @author sander
 */
public class FullEncryptionSmokeTest extends TestCase {

    /**
     * 覆盖 ASCII、中文、数字、符号的测试明文
     */
    private static final String PLAIN = "Hello 加密测试 1234567890 !@#$%^&*()_+-=[]{}|;':\",./<>?";

    private static final String IV8 = "12345678";
    private static final String IV16 = "1234567812345678";

    // ==================== 哈希 ====================

    public void testMd5() throws Exception {
        String md5 = MD5Utils.encode(PLAIN);
        assertNotNull(md5);
        assertEquals(32, md5.length());
        // 同输入同输出
        assertEquals(md5, MD5Utils.encode(PLAIN));
        // 不同输入不同输出
        assertFalse(md5.equals(MD5Utils.encode(PLAIN + "x")));
    }

    public void testShaAll() throws Exception {
        // 对照 NIST 标准值
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", ShaUtils.encodeSha1("abc"));
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                ShaUtils.encodeSha256("abc"));
        assertEquals(96, ShaUtils.encodeSha384("abc").length());
        assertEquals(128, ShaUtils.encodeSha512("abc").length());
    }

    // ==================== 编码与校验 ====================

    public void testBase64() {
        String encoded = Base64Utils.encode(PLAIN);
        assertNotNull(encoded);
        assertEquals(PLAIN, Base64Utils.decode(encoded));
    }

    public void testCrc32() {
        long a = Crc32Utils.encode(PLAIN.getBytes());
        long b = Crc32Utils.encode(PLAIN.getBytes());
        assertEquals(a, b);
        assertFalse(a == Crc32Utils.encode((PLAIN + "x").getBytes()));
    }

    // ==================== 对称加密：加解密 + 密钥往返 ====================

    public void testAes() throws Exception {
        // 1. 密钥往返
        SecretKey original = AesKeyGenerator.getSecretKey();
        String encoded = AesKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = AesKeyGenerator.getSecretKey("AES", encoded);
        assertTrue("AES 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        // 2. 用往返后的密钥加解密
        Aes cfg = new Aes();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter(IV16);
        AesHelper.load(cfg);

        assertEquals(PLAIN, AesHelper.decode(AesHelper.encode(PLAIN)));
        assertEquals(encoded, AesHelper.getProperties().getSecretKeyStr());
    }

    public void testDes() throws Exception {
        SecretKey original = DesKeyGenerator.getSecretKey();
        String encoded = DesKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = DesKeyGenerator.getSecretKey("DES", encoded);
        assertTrue("DES 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        Des cfg = new Des();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter(IV8);
        DesHelper.load(cfg);

        assertEquals(PLAIN, DesHelper.decode(DesHelper.encode(PLAIN)));
    }

    public void testIdea() throws Exception {
        SecretKey original = IdeaKeyGenerator.getSecretKey();
        String encoded = IdeaKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = IdeaKeyGenerator.getSecretKey("IDEA", encoded);
        assertTrue("IDEA 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        Idea cfg = new Idea();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        IdeaHelper.load(cfg);

        assertEquals(PLAIN, IdeaHelper.decode(IdeaHelper.encode(PLAIN)));
    }

    public void testDes3() throws Exception {
        SecretKey original = Des3KeyGenerator.getSecretKey();
        String encoded = Des3KeyGenerator.getSecretKeyStr(original);
        SecretKey restored = Des3KeyGenerator.getSecretKey("DESede", encoded);
        assertTrue("3DES 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        Des3 cfg = new Des3();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter(IV8);
        Des3Helper.load(cfg);

        assertEquals(PLAIN, Des3Helper.decode(Des3Helper.encode(PLAIN)));
    }

    public void testBlowfish() throws Exception {
        SecretKey original = BlowfishKeyGenerator.getSecretKey();
        String encoded = BlowfishKeyGenerator.getSecretKeyStr(original);
        SecretKey restored = BlowfishKeyGenerator.getSecretKey("Blowfish", encoded);
        assertTrue("Blowfish 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        Blowfish cfg = new Blowfish();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter(IV8);
        BlowfishHelper.load(cfg);

        assertEquals(PLAIN, BlowfishHelper.decode(BlowfishHelper.encode(PLAIN)));
    }

    public void testRc2() throws Exception {
        SecretKey original = Rc2KeyGenerator.getSecretKey();
        String encoded = Rc2KeyGenerator.getSecretKeyStr(original);
        SecretKey restored = Rc2KeyGenerator.getSecretKey("RC2", encoded);
        assertTrue("RC2 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        Rc2 cfg = new Rc2();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        cfg.setIvParameter(IV8);
        Rc2Helper.load(cfg);

        assertEquals(PLAIN, Rc2Helper.decode(Rc2Helper.encode(PLAIN)));
    }

    public void testRc4() throws Exception {
        SecretKey original = Rc4KeyGenerator.getSecretKey();
        String encoded = Rc4KeyGenerator.getSecretKeyStr(original);
        SecretKey restored = Rc4KeyGenerator.getSecretKey("RC4", encoded);
        assertTrue("RC4 密钥往返字节一致", Arrays.equals(original.getEncoded(), restored.getEncoded()));

        Rc4 cfg = new Rc4();
        cfg.setEnabled(true);
        cfg.setSecretKey(encoded);
        Rc4Helper.load(cfg);

        assertEquals(PLAIN, Rc4Helper.decode(Rc4Helper.encode(PLAIN)));
    }

    /**
     * 所有对称算法的密钥字符串都必须带 hex: 前缀，且能还原出原字节
     */
    public void testAllSymmetricKeyEncodingPrefix() throws Exception {
        SecretKey[] keys = {
                AesKeyGenerator.getSecretKey(),
                DesKeyGenerator.getSecretKey(),
                IdeaKeyGenerator.getSecretKey(),
                Des3KeyGenerator.getSecretKey(),
                BlowfishKeyGenerator.getSecretKey(),
                Rc2KeyGenerator.getSecretKey(),
                Rc4KeyGenerator.getSecretKey()
        };
        String[] algorithms = {"AES", "DES", "IDEA", "DESede", "Blowfish", "RC2", "RC4"};

        for (int i = 0; i < keys.length; i++) {
            String str = SecretKeyCodec.encode(keys[i]);
            assertTrue(algorithms[i] + " 编码应带 hex: 前缀", str.startsWith(SecretKeyCodec.HEX_PREFIX));
            SecretKey back = SecretKeyCodec.decode(algorithms[i], str);
            assertTrue(algorithms[i] + " 往返字节一致", Arrays.equals(keys[i].getEncoded(), back.getEncoded()));
        }
    }

    // ==================== 非对称：RSA ====================

    public void testRsaEncryptDecrypt() throws Exception {
        KeyPair kp = RsaKeyGenerator.getKeyPair();
        String pub = RsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = RsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());

        // 密钥字符串往返
        assertEquals(pub, RsaKeyGenerator.getPublicKeyStr(RsaKeyGenerator.getPublicKey(pub)));
        assertEquals(pri, RsaKeyGenerator.getPrivateKeyStr(RsaKeyGenerator.getPrivateKey(pri)));

        // 加解密（用往返后的字符串密钥）
        String raw = "RSA 加解密测试 中文";
        String cipher = RsaHelper.encode(pub, raw);
        assertEquals(raw, RsaHelper.decode(pri, cipher));
    }

    public void testRsaSignVerify() throws Exception {
        KeyPair kp = RsaKeyGenerator.getKeyPair();
        String pub = RsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = RsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());

        String raw = "RSA 签名测试 中文";
        String sign = RsaHelper.sign(pri, raw);
        assertNotNull(sign);
        assertTrue(RsaHelper.verify(pub, raw, sign));
        assertFalse(RsaHelper.verify(pub, raw + "x", sign));
    }

    // ==================== 密钥协商 ====================

    public void testEcdh() throws Exception {
        KeyPair alice = EccKeyGenerator.getKeyPair();
        KeyPair bob = EccKeyGenerator.getKeyPair();

        // 密钥字符串往返
        String apub = EccKeyGenerator.getPublicKeyStr(alice.getPublic());
        String apri = EccKeyGenerator.getPrivateKeyStr(alice.getPrivate());
        assertNotNull(EccKeyGenerator.getPublicKey(apub));
        assertNotNull(EccKeyGenerator.getPrivateKey(apri));

        // 双方协商一致
        byte[] a = EcdhHelper.generateSecret(alice.getPrivate(), bob.getPublic());
        byte[] b = EcdhHelper.generateSecret(bob.getPrivate(), alice.getPublic());
        assertTrue("ECDH 双方共享密钥一致", Arrays.equals(a, b));

        // 协商出的密钥可直接用于 AES
        SecretKey ak = EcdhHelper.generateSecretKey(alice.getPrivate(), bob.getPublic(), "AES", 16);
        SecretKey bk = EcdhHelper.generateSecretKey(bob.getPrivate(), alice.getPublic(), "AES", 16);
        assertTrue(Arrays.equals(ak.getEncoded(), bk.getEncoded()));
    }

    public void testDh() throws Exception {
        KeyPair alice = DhHelper.generateKeyPair();
        KeyPair bob = DhHelper.generateKeyPair(alice.getPublic());

        // 密钥字符串往返
        String apub = DhHelper.getPublicKeyStr(alice.getPublic());
        String apri = DhHelper.getPrivateKeyStr(alice.getPrivate());
        assertNotNull(DhHelper.getPublicKey(apub));
        assertNotNull(DhHelper.getPrivateKey(apri));

        // 双方协商一致
        byte[] a = DhHelper.generateSecret(alice.getPrivate(), bob.getPublic());
        byte[] b = DhHelper.generateSecret(bob.getPrivate(), alice.getPublic());
        assertTrue("DH 双方共享密钥一致", Arrays.equals(a, b));

        // 字符串形式的协商
        assertEquals(
                DhHelper.generateSecretStr(DhHelper.getPrivateKeyStr(alice.getPrivate()),
                        DhHelper.getPublicKeyStr(bob.getPublic())),
                DhHelper.generateSecretStr(DhHelper.getPrivateKeyStr(bob.getPrivate()),
                        DhHelper.getPublicKeyStr(alice.getPublic())));
    }

    // ==================== 数字签名 ====================

    public void testDsa() throws Exception {
        KeyPair kp = DsaKeyGenerator.getKeyPair();
        String pub = DsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = DsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());

        // 密钥字符串往返
        assertEquals(pub, DsaKeyGenerator.getPublicKeyStr(DsaKeyGenerator.getPublicKey("DSA", pub)));
        assertEquals(pri, DsaKeyGenerator.getPrivateKeyStr(DsaKeyGenerator.getPrivateKey("DSA", pri)));

        // 用往返后的字符串密钥签名验签
        String raw = "DSA 签名测试 中文";
        String sign = DsaHelper.sign(pri, raw);
        assertTrue(DsaHelper.verify(pub, raw, sign));
        assertFalse(DsaHelper.verify(pub, raw + "x", sign));
    }

    public void testEcdsa() throws Exception {
        KeyPair kp = EcdsaKeyGenerator.getKeyPair();
        String pub = EcdsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = EcdsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());

        // 密钥字符串往返
        assertEquals(pub, EcdsaKeyGenerator.getPublicKeyStr(EcdsaKeyGenerator.getPublicKey(pub)));
        assertEquals(pri, EcdsaKeyGenerator.getPrivateKeyStr(EcdsaKeyGenerator.getPrivateKey(pri)));

        // 用往返后的字符串密钥签名验签
        String raw = "ECDSA 签名测试 中文";
        String sign = EcdsaHelper.sign(pri, raw);
        assertTrue(EcdsaHelper.verify(pub, raw, sign));
        assertFalse(EcdsaHelper.verify(pub, raw + "x", sign));
    }

    /**
     * ECDSA 三种签名算法全部验证
     */
    public void testEcdsaAllAlgorithms() throws Exception {
        KeyPair kp = EcdsaKeyGenerator.getKeyPair();
        String pub = EcdsaKeyGenerator.getPublicKeyStr(kp.getPublic());
        String pri = EcdsaKeyGenerator.getPrivateKeyStr(kp.getPrivate());

        for (String alg : new String[]{"SHA256withECDSA", "SHA384withECDSA", "SHA512withECDSA"}) {
            io.github.sander_i.encrypt.entity.digitalsignature.Ecdsa cfg =
                    new io.github.sander_i.encrypt.entity.digitalsignature.Ecdsa();
            cfg.setEnabled(true);
            cfg.setPublicKey(pub);
            cfg.setPrivateKey(pri);
            cfg.setSignAlgorithm(alg);
            EcdsaHelper.load(cfg);

            String raw = "alg " + alg;
            assertTrue(alg, EcdsaHelper.verify(raw, EcdsaHelper.sign(raw)));
        }
    }
}
