package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.AlgorithmEnum;
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
public class Des extends KeyPair {

    private Boolean enable;

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

    private Boolean groupEnable;

    private List<KeyPair> group;

}
