package io.github.sander_i.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.SignAlgorithmEnum;

import java.util.List;

/**
 * ECDSA 数字签名配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.digitalsignature.ecdsa")
public class Ecdsa extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.EC.getAlgorithm();

    /**
     * 签名算法
     * SHA256withECDSA（默认）
     * SHA384withECDSA
     * SHA512withECDSA
     */
    private String signAlgorithm = SignAlgorithmEnum.SHA256_WITH_ECDSA.getSignAlgorithm();

    /**
     * 椭圆曲线名称
     */
    private String curve = "secp256r1";

    /**
     * 密钥位数，由曲线决定
     */
    private int keyBits = 256;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 分组是否启用
     */
    private Boolean groupEnabled = false;

    /**
     * 分组
     */
    private List<KeyPair> group;

}
