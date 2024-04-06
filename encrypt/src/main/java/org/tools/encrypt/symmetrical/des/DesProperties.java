package org.tools.encrypt.symmetrical.des;

import lombok.Data;
import org.tools.encrypt.AlgorithmEnum;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:13
 * @description des加密配置文件
 * @scope des
 */
@Data
public class DesProperties {

    /**
     * 算法模式
     * DES
     * DES/ECB/NoPadding
     * DESede/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmEnum.DES.getAlgorithm();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.DES.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 56;

    /**
     * 向量
     */
    private String ivParameter;

    /**
     * DES密钥
     */
    private String secretKey;

    /**
     * DES 密钥对象
     */
    private SecretKey desKey;

    /**
     * 密钥长度固定8bit
     */
    protected void setDesKeyFixedLength() {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.desKey = new SecretKeySpec(secretKeyBytes, algorithm);
    }

}
