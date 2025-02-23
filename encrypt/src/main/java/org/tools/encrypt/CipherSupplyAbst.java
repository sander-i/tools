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
 * @author sander
 **/
public abstract class CipherSupplyAbst {

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
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持异常
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     */
    @SneakyThrows
    public Cipher prepCipher(AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode, Key secretKey, String ivParameter, int mode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        initSupport(algorithmMode);
        Cipher cipher = getInstanceCipher(algorithmMode);
        initCipher(cipher, algorithm, algorithmMode, secretKey, ivParameter, mode);
        return cipher;
    }

    public abstract Cipher getInstanceCipher(AlgorithmModeEnum algorithmMode) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException;

    public abstract void initCipher(Cipher cipher, AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode, Key secretKey, String ivParameter, int mode) throws InvalidKeyException, InvalidAlgorithmParameterException;

}
