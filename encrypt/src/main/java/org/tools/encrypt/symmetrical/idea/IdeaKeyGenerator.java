package org.tools.encrypt.symmetrical.idea;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 **/
@Slf4j
public class IdeaKeyGenerator {

    private IdeaKeyGenerator() {
    }

    /**
     * 密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * Idea 配置文件
     */
    private static IdeaProperties properties;

    /**
     * 获取密钥对象。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @return SecretKey
     * @throws IdeaException IDEA加解密异常
     */
    public static SecretKey getSecretKey() throws IdeaException {
        try {
            KeyGenerator keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.IDEA.getAlgorithm());
            keyGenerator.init(KEY_BITS);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new IdeaException(e);
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
     * 获取密钥对象。
     *
     * <p>密钥字符串为 <b>Hex</b>（与 {@link #getSecretKeyStr} 输出对称，32 个 Hex 字符 = 128 位）。
     * 此前按 UTF-8 原文取字节，与 {@code IdeaHelper.load} 依赖的 Hex 约定不一致
     * （IdeaHelper 因此被迫调用 AesKeyGenerator），已对齐为 Hex 解码。</p>
     *
     * @param algorithm 算法
     * @param secretKey Hex 密钥字符串
     * @return 密钥对象
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) {
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new IdeaException(e);
        }
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 获取Idea配置文件
     *
     * @return IdeaProperties
     * @throws IdeaException IDEA加解密异常
     */
    public static IdeaProperties getProperties() throws IdeaException {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new IdeaProperties();
                }
            }
        }
        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }

}
