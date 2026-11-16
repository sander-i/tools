package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * des加密配置文件
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AesProperties extends EncryptProperties {

    {
        setAlgorithmMode(AlgorithmModeEnum.AES_CBC_PKCS5PADDING.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.AES.getAlgorithm());
        setKeyBits(256);
    }

    /**
     * 密钥长度固定8bit
     */
    protected void setAesKeyFixedLength() {
        byte[] secretKeyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(secretKeyBytes, algorithm);
    }

}
