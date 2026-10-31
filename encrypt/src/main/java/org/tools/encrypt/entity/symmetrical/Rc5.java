package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * RC5 对称加密配置实体。
 *
 * <p>默认算法模式为 {@code RC5}（ECB/PKCS5Padding），密钥为 Hex 字符串，默认 128 位。
 * RC5 的轮数、分组长度与字长固定为 12 / 8 字节 / 32 位，不是可配置项。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc5 extends KeyPair {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * RC5
     * RC5/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.RC5.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.RC5.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 128;

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
