package org.tools.spring.boot.encrypt.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tools.encrypt.entity.Encrypt;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/10 22:36
 * @description des 自动配置类
 * @scope spring boot
 */
@Configuration
@ConditionalOnClass({Encrypt.class})
@EnableConfigurationProperties({Encrypt.class})
// 存在对应配置信息时初始化该配置类
@ConditionalOnProperty
        (
                //存在配置前缀
                value = "tools.encrypt"
        )
@Slf4j
public class EncryptAutoConfiguration {

    @Autowired
    Encrypt encrypt;

    @Bean
    @ConditionalOnMissingBean(name = {"encryptTemplate"})
    /**
     * 始终只有一个实例化对象
     *
     * @return Des
     */
    public Encrypt automaticInjection() {
        return encrypt;
    }

}
