package org.tools.encrypt.asymmetric.rsa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 17:22
 * @description rsa加密配置文件
 * @scope rsa
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "tools.encrypt.asymmetric.rsa")
public class RsaProperties {

    /**
     * 算法模式
     * RSA
     * RSA/None/NoPadding
     * ======
     * RSA/None/OAEPWITHSHA-256ANDMGF1PADDING
     * RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
     */
    private String algorithmMode = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    /***
     * 算法
     */
    public static final String ALGORITHM = "RSA";
    /**
     * 验签算法
     */
    public static final String SIGN_ALGORITHM = "MD5withRSA";
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

    public String getAlgorithmMode() {
        return algorithmMode;
    }

    public void setAlgorithmMode(String algorithmMode) {
        this.algorithmMode = algorithmMode;
    }

    public int getKeyBits() {
        return keyBits;
    }

    public void setKeyBits(int keyBits) {
        this.keyBits = keyBits;
    }

    public Integer getMaxEncryptBlock() {
        return maxEncryptBlock;
    }

    public Integer getMaxDecryptBlock() {
        return maxDecryptBlock;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public PublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public PrivateKey getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setMaxEncryptBlock(Integer maxEncryptBlock) {
        this.maxEncryptBlock = maxEncryptBlock;
    }

    public void setMaxDecryptBlock(Integer maxDecryptBlock) {
        this.maxDecryptBlock = maxDecryptBlock;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        this.setRsaPublicKey();
    }

    public void setRsaPublicKey() {
        try {
            this.rsaPublicKey = RsaKeyGenerator.getPublicKey(this.publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        this.setRsaPrivateKey();
    }

    public void setRsaPrivateKey() {
        try {
            this.rsaPrivateKey = RsaKeyGenerator.getPrivateKey(this.privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void setRsaPublicKey(PublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public void setRsaPrivateKey(PrivateKey rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }
}
