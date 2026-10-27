package org.tools.encrypt.entity;

import lombok.Data;

import org.tools.encrypt.entity.asymmetric.Asymmetric;
import org.tools.encrypt.entity.symmetrical.Symmetrical;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 * @since 1.0.6.1
 */
@Data
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
