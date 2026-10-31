package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;

import java.util.List;

/**
 * DH 密钥协商配置实体。
 *
 * <p>与 {@link Rsa} 类似，支持按分组名切换密钥；但 DH 只用于生成密钥对与协商共享秘密，
 * 不提供 {@code encode}/{@code decode}。默认算法 {@code DH}、默认密钥 2048 位。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dh extends KeyPair {
    /**
     * 是否启用（{@code load(KeyPair, String)} 前置校验）
     */
    private Boolean enabled = false;

    /**
     * 算法
     */
    public String algorithm = "DH";

    /**
     * 密钥位数
     * 512
     * 1024
     * 2048
     * 4096（取决于 JDK Provider 支持范围）
     */
    private int keyBits = 2048;

    /**
     * 分组是否启用
     */
    private Boolean groupEnabled = false;

    /**
     * 分组
     */
    private List<KeyPair> group;
}
