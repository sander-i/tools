package org.tools.spring.boot.encrypt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.tools.encrypt.entity.Encrypt;

/**
 * tools.encrypt 配置绑定实体。
 *
 * <p>继承 encrypt 模块的纯 POJO 配置树（Encrypt/Asymmetric/Symmetrical 及各算法配置），
 * 配置绑定注解与前缀由 starter 持有——encrypt 保持 Spring 无关，
 * 非 Spring 项目可直接复用其 POJO 与工具方法。</p>
 *
 * @author sander
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "tools.encrypt")
public class EncryptProperties extends Encrypt {
}
