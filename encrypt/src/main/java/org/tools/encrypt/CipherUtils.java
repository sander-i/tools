package org.tools.encrypt;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/05
 */
public class CipherUtils {

    private CipherUtils() {
    }

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
     * @throws NoSuchAlgorithmException           算法不存在或不支持
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     */
    public static Cipher prepCipher(AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode, Key secretKey, String ivParameter, int mode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance(algorithmMode.getAlgorithmMode());
        Key key = new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm());

        SecureRandom random;
        IvParameterSpec iv;
        switch (algorithmMode) {
            case AES:
            case DES:
            case RSA: {
                random = new SecureRandom();
                cipher.init(mode, key, random);
                break;
            }
            case DES_ECB_NO_PADDING: {
                cipher.init(mode, key);
                break;
            }
            case RSA_ECB_OAEPWITHSHA256ANDMGF1PADDING: {
                cipher.init(mode, secretKey);
                break;
            }
            case AES_CBC_PKCS5PADDING:
            case DES_EDE_CBC_PKCS5Padding: {
                iv = new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8));
                cipher.init(mode, key, iv);
                break;
            }
        }
        return cipher;
    }

}
