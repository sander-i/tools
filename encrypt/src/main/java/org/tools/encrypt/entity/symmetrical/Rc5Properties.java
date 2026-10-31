package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * RC5 加密配置文件：默认 {@code RC5}（ECB/PKCS5Padding）、128 位密钥。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc5Properties extends EncryptProperties {
    {
        setAlgorithmMode(AlgorithmModeEnum.RC5.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.RC5.getAlgorithm());
        setKeyBits(128);
    }
}
