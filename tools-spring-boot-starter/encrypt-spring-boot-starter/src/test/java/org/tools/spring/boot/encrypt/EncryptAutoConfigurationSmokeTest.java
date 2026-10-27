package org.tools.spring.boot.encrypt;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.tools.spring.boot.encrypt.autoconfiguration.EncryptAutoConfiguration;
import org.tools.encrypt.entity.symmetrical.AesProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * EncryptAutoConfiguration 装配冒烟测试：验证"引依赖 + 写 tools.encrypt.* 配置即自动生效，
 * 无配置不装配"的启用契约（不依赖 Boot 应用上下文，直接以注解上下文驱动）。
 *
 * @author sander 2026/10/25
 * @since 2.0.0
 */
public class EncryptAutoConfigurationSmokeTest {
    private AnnotationConfigApplicationContext buildContext(Map<String, Object> props) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test", props));
        context.register(EncryptAutoConfiguration.class);
        context.refresh();
        return context;
    }

    /** 存在 tools.encrypt.* 配置时自动装配：aes.enabled=true 且密钥合法，aes bean 完成 Helper 装载 */
    @Test
    public void autoActiveWithToolsEncryptConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put("tools.encrypt.symmetrical.aes.enabled", "true");
        props.put("tools.encrypt.symmetrical.aes.secret-key", "a1887f3ff2cd45e6b0a91c79f21e35d8");
        props.put("tools.encrypt.symmetrical.aes.iv-parameter", "0123456789abcdef");

        try (AnnotationConfigApplicationContext context = buildContext(props)) {
            Assert.assertEquals("存在 tools.encrypt.* 配置时应装配出 aes 配置 bean",
                    1, context.getBeansOfType(AesProperties.class).size());
            // aes bean 为 Helper.load 装载后的激活配置（secretKey 已被解析为 Key 对象，不再断言原始字符串）
            AesProperties aes = context.getBean("aes", AesProperties.class);
            Assert.assertNotNull("aes bean 应完成 Helper 装载并返回激活配置", aes);
        }
    }

    /** 无任何 tools.encrypt.* 配置时不装配（OnEncryptPropertiesCondition 不通过） */
    @Test
    public void inactiveWithoutToolsEncryptConfig() {
        try (AnnotationConfigApplicationContext context = buildContext(Collections.emptyMap())) {
            Assert.assertTrue("无 tools.encrypt.* 配置时不应装配任何算法 bean",
                    context.getBeansOfType(AesProperties.class).isEmpty());
        }
    }
}
