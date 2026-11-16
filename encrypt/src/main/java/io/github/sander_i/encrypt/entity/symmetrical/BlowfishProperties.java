package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

/**
 * Blowfish 加密运行时配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlowfishProperties extends EncryptProperties {

    {
        setAlgorithmMode(AlgorithmModeEnum.BLOWFISH_CBC_PKCS5PADDING.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.BLOWFISH.getAlgorithm());
        setKeyBits(128);
    }

}
