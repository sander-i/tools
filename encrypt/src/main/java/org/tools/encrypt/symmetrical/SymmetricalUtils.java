package org.tools.encrypt.symmetrical;

import lombok.extern.slf4j.Slf4j;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * 对称加解密统一入口。
 *
 * <p>编码约定：明文与解密输出均使用 <b>UTF-8</b>（{@link #encryptBefore} / {@link #decryptAfter}），
 * 密文为标准 Base64。加解密两端 JVM 需使用相同字符集约定（本类已固定为 UTF-8）。</p>
 *
 * <p>线程安全：encode/decode 每次调用新建 Cipher，可并发使用。</p>
 *
 * @author sander 2024/04/14
 * @since 1.0.6.1
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
     * <p>与 {@link #encryptBefore(String)} 对称，固定使用 UTF-8。
     * 此前使用 JVM 默认 charset，在非 UTF-8 平台（如中文 Windows GBK）上会导致含
     * 非 ASCII 字符的明文 round-trip 乱码，已修复。</p>
     *
     * @param process 待处理
     * @return String
     */
    protected static String decryptAfter(byte[] process) {
        return new String(process, StandardCharsets.UTF_8);
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
    public static String encode(String algorithm, String algorithmMode, SecretKey secretKey, String ivParameter,
            String raw)
            throws SymmetricalException {
        byte[] before = encryptBefore(raw);

        byte[] execute;
        try {
            execute = CipherSymmetrical.getInstance()
                    .actuator(algorithm, algorithmMode, secretKey, ivParameter, Cipher.ENCRYPT_MODE, before, 0);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("encode error! algorithm {}, algorithmMode {}, mode Cipher.ENCRYPT_MODE",
                    algorithm, algorithmMode, e);
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
    public static String decode(String algorithm, String algorithmMode, SecretKey secretKey, String ivParameter,
            String wrought)
            throws SymmetricalException {
        byte[] before = decryptBefore(wrought);

        byte[] execute;
        try {
            execute = CipherSymmetrical.getInstance()
                    .actuator(algorithm, algorithmMode, secretKey, ivParameter, Cipher.DECRYPT_MODE, before, 0);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("decode error! algorithm {}, algorithmMode {}, mode Cipher.DECRYPT_MODE",
                    algorithm, algorithmMode, e);
            throw new SymmetricalException(e);
        }

        return decryptAfter(execute);
    }
}
