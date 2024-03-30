package org.tools.encrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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

    /**
     * 密钥生成器
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @return KeyGenerator
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyGenerator getKeyGenerator(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        return keyGenerator;
    }

    /**
     * 密钥生成器
     *
     * @param algorithm    算法
     * @param keySize      密钥长度
     * @param secureRandom 随机源
     * @return KeyGenerator
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyGenerator getKeyGenerator(String algorithm, int keySize, SecureRandom secureRandom) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        keyGenerator.init(secureRandom);
        return keyGenerator;
    }


    /**
     * 密钥
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @return SecretKey
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static SecretKey getSecretKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        return getKeyGenerator(algorithm, keySize).generateKey();
    }

    /**
     * 密钥
     *
     * @param algorithm    算法
     * @param keySize      密钥长度
     * @param secureRandom 随机源
     * @return SecretKey
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static SecretKey getSecretKey(String algorithm, int keySize, SecureRandom secureRandom) throws NoSuchAlgorithmException {
        return getKeyGenerator(algorithm, keySize, secureRandom).generateKey();
    }

    /**
     * 密钥对生成器
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @return KeyPairGenerator
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyPairGenerator getKeyPairGenerator(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator;
    }

    /**
     * 密钥对生成器
     *
     * @param algorithm    算法
     * @param keySize      密钥长度
     * @param secureRandom 随机源
     * @return KeyPairGenerator
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyPairGenerator getKeyPairGenerator(String algorithm, int keySize, SecureRandom secureRandom) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize, secureRandom);
        return keyPairGenerator;
    }

    /**
     * 密钥对
     *
     * @param algorithm 算法
     * @param keySize   密钥长度
     * @return KeyPair
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyPair getKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        return getKeyPairGenerator(algorithm, keySize).generateKeyPair();
    }

    /**
     * 密钥对
     *
     * @param algorithm    算法
     * @param keySize      密钥长度
     * @param secureRandom 随机源
     * @return KeyPair
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    public static KeyPair getKeyPair(String algorithm, int keySize, SecureRandom secureRandom) throws NoSuchAlgorithmException {
        return getKeyPairGenerator(algorithm, keySize, secureRandom).generateKeyPair();
    }

}
