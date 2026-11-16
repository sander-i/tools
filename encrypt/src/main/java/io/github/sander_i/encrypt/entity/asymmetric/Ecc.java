package io.github.sander_i.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;

import java.util.List;

/**
 * ECC 椭圆曲线加密配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.asymmetric.ecc")
public class Ecc extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.EC.getAlgorithm();

    /**
     * 椭圆曲线名称
     * secp256r1（P-256，默认）
     * secp384r1（P-384）
     * secp521r1（P-521）
     * secp256k1
     */
    private String curve = "secp256r1";

    /**
     * 密钥位数，仅作记录，实际由曲线决定
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
