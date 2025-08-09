package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

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
