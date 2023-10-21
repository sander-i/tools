package org.tools.encrypt.symmetrical.des;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:13
 * @description des加密配置文件
 * @scope des
 */
@Log4j2
@Component
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical.des")
public class DesProperties {

    /**
     * 算法模式
     * DES
     * DES/ECB/NoPadding
     * DESede/CBC/PKCS5Padding
     */
    private String algorithmMode = "DES";
    /**
     * 算法
     */
    public static final String ALGORITHM = "DES";
    /**
     * 密钥位数
     */
    private int keyBits = 56;
    /**
     * DES密钥
     */
    private String secretKey;

    /**
     * DES 密钥对象
     */
    private SecretKey desKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        this.setDesKey();
    }

    public SecretKey getDesKey() {
        return desKey;
    }

    private void setDesKey() {
        try {
            this.desKey = DesKeyGenerator.getSecretKey(this.secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 密钥长度固定8bit
     *
     * @param secretKey DES密钥
     */
    public void setSecretKeyFixedLength(String secretKey) {
        this.secretKey = secretKey;
        this.setDesKeyFixedLength();
    }

    /**
     * 密钥长度固定8bit
     */
    private void setDesKeyFixedLength() {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.desKey = new SecretKeySpec(secretKeyBytes, ALGORITHM);
    }

    public String getAlgorithmMode() {
        return algorithmMode;
    }

    public void setAlgorithmMode(String algorithmMode) {
        this.algorithmMode = algorithmMode;
    }

    public void setDesKey(SecretKey desKey) {
        this.desKey = desKey;
    }

    public int getKeyBits() {
        return keyBits;
    }

    public void setKeyBits(int keyBits) {
        this.keyBits = keyBits;
    }
}
