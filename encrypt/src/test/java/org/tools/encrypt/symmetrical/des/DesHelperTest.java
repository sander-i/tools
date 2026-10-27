package org.tools.encrypt.symmetrical.des;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.des.DesException;

import javax.crypto.SecretKey;

import java.security.InvalidKeyException;

/**
 * DesHelper 冒烟测试
 * 覆盖全部公开方法：load×3 / encode×3 / decode×3 / getProperties
 *
 * @author sander 2026/10/24
 * @since 1.0.6.1
 */
public class DesHelperTest {
    /** starter 样例 application.yml 中的 DES 密钥（8 字节） */
    private static final String KEY = "BeYour18";
    private static final String PLAINTEXT = "DES smoke 测试文本";

    private static DesProperties loadGlobal(String key) throws DesException {
        DesProperties props = new DesProperties();
        props.setSecretKeyStr(key);
        props.setSecretKey(DesKeyGenerator.getSecretKey("DES", key));
        DesHelper.load(props);
        return props;
    }

    @Test
    public void testLoadKeyPair() throws Exception {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm("DES");
        keyPair.setAlgorithmMode("DES");
        keyPair.setSecretKey(KEY);
        DesHelper.load(keyPair);

        Assert.assertEquals(PLAINTEXT, DesHelper.decode(DesHelper.encode(PLAINTEXT)));
    }

    @Test
    public void testLoadKeyPairNamed() throws Exception {
        org.tools.encrypt.entity.symmetrical.Des des = new org.tools.encrypt.entity.symmetrical.Des();
        des.setEnabled(true);
        des.setGroupEnabled(true);
        des.setSecretKey(KEY);
        des.setAlgorithm("DES");
        des.setAlgorithmMode("DES");

        java.util.List<KeyPair> groupList = new java.util.ArrayList<>();
        KeyPair idCard = new KeyPair();
        idCard.setName("id-card");
        idCard.setSecretKey("12345678");
        groupList.add(idCard);
        des.setGroup(groupList);

        // 分组加载：缺省字段由 Des 顶层配置补齐
        DesHelper.load(des, "id-card");

        String ciphertext = DesHelper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, DesHelper.decode(ciphertext));
        Assert.assertEquals("12345678", DesHelper.getProperties().getSecretKeyStr());
    }

    @Test
    public void testLoadProperties() throws DesException {
        loadGlobal(KEY);
        Assert.assertNotNull(DesHelper.getProperties());
    }

    /**
     * 只提供字符串密钥（无 SecretKey 对象）的装载场景：
     * load(DesProperties) 会自动调用 DesProperties.setDesKeyFixedLength() 按 UTF-8 原文构造密钥。
      *
      * @throws DesException DES 加载密钥或编解码失败时抛出
     */
    @Test
    public void testLoadPropertiesWithStringKeyOnly() throws DesException {
        DesProperties props = new DesProperties();
        props.setSecretKeyStr(KEY);
        DesHelper.load(props);

        Assert.assertNotNull(DesHelper.getProperties().getSecretKey());
        String ciphertext = DesHelper.encode(PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, DesHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithGlobalConfig() throws DesException {
        loadGlobal(KEY);
        String ciphertext = DesHelper.encode(PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, DesHelper.decode(ciphertext));
    }

    @Test
    public void testEncodeDecodeWithStringKey() throws DesException {
        loadGlobal(KEY);
        String ciphertext = DesHelper.encode(KEY, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, DesHelper.decode(KEY, ciphertext));
    }

    @Test
    public void testEncodeDecodeWithSecretKey() throws Exception {
        loadGlobal(KEY);
        SecretKey key = DesKeyGenerator.getSecretKey("DES", KEY);
        String ciphertext = DesHelper.encode(key, PLAINTEXT);
        Assert.assertEquals(PLAINTEXT, DesHelper.decode(key, ciphertext));
    }

    /**
     * 非法长度密钥（"BeYourself" 为 10 字节，DES 要求恰好 8 字节）
     * 在 Cipher.init 阶段抛 InvalidKeyException（包装为 SymmetricalException）。
     */
    @Test
    public void testInvalidLengthKeyRejected() {
        DesProperties props = new DesProperties();
        props.setSecretKeyStr("BeYourself");
        props.setSecretKey(DesKeyGenerator.getSecretKey("DES", "BeYourself"));
        DesHelper.load(props);

        try {
            DesHelper.encode(PLAINTEXT);
            Assert.fail("10 字节密钥应被拒绝");
        } catch (SymmetricalException e) {
            Assert.assertTrue("应包装 InvalidKeyException（密钥长度错误）",
                    e.getCause() instanceof InvalidKeyException);
        }
    }
}
