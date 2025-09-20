package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.entity.KeyPair;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical.des")
public class Des extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * DES
     * DES/ECB/NoPadding
     * DESede/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmEnum.DES.getAlgorithm();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.DES.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 56;

    /**
     * 密钥
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
