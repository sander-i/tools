package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.AlgorithmModeEnum;
import org.tools.encrypt.entity.KeyPair;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Aes extends KeyPair {

    private Boolean enable;

    /**
     * 算法模式
     * AES
     * AES/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.AES_CBC_PKCS5PADDING.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.AES.getAlgorithm();

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
