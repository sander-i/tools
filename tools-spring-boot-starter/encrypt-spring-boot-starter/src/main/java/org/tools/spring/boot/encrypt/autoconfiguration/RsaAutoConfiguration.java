package org.tools.spring.boot.encrypt.autoconfiguration;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tools.encrypt.asymmetric.rsa.Rsa;
import org.tools.encrypt.asymmetric.rsa.RsaProperties;
import org.tools.encrypt.asymmetric.rsa.factory.RsaFactory;
import org.tools.encrypt.symmetrical.des.DesProperties;

import javax.annotation.Resource;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 23:26
 * @description rsa 自动配置类
 * @scope spring boot
 */
@Configuration
@ConditionalOnClass({DesProperties.class})
@EnableConfigurationProperties({DesProperties.class})
// 存在对应配置信息时初始化该配置类
@ConditionalOnProperty
        (
                //存在配置前缀
                value = "tools.encrypt.asymmetric.rsa"
        )
@Log4j2
public class RsaAutoConfiguration {

    @Resource
    RsaProperties rsaProperties;

    @Bean
    @ConditionalOnMissingBean(name = {"rsaTemplate"})
    /**
     * 始终只有一个实例化对象
     *
     * @return Des
     */
    public Rsa automaticInjection() {
        // des工厂获取实现类
        return RsaFactory.newInstance(rsaProperties);
    }

}
