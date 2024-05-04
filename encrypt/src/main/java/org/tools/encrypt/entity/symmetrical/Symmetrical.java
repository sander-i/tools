package org.tools.encrypt.entity.symmetrical;

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
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical")
public class Symmetrical {

    /**
     * AES
     */
    private Aes aes;

    /**
     * DES
     */
    private Des des;

}
