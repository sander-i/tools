package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * Skipjack 对称加密配置实体。
 *
 * <p>默认算法模式为 {@code SKIPJACK}（ECB/PKCS5Padding），密钥为 Hex 字符串且固定 80 位（10 字节）。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Skipjack extends KeyPair {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * SKIPJACK
     * SKIPJACK/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.SKIPJACK.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.SKIPJACK.getAlgorithm();

    /**
     * 密钥位数（Skipjack 固定 80 位）
     */
    private int keyBits = 80;

    /**
     * 密钥（Hex 字符串）
     */
    private String secretKey;

    /**
     * 分组是否启用
     */
    private Boolean groupEnabled = false;

    /**
     * 分组
     */
    private List<KeyPair> group;
}
