package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * Blowfish 加密配置文件：默认 {@code Blowfish}（ECB/PKCS5Padding）、128 位密钥。
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlowfishProperties extends EncryptProperties {
    {
        setAlgorithmMode(AlgorithmModeEnum.BLOWFISH.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.BLOWFISH.getAlgorithm());
        setKeyBits(128);
    }
}
