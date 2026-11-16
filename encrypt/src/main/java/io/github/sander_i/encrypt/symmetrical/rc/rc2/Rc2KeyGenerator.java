package io.github.sander_i.encrypt.symmetrical.rc.rc2;

import lombok.extern.slf4j.Slf4j;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.EncryptKeyGenerator;
import io.github.sander_i.encrypt.SecretKeyCodec;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.exception.EncryptException;
import io.github.sander_i.encrypt.exception.symmetrical.rc2.Rc2Exception;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * RC2 密钥生成工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class Rc2KeyGenerator {

    private Rc2KeyGenerator() {
    }

    /**
     * 密钥位数，RC2 支持 40~1024 位，默认 128
     */
    private static final int KEY_BITS = 128;

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
    private static void init() throws Rc2Exception {
        if (BaseUtils.isNull(keyGenerator)) {
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.RC2.getAlgorithm());
                        keyGenerator.init(KEY_BITS);
                    }
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new Rc2Exception(e);
                }
            }
        }
    }

    /**
     * 生成随机密钥
     *
     * @return SecretKey
     * @throws Rc2Exception RC2 加解密异常
     */
    public static SecretKey getSecretKey() throws Rc2Exception {
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
