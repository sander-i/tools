package org.tools.encrypt.entity.asymmetric;

import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 * @since 1.0.6.1
 */
@Data
public class Asymmetric {
    /**
     * RSA
     */
    private Rsa rsa;

    /**
     * DH 密钥协商
     */
    private Dh dh;

    /**
     * ECC（ECDH 密钥协商）
     */
    private Ecc ecc;
}
