package io.github.sander_i.encrypt.symmetrical.des.des3;

import lombok.extern.slf4j.Slf4j;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.EncryptKeyGenerator;
import io.github.sander_i.encrypt.SecretKeyCodec;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.exception.EncryptException;
import io.github.sander_i.encrypt.exception.symmetrical.des3.Des3Exception;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * 3DES 密钥生成工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class Des3KeyGenerator {

    private Des3KeyGenerator() {
    }

    /**
     * 密钥位数：168 位（3 × 56）
     */
    private static final int KEY_BITS = 168;

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * 密钥生成器
     */
    private static KeyGenerator keyGenerator;

    /**
     * 初始化密钥生成器对象
     */
    private static void init() throws Des3Exception {
        if (BaseUtils.isNull(keyGenerator)) {
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.DES_EDE.getAlgorithm());
                        keyGenerator.init(KEY_BITS);
                    }
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new Des3Exception(e);
                }
            }
        }
    }

    /**
     * 生成随机密钥
     *
     * @return SecretKey
     * @throws Des3Exception 3DES 加解密异常
     */
    public static SecretKey getSecretKey() throws Des3Exception {
        init();
        return keyGenerator.generateKey();
    }

    /**
     * 密钥对象转十六进制字符串
     *
     * @param secretKey 密钥对象
     * @return 密钥字符串
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return SecretKeyCodec.encode(secretKey);
    }

    /**
     * 字符串转密钥对象
     *
     * @param algorithm 算法
     * @param secretKey 密钥字符串
     * @return 密钥对象
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws EncryptException {
        return SecretKeyCodec.decode(algorithm, secretKey);
    }

}
