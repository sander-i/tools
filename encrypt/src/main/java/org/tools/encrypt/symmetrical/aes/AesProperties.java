package org.tools.encrypt.symmetrical.aes;

import lombok.Data;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.AlgorithmModeEnum;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * des加密配置文件
 *
 * @author sander
 * @version 1.0
 */
@Data
public class AesProperties {

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
    private int keyBits = 256;

    /**
     * 向量
     */
    private String ivParameter;

    /**
     * AES密钥
     */
    private String secretKey;

    /**
     * AES 密钥对象
     */
    private SecretKey aesKey;

    /**
     * 密钥长度固定8bit
     */
    protected void setAesKeyFixedLength() {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.aesKey = new SecretKeySpec(secretKeyBytes, algorithm);
    }

}
