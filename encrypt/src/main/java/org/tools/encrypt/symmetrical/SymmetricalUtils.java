package org.tools.encrypt.symmetrical;

import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.AlgorithmModeEnum;
import org.tools.encrypt.CipherUtils;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
@Slf4j
public class SymmetricalUtils {


    /**
     * 执行加解密操作
     *
     * @param algorithm     加密算法枚举
     * @param algorithmMode 加密算法模式枚举
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param mode          加密对象模式
     * @param stuff         待处理
     * @return byte[]
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected static byte[] actuator(String algorithm, String algorithmMode, Key secretKey, String ivParameter, int mode, byte[] stuff)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Cipher对象实际完成解密操作
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherUtils.prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, mode);
        // 执行加解密操作
        return cipher.doFinal(stuff);
    }

    /**
     * 加密前处理
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected static byte[] encryptBefore(String todo) {
        return todo.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 加密后处理
     *
     * @param todo 待处理
     * @return String
     */
    protected static String encryptAfter(byte[] todo) {
        return Base64.getEncoder().encodeToString(todo);
    }

    /**
     * 解密前处理
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected static byte[] decryptBefore(String todo) {
        return Base64.getDecoder().decode(todo);
    }

    /**
     * 解密后处理
     *
     * @param todo 待处理
     * @return String
     */
    protected static String decryptAfter(byte[] todo) {
        return new String(todo);
    }

    public static String encode(String algorithm, String algorithmMode, SecretKey secretKey, String ivParameter, String raw) throws SymmetricalException {
        // 加密前处理
        byte[] before = encryptBefore(raw);
        // 执行加密
        byte[] execute;
        try {
            execute = actuator(algorithm, algorithmMode, secretKey, ivParameter, Cipher.ENCRYPT_MODE, before);
            // 加密后处理
            return encryptAfter(execute);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("encode error! algorithm {}, algorithmMode {}, secretKey {}, ivParameter {}, mode Cipher.ENCRYPT_MODE , before {}", algorithm, algorithmMode, secretKey, ivParameter, before);
            throw new SymmetricalException(e);
        }
    }

    public static String decode(String algorithm, String algorithmMode, SecretKey secretKey, String ivParameter, String wrought) throws SymmetricalException {
        // 解密前处理
        byte[] before = decryptBefore(wrought);
        // 执行解密
        byte[] execute;
        try {
            execute = actuator(algorithm, algorithmMode, secretKey, ivParameter, Cipher.DECRYPT_MODE, before);
            // 解密后处理
            return decryptAfter(execute);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("decode error! algorithm {}, algorithmMode {}, secretKey {}, ivParameter {}, mode Cipher.ENCRYPT_MODE , before {}", algorithm, algorithmMode, secretKey, ivParameter, before);
            throw new SymmetricalException(e);
        }
    }

}
