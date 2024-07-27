package org.tools.spring.boot.fortify.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tools.fortify.config.IdTooLongConvertConfiguration;

/**
 * <p>
 * 解决Long类型ID过长无法解析的问题
 * </p>
 *
 * @author sander 2024/06/27
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "tools", name = "fortify.longNumber.enabled", havingValue = "true")
@Import({IdTooLongConvertConfiguration.class})
// 扫描 IdTooLongConvertConfiguration 类所在包
@ComponentScan(basePackageClasses = IdTooLongConvertConfiguration.class)
public class IdTooLongConvertAutoConfiguration {

    static {
        log.info("IdTooLongConvertConfiguration.enabled: {}", true);
    }

}
