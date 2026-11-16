package io.github.sander_i.encrypt.entity;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.sander_i.encrypt.entity.asymmetric.Asymmetric;
import io.github.sander_i.encrypt.entity.symmetrical.Symmetrical;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 */
@Data
@ConfigurationProperties(prefix = "tools.encrypt")
public class Encrypt {

    /**
     * 非对称加密
     */
    private Asymmetric asymmetric;

    /**
     * 对称加密
     */
    private Symmetrical symmetrical;

}
