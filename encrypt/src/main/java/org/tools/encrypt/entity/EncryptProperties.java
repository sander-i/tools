package org.tools.encrypt.entity;

import lombok.Data;

import org.tools.bedrock.annotations.Inherit;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

/**
 * 加密配置属性基类：算法、模式、密钥、分块与签名参数。
 *
 * @author sander
 * @since 1.0.6.6
 */
@Inherit
@Data
public class EncryptProperties {
    /**
     * 算法
     */
    protected String algorithm;

    /**
     * 算法模式
     */
    protected String algorithmMode;

    /**
     * 验签算法
     */
    protected String signAlgorithm;

    /**
     * 密钥位数
     */
    protected int keyBits;

    /**
     * 最大加密明文大小
     */
    protected Integer maxEncryptBlock;

    /**
     * 最大解密密文大小
     */
    protected Integer maxDecryptBlock;

    /**
     * 向量
     */
    protected String ivParameter;

    /**
     * 密钥
     */
    protected String secretKeyStr;

    /**
     * 密钥对象
     */
    protected SecretKey secretKey;

    /**
     * 公钥
     */
    protected String publicKeyStr;

    /**
     * 公钥对象
     */
    protected PublicKey publicKey;

    /**
     * 私钥
     */
    protected String privateKeyStr;

    /**
     * 私钥对象
     */
    protected PrivateKey privateKey;
}
