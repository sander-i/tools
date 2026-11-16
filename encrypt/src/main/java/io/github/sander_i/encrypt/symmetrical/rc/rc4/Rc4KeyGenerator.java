package io.github.sander_i.encrypt.symmetrical.rc.rc4;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.EncryptKeyGenerator;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.exception.symmetrical.rc4.Rc4Exception;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * RC4 密钥生成工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class Rc4KeyGenerator {

    private Rc4KeyGenerator() {
    }

    /**
     * 密钥位数，RC4 支持 40~1024 位，默认 128
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
    private static void init() throws Rc4Exception {
        if (BaseUtils.isNull(keyGenerator)) {
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.RC4.getAlgorithm());
                        keyGenerator.init(KEY_BITS);
                    }
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new Rc4Exception(e);
                }
            }
        }
    }

    /**
     * 生成随机密钥
     *
     * @return SecretKey
     * @throws Rc4Exception RC4 加解密异常
     */
    public static SecretKey getSecretKey() throws Rc4Exception {
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
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 字符串转密钥对象
     *
     * @param algorithm 算法
     * @param secretKey 密钥字符串
     * @return 密钥对象
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

}
