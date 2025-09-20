package org.tools.encrypt.symmetrical;

import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
     * 加密前处理
     *
     * @param process 待处理
     * @return byte[]
     */
    protected static byte[] encryptBefore(String process) {
        return process.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 加密后处理
     *
     * @param process 待处理
     * @return String
     */
    protected static String encryptAfter(byte[] process) {
        return Base64.getEncoder().encodeToString(process);
    }

    /**
     * 解密前处理
     *
     * @param process 待处理
     * @return byte[]
     */
    protected static byte[] decryptBefore(String process) {
        return Base64.getDecoder().decode(process);
    }

    /**
     * 解密后处理
     *
     * @param process 待处理
     * @return String
     */
    protected static String decryptAfter(byte[] process) {
        return new String(process);
    }

    /**
     * 加密
     *
     * @param algorithm     算法
     * @param algorithmMode 算法模式
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param raw           明文
     * @return 密文
     * @throws SymmetricalException 异常
     */
    public static String encode(String algorithm, String algorithmMode, SecretKey secretKey, String ivParameter, String raw)
            throws SymmetricalException {

        byte[] before = encryptBefore(raw);

        byte[] execute;
        try {
            execute = CipherSymmetrical.getInstance()
                    .actuator(algorithm, algorithmMode, secretKey, ivParameter, Cipher.ENCRYPT_MODE, before, 0);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("encode error! algorithm {}, algorithmMode {}, secretKey {}, ivParameter {}, mode Cipher.ENCRYPT_MODE , before {}", algorithm, algorithmMode, secretKey, ivParameter, before);
            throw new SymmetricalException(e);
        }

        return encryptAfter(execute);
    }

    /**
     * 解密
     *
     * @param algorithm     算法
     * @param algorithmMode 算法模式
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param wrought       密文
     * @return 明文
     * @throws SymmetricalException 异常
     */
    public static String decode(String algorithm, String algorithmMode, SecretKey secretKey, String ivParameter, String wrought)
            throws SymmetricalException {

        byte[] before = decryptBefore(wrought);

        byte[] execute;
        try {
            execute = CipherSymmetrical.getInstance()
                    .actuator(algorithm, algorithmMode, secretKey, ivParameter, Cipher.DECRYPT_MODE, before, 0);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("decode error! algorithm {}, algorithmMode {}, secretKey {}, ivParameter {}, mode Cipher.DECRYPT_MODE , before {}", algorithm, algorithmMode, secretKey, ivParameter, before);
            throw new SymmetricalException(e);
        }

        return decryptAfter(execute);
    }

}
