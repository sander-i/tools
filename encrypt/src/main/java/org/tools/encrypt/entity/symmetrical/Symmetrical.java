package org.tools.encrypt.entity.symmetrical;

import lombok.Data;

/**
 * 对称加密实体：聚合 AES / DES / IDEA / Blowfish / 3DES / RC2 / RC4 / RC5 / Skipjack 对称算法的配置
 *
 * @author sander 2024/03/15
 * @since 1.0.6.1
 */
@Data
public class Symmetrical {
    /**
     * AES
     */
    private Aes aes;

    /**
     * DES
     */
    private Des des;

    /**
     * IDEA
     */
    private Idea idea;

    /**
     * Blowfish
     */
    private Blowfish blowfish;

    /**
     * 3DES（DESede / TDEA）
     */
    private Des3 des3;

    /**
     * RC2
     */
    private Rc2 rc2;

    /**
     * RC4
     */
    private Rc4 rc4;

    /**
     * RC5
     */
    private Rc5 rc5;

    /**
     * Skipjack
     */
    private Skipjack skipjack;
}
