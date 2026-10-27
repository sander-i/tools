package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * aes加密配置文件
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AesProperties extends EncryptProperties {
    {
        setAlgorithmMode(AlgorithmModeEnum.AES_CBC_PKCS5PADDING.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.AES.getAlgorithm());
        setKeyBits(256);
    }
}
