package org.tools.encrypt.entity;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.tools.encrypt.entity.asymmetric.Asymmetric;
import org.tools.encrypt.entity.symmetrical.Symmetrical;

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
