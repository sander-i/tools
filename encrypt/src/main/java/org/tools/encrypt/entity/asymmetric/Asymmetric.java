package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 */
@Data
@ConfigurationProperties(prefix = "tools.encrypt.asymmetric")
public class Asymmetric {

    /**
     * RSA
     */
    private Rsa rsa;

}
