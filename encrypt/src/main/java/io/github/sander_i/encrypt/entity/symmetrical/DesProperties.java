package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

import javax.crypto.SecretKey;
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
public class DesProperties extends EncryptProperties {

    /**
     * DES密钥
     */
    private String secretKeyStr;

    /**
     * DES 密钥对象
     */
    private SecretKey secretKey;

    {
        setAlgorithmMode(AlgorithmModeEnum.DES.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.DES.getAlgorithm());
        setKeyBits(56);
    }

    /**
     * 密钥长度固定8bit
     */
    protected void setDesKeyFixedLength() {
        byte[] secretKeyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(secretKeyBytes, algorithm);
    }

}
