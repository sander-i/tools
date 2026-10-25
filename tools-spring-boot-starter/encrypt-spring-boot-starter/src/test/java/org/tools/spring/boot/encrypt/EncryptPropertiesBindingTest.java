package org.tools.spring.boot.encrypt;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * EncryptProperties 绑定冒烟测试：验证 starter 侧承接 @ConfigurationProperties 后，
 * tools.encrypt.* 前缀仍能正确绑定到 encrypt 的 POJO 配置树（含嵌套 asymmetric/symmetrical）。
 *
 * @author sander 2026/10/25
 */
public class EncryptPropertiesBindingTest {

    /** tools.encrypt 前缀经 EncryptProperties 绑定到继承的 Encrypt POJO 树 */
    @Test
    public void bindToolsEncryptPrefix() {
        Map<String, Object> source = new HashMap<>();
        source.put("tools.encrypt.asymmetric.rsa.enabled", "true");
        source.put("tools.encrypt.asymmetric.rsa.public-key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDtest");
        source.put("tools.encrypt.symmetrical.aes.enabled", "true");
        source.put("tools.encrypt.symmetrical.aes.secret-key", "a1887f3ff2cd45e6b0a91c79f21e35d8");
        source.put("tools.encrypt.symmetrical.aes.iv-parameter", "0123456789abcdef");

        Binder binder = new Binder(new MapConfigurationPropertySource(source));
        EncryptProperties properties = binder.bind("tools.encrypt", EncryptProperties.class).get();

        Assert.assertNotNull("asymmetric 节点应被绑定", properties.getAsymmetric());
        Assert.assertNotNull("symmetrical 节点应被绑定", properties.getSymmetrical());
        Assert.assertTrue("rsa.enabled 应被绑定", properties.getAsymmetric().getRsa().getEnabled());
        Assert.assertEquals("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDtest",
                properties.getAsymmetric().getRsa().getPublicKey());
        Assert.assertEquals("a1887f3ff2cd45e6b0a91c79f21e35d8",
                properties.getSymmetrical().getAes().getSecretKey());
        Assert.assertEquals("0123456789abcdef",
                properties.getSymmetrical().getAes().getIvParameter());
    }

    /** 部分配置时仅绑定对应分支，未配置的分支保持 null（enabled 等默认值由实体自身持有） */
    @Test
    public void bindPartialConfig() {
        Map<String, Object> source = new HashMap<>();
        source.put("tools.encrypt.symmetrical.des.enabled", "true");

        Binder binder = new Binder(new MapConfigurationPropertySource(source));
        EncryptProperties properties = binder.bind("tools.encrypt", EncryptProperties.class).get();

        Assert.assertNull("未配置的 asymmetric 分支应为 null", properties.getAsymmetric());
        Assert.assertNotNull("已配置的 symmetrical 分支应被绑定", properties.getSymmetrical());
        Assert.assertNotNull("已配置的 des 节点应被绑定", properties.getSymmetrical().getDes());
        Assert.assertTrue("des.enabled 应为 true", properties.getSymmetrical().getDes().getEnabled());
        Assert.assertNull("未配置的 aes 节点应为 null", properties.getSymmetrical().getAes());
    }

}
