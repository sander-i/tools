package org.tools.encrypt.entity.symmetrical;

import lombok.Data;

/**
 * 对称加密实体：聚合 AES / DES / IDEA 三种对称算法的配置
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
}
