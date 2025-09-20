package org.tools.spring.boot.encrypt;

import org.springframework.context.annotation.Import;
import org.tools.spring.boot.encrypt.autoconfiguration.EncryptAutoConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启加密自动配置
 * 当配置文件中存在 tools.encrypt.* 时，自动开启加密配置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({EncryptAutoConfiguration.class})
public @interface EnableToolsEncrypt {
}
