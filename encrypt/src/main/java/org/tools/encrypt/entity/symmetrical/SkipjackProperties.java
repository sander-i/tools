package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * Skipjack 加密配置文件：默认 {@code SKIPJACK}（ECB/PKCS5Padding）、80 位密钥（10 字节）。
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SkipjackProperties extends EncryptProperties {
    {
        setAlgorithmMode(AlgorithmModeEnum.SKIPJACK.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.SKIPJACK.getAlgorithm());
        setKeyBits(80);
    }
}
