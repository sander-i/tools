package org.tools.encrypt.symmetrical.des;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.des.DesException;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * des密钥生成工具
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
@Slf4j
public class DesKeyGenerator {
    /**
     * 密钥位数
     */
    private static final int KEY_BITS = 56;

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * Des 配置文件
     */
    private static DesProperties properties;

    private DesKeyGenerator() {
    }

    /**
     * 获取密钥对象。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @return SecretKey
     * @throws DesException DES加解密异常
     */
    public static SecretKey getSecretKey() throws DesException {
        try {
            KeyGenerator keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.DES.getAlgorithm());
            keyGenerator.init(KEY_BITS);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 获取密钥字符串
     *
     * @param secretKey 密钥对象
     * @return 密钥字符串
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 获取密钥对象
     *
     * @param algorithm 算法
     * @param secretKey 密钥字符串
     * @return 密钥对象
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 单例模式-线程安全
     */
    private static void initProperties() {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new DesProperties();
                }
            }
        }
    }

    /**
     * 获取Des配置文件
     *
     * @return DesProperties
     * @throws DesException DES加解密异常
     */
    public static DesProperties getProperties() throws DesException {
        initProperties();

        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
