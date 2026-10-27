package org.tools.encrypt;

import lombok.SneakyThrows;

import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Cipher 供给抽象基类：定义 initSupport、initCipher 与 prepCipher 骨架。
 *
 * @author sander
 * @since 1.0.6.6
 */
public abstract class CipherSupplyAbst {
    /**
     * 按算法模式注册所需 Provider
     *
     * @param algorithmMode 算法模式
     */
    public abstract void initSupport(AlgorithmModeEnum algorithmMode);

    /**
     * 预备加密对象
     *
     * @param algorithm     加密算法枚举
     * @param algorithmMode 加密算法模式枚举
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param mode          加密对象模式
     * @return javax.crypto.Cipher
     */
    @SneakyThrows
    public Cipher prepCipher(AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode, Key secretKey,
            String ivParameter, int mode) {
        initSupport(algorithmMode);
        Cipher cipher = getInstanceCipher(algorithmMode);
        initCipher(cipher, algorithm, algorithmMode, secretKey, ivParameter, mode);
        return cipher;
    }

    /**
     * 创建指定算法模式的 Cipher 实例
     *
     * @param algorithmMode 算法模式
     * @return Cipher 实例
     * @throws NoSuchPaddingException   填充方式不可用时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws NoSuchProviderException  Provider 不可用时抛出
     */
    public abstract Cipher getInstanceCipher(AlgorithmModeEnum algorithmMode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException;

    /**
     * 按算法模式初始化 Cipher
     *
     * @param cipher        Cipher 实例
     * @param algorithm     加密算法
     * @param algorithmMode 算法模式
     * @param secretKey     密钥
     * @param ivParameter   初始向量
     * @param mode          加解密模式（Cipher.ENCRYPT_MODE / DECRYPT_MODE）
     * @throws InvalidKeyException                密钥非法时抛出
     * @throws InvalidAlgorithmParameterException 算法参数非法时抛出
     */
    public abstract void initCipher(Cipher cipher, AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode,
            Key secretKey, String ivParameter, int mode)
            throws InvalidKeyException, InvalidAlgorithmParameterException;
}
