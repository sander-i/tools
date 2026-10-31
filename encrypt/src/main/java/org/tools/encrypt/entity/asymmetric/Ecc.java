package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;

import java.util.List;

/**
 * ECC 密钥对配置：算法、密钥位数、曲线与公私钥。
 *
 * <p>曲线默认 {@code secp256r1}（P-256），仅用于 ECDH 密钥协商。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Ecc extends KeyPair {
    /**
     * 算法
     */
    public String algorithm = "EC";

    /**
     * 密钥位数
     */
    private int keyBits = 256;

    /**
     * 曲线名
     */
    private String curve = "secp256r1";

    /**
     * 分组是否启用
     */
    private Boolean groupEnabled = false;

    /**
     * 分组
     */
    private List<KeyPair> group;
}
