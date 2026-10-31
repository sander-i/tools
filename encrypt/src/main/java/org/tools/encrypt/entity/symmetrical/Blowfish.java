package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * Blowfish 对称加密配置实体。
 *
 * <p>默认算法模式为 {@code Blowfish}（ECB/PKCS5Padding），密钥为 Hex 字符串，默认 128 位。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Blowfish extends KeyPair {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * Blowfish
     * Blowfish/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.BLOWFISH.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.BLOWFISH.getAlgorithm();

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
