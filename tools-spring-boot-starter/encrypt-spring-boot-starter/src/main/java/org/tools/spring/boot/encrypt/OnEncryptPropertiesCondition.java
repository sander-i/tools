package org.tools.spring.boot.encrypt;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 判断是否存在加密配置
 *
 * @author sander
 **/
@Slf4j
public class OnEncryptPropertiesCondition implements Condition {

    /**
     * 加密配置前缀
     */
    private final static String prefix = "tools.encrypt.";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();

        for (PropertySource<?> ps : ((AbstractEnvironment) env).getPropertySources()) {
            if (ps instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>) ps;
                for (String name : eps.getPropertyNames()) {
                    // 判断是否存在加密配置
                    if (name.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
