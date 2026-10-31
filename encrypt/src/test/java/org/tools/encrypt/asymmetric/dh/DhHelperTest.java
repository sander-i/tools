package org.tools.encrypt.asymmetric.dh;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.asymmetric.Dh;
import org.tools.encrypt.entity.asymmetric.DhProperties;
import org.tools.encrypt.exception.asymmetric.dh.DhException;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Collections;

import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * DhHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / generateKeyPair×3 / generateSharedSecret /
 * generateSharedSecretStr×2 / getParams / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class DhHelperTest {
    private static KeyPair alice() throws DhException {
        return DhKeyGenerator.generateKeyPair(2048);
    }

    @Test
    public void testGenerateSharedSecret() throws Exception {
        KeyPair alice = alice();
        DHParameterSpec params = DhHelper.getParams(alice.getPublic());
        KeyPair bob = DhHelper.generateKeyPair(params);

        byte[] secret1 = DhHelper.generateSharedSecret(alice.getPrivate(), bob.getPublic());
        byte[] secret2 = DhHelper.generateSharedSecret(bob.getPrivate(), alice.getPublic());

        Assert.assertArrayEquals(secret1, secret2);
        // 2048 位 DH 的原始共享秘密长度等于素数 p 的字节长度
        Assert.assertEquals(256, secret1.length);
    }

    @Test
    public void testGenerateSharedSecretStr() throws Exception {
        KeyPair alice = alice();
        DHParameterSpec params = DhHelper.getParams(alice.getPublic());
        KeyPair bob = DhHelper.generateKeyPair(params);

        String alicePublic = DhKeyGenerator.getPublicKeyStr(alice.getPublic());
        String alicePrivate = DhKeyGenerator.getPrivateKeyStr(alice.getPrivate());
        String bobPublic = DhKeyGenerator.getPublicKeyStr(bob.getPublic());
        String bobPrivate = DhKeyGenerator.getPrivateKeyStr(bob.getPrivate());

        String secret1 = DhHelper.generateSharedSecretStr(alicePrivate, bobPublic);
        String secret2 = DhHelper.generateSharedSecretStr(bobPrivate, alicePublic);
        Assert.assertEquals(secret1, secret2);

        // 字符串路径与对象路径结果一致
        byte[] objectSecret = DhHelper.generateSharedSecret(alice.getPrivate(), bob.getPublic());
        Assert.assertEquals(Base64.encodeBase64String(objectSecret), secret1);
    }

    @Test
    public void testMismatchedParamsThrows() throws Exception {
        KeyPair first = DhHelper.generateKeyPair(1024);
        KeyPair second = DhHelper.generateKeyPair(2048);
        try {
            DhHelper.generateSharedSecret(first.getPrivate(), second.getPublic());
            Assert.fail("域参数不一致应抛出 DhException");
        } catch (DhException e) {
            Assert.assertNotNull(e.getCause());
        }
    }

    @Test
    public void testGenerateKeyPairDelegates() throws Exception {
        java.security.KeyPair defaultPair = DhHelper.generateKeyPair();
        Assert.assertEquals("DH", defaultPair.getPublic().getAlgorithm());

        java.security.KeyPair smallPair = DhHelper.generateKeyPair(1024);
        Assert.assertEquals(1024, ((DHPublicKey) smallPair.getPublic()).getParams().getP().bitLength());

        DHParameterSpec params = DhHelper.getParams(defaultPair.getPublic());
        java.security.KeyPair withParams = DhHelper.generateKeyPair(params);
        Assert.assertEquals(params.getP(), ((DHPublicKey) withParams.getPublic()).getParams().getP());
    }

    @Test
    public void testLoadKeyPair() throws Exception {
        KeyPair source = alice();
        org.tools.encrypt.entity.KeyPair keyPair = new org.tools.encrypt.entity.KeyPair();
        keyPair.setPublicKey(DhKeyGenerator.getPublicKeyStr(source.getPublic()));
        keyPair.setPrivateKey(DhKeyGenerator.getPrivateKeyStr(source.getPrivate()));

        DhHelper.load(keyPair);

        DhProperties properties = DhHelper.getProperties();
        Assert.assertEquals("DH", properties.getAlgorithm());
        Assert.assertEquals(2048, properties.getKeyBits());
        Assert.assertNotNull(properties.getPublicKey());
        Assert.assertNotNull(properties.getPrivateKey());
        Assert.assertNotNull(properties.getParams());
    }

    @Test
    public void testLoadKeyPairWithName() throws Exception {
        KeyPair source = alice();

        org.tools.encrypt.entity.KeyPair group = new org.tools.encrypt.entity.KeyPair();
        group.setName("group-1");
        group.setPublicKey(DhKeyGenerator.getPublicKeyStr(source.getPublic()));
        group.setPrivateKey(DhKeyGenerator.getPrivateKeyStr(source.getPrivate()));

        Dh dh = new Dh();
        dh.setEnabled(true);
        dh.setGroupEnabled(true);
        dh.setGroup(Collections.singletonList(group));

        DhHelper.load(dh, "group-1");

        DhProperties properties = DhHelper.getProperties();
        // 分组未设置的字段由顶层 Dh 默认值补齐
        Assert.assertEquals("DH", properties.getAlgorithm());
        Assert.assertEquals(2048, properties.getKeyBits());
        Assert.assertNotNull(properties.getPublicKey());
        Assert.assertNotNull(properties.getPrivateKey());
    }

    @Test
    public void testLoadKeyPairWithNameNotEnabled() throws Exception {
        Dh dh = new Dh();
        try {
            DhHelper.load(dh, "group-1");
            Assert.fail("未启用时加载应抛出 DhException");
        } catch (DhException e) {
            Assert.assertTrue(e.getMessage().contains("enable"));
        }
    }

    @Test
    public void testLoadProperties() throws Exception {
        KeyPair source = alice();
        DhProperties properties = new DhProperties();
        properties.setPublicKeyStr(DhKeyGenerator.getPublicKeyStr(source.getPublic()));
        properties.setPrivateKeyStr(DhKeyGenerator.getPrivateKeyStr(source.getPrivate()));
        properties.setPublicKey(source.getPublic());
        properties.setPrivateKey(source.getPrivate());

        DhHelper.load(properties);

        Assert.assertSame(properties, DhHelper.getProperties());
        Assert.assertNotNull(DhHelper.getProperties().getParams());
    }

    @Test
    public void testGetParamsWithNull() {
        try {
            DhHelper.getParams((PublicKey) null);
            Assert.fail("公钥为空应抛出 DhException");
        } catch (DhException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    /**
     * 只配置 keyBits、缺少公私钥字符串时应在解析前被拒绝，而不是抛 NPE。
     *
     * @throws Exception 装载失败时抛出
     */
    @Test
    public void testLoadKeyPairWithOnlyKeyBitsRejected() throws Exception {
        org.tools.encrypt.entity.KeyPair keyPair = new org.tools.encrypt.entity.KeyPair();
        keyPair.setKeyBits(2048);
        try {
            DhHelper.load(keyPair);
            Assert.fail("缺少公私钥应抛出 DhException");
        } catch (DhException e) {
            Assert.assertTrue(e.getMessage().contains("publicKey"));
        }
    }

    @Test
    public void testGetProperties() {
        DhProperties properties = DhHelper.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertEquals("DH", properties.getAlgorithm());
        Assert.assertEquals(2048, properties.getKeyBits());
    }
}
