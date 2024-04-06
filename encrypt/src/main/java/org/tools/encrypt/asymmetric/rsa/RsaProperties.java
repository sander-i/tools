package org.tools.encrypt.asymmetric.rsa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.AlgorithmModeEnum;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 17:22
 * @description rsa加密配置文件
 * @scope rsa
 */
@Slf4j
@Data
public class RsaProperties {

    /**
     * 算法模式
     * RSA
     * RSA/None/NoPadding
     * ======
     * RSA/None/OAEPWITHSHA-256ANDMGF1PADDING
     * RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
     */
    private String algorithmMode = AlgorithmModeEnum.RSA_ECB_OAEPWITHSHA256ANDMGF1PADDING.getAlgorithmMode();

    /***
     * 算法
     */
    private String algorithm = AlgorithmEnum.RSA.getAlgorithm();

    /**
     * 验签算法
     */
    private String signAlgorithm = AlgorithmEnum.MD5withRSA.getAlgorithm();

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
     * 向量
     */
    private String ivParameter;

    /**
     * RSA公钥
     */
    private String publicKey;

    /**
     * RSA公钥对象
     */
    private PublicKey rsaPublicKey;

    /**
     * RSA私钥
     */
    private String privateKey;

    /**
     * RSA私钥对象
     */
    private PrivateKey rsaPrivateKey;

}
