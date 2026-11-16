package io.github.sander_i.encrypt.symmetrical.des;

import junit.framework.TestCase;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Des;
import io.github.sander_i.encrypt.entity.symmetrical.DesProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;
import io.github.sander_i.encrypt.exception.EncryptException;
import io.github.sander_i.encrypt.exception.symmetrical.des.DesException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * DES 加解密测试
 *
 * @author sander 2024/03/30
 */
public class DesHelperTest extends TestCase {

    private static final String IV8 = "12345678";
    private static final String PLAIN = "DES 中文测试 1234567890";

    public void testEncode() throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        KeyPair keyPair = new KeyPair();
        keyPair.setAlgorithm(AlgorithmEnum.DES.getAlgorithm());
        keyPair.setAlgorithmMode(AlgorithmModeEnum.DES.getAlgorithmMode());
        keyPair.setIvParameter(IV8);
        keyPair.setSecretKey("12345678");

        DesHelper.load(keyPair);

        DesProperties properties = DesHelper.getProperties();
        assertNotNull(properties);
        assertEquals("12345678", properties.getSecretKeyStr());

        String encoded = DesHelper.encode("66666666");
        assertNotNull(encoded);
        assertEquals("66666666", DesHelper.decode(encoded));
    }

    public void testDecode() throws Exception {
        Des des = new Des();
        des.setEnabled(true);
        des.setSecretKey(DesKeyGenerator.getSecretKeyStr(DesKeyGenerator.getSecretKey()));
        des.setIvParameter(IV8);
        DesHelper.load(des);

        String encoded = DesHelper.encode(PLAIN);
        assertNotNull(encoded);
        assertEquals(PLAIN, DesHelper.decode(encoded));
    }

    public void testTestEncode() throws Exception {
        Des des = new Des();
        des.setEnabled(true);
        des.setSecretKey(DesKeyGenerator.getSecretKeyStr(DesKeyGenerator.getSecretKey()));
        des.setIvParameter(IV8);
        DesHelper.load(des);

        assertEquals("同一明文加解密应往返一致", PLAIN, DesHelper.decode(DesHelper.encode(PLAIN)));
    }

    /**
     * 回归：分组未配置 vector 时应继承父级，否则 CBC 初始化会 NPE
     */
    public void testTestEncode1() throws Exception {
        Des des = new Des();
        des.setEnabled(true);
        des.setGroupEnabled(true);
        des.setSecretKey(DesKeyGenerator.getSecretKeyStr(DesKeyGenerator.getSecretKey()));
        des.setIvParameter(IV8);

        KeyPair group = new KeyPair();
        group.setName("no-iv");
        group.setSecretKey(DesKeyGenerator.getSecretKeyStr(DesKeyGenerator.getSecretKey()));
        List<KeyPair> groupList = new ArrayList<>();
        groupList.add(group);
        des.setGroup(groupList);

        DesHelper.load(des, "no-iv");
        assertEquals("应继承父级向量", IV8, DesHelper.getProperties().getIvParameter());
        assertEquals(PLAIN, DesHelper.decode(DesHelper.encode(PLAIN)));
    }

    public void testTestDecode() throws Exception {
        // 密钥往返
        javax.crypto.SecretKey original = DesKeyGenerator.getSecretKey();
        String encoded = DesKeyGenerator.getSecretKeyStr(original);
        javax.crypto.SecretKey restored = DesKeyGenerator.getSecretKey("DES", encoded);
        assertEquals("密钥往返后字节应一致",
                java.util.Arrays.toString(original.getEncoded()),
                java.util.Arrays.toString(restored.getEncoded()));

        Des des = new Des();
        des.setEnabled(true);
        des.setSecretKey(encoded);
        des.setIvParameter(IV8);
        DesHelper.load(des);
        assertEquals(PLAIN, DesHelper.decode(DesHelper.encode(PLAIN)));
    }

    public void testTestDecode1() throws Exception {
        // disabled 时应拒绝
        Des des = new Des();
        des.setEnabled(false);
        try {
            des.setSecretKey("12345678");
            DesHelper.load(des, "any");
            fail("expected DesException");
        } catch (DesException expected) {
            // 预期
        }
    }
}
