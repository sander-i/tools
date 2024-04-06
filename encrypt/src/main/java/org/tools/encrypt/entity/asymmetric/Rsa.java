package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.entity.KeyPair;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class Rsa extends KeyPair {

    private Boolean enable;

    /**
     * 算法模式
     * RSA
     * RSA/None/NoPadding
     * ======
     * RSA/None/OAEPWITHSHA-256ANDMGF1PADDING
     * RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
     */
    private String algorithmMode = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

    /**
     * 算法
     */
    public String algorithm = "RSA";

    /**
     * 验签算法
     */
    public String signAlgorithm = "MD5withRSA";

    /**
     * 密钥位数
     * 512
     * 1024
     * 2048
     * 4096
     */
    private int keyBits = 1024;

    /**
     * RSA最大加密明文大小
     */
    private Integer maxEncryptBlock = 117;

    /**
     * RSA最大解密密文大小
     */
    private Integer maxDecryptBlock = 128;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    private Boolean groupEnable;

    private List<KeyPair> group;

}
