package org.tools.spring.boot.encrypt;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

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
    private static final String PREFIX = "tools.encrypt.";

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();

        for (PropertySource<?> ps : ((AbstractEnvironment) env).getPropertySources()) {
            if (ps instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>) ps;
                for (String name : eps.getPropertyNames()) {
                    // 判断是否存在加密配置
                    if (name.startsWith(PREFIX)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
