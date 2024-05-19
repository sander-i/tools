package org.tools.encrypt;

import org.tools.bedrock.util.BaseUtils;

import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/12 17:07
 * @description 密钥生成器
 * @scope encrypt
 */
public class EncryptKeyGenerator {

    private EncryptKeyGenerator() {
    }

    private static KeyGenerator keyGenerator;

    private static KeyPairGenerator keyPairGenerator;

    /**
     * 密钥生成器
     *
     * @param algorithm 算法
     * @return KeyGenerator
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyGenerator getKeyGenerator(String algorithm) throws NoSuchAlgorithmException {
        if (BaseUtils.isNull(keyGenerator) || !Objects.equals(algorithm, keyGenerator.getAlgorithm())) {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        }
        return keyGenerator;
    }

    /**
     * 密钥对生成器
     *
     * @param algorithm 算法
     * @return KeyPairGenerator
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyPairGenerator getKeyPairGenerator(String algorithm) throws NoSuchAlgorithmException {
        if (BaseUtils.isNull(keyPairGenerator) || !Objects.equals(algorithm, keyPairGenerator.getAlgorithm())) {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        }
        return keyPairGenerator;
    }

}
