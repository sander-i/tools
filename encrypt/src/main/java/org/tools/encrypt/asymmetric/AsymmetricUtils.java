package org.tools.encrypt.asymmetric;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.tools.encrypt.exception.asymmetric.AsymmetricException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称加解密工具：RSA 分块加解密与 Base64 编解码衔接。
 *
 * @author sander
 * @since 1.0.6.6
 */
@Slf4j
public class AsymmetricUtils {
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
        return Base64.encodeBase64String(process);
    }

    /**
     * 解密前处理
     *
     * @param process 待处理
     * @return byte[]
     */
    protected static byte[] decryptBefore(String process) {
        return Base64.decodeBase64(process);
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
     * @param algorithm       {@link org.tools.encrypt.enums.AlgorithmEnum}
     * @param algorithmMode   {@link org.tools.encrypt.enums.AlgorithmModeEnum}
     * @param publicKey       公钥
     * @param ivParameter     向量
     * @param raw             明文
     * @param maxEncryptBlock 最大加密块
     * @return 密文
     * @throws AsymmetricException 异常
     */
    public static String encode(String algorithm, String algorithmMode, PublicKey publicKey, String ivParameter,
            String raw, int maxEncryptBlock)
            throws AsymmetricException {
        if (maxEncryptBlock <= 0) {
            maxEncryptBlock = 117;
        }

        byte[] before = encryptBefore(raw);

        byte[] execute;
        try {
            execute = CipherAsymmetric.getInstance()
                    .actuator(algorithm, algorithmMode, publicKey, ivParameter, Cipher.ENCRYPT_MODE, before,
                            maxEncryptBlock);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException | InvalidAlgorithmParameterException e) {
            log.error(e.getLocalizedMessage());
            throw new AsymmetricException(e);
        }

        return encryptAfter(execute);
    }

    /**
     * 解密
     *
     * @param algorithm       {@link org.tools.encrypt.enums.AlgorithmEnum}
     * @param algorithmMode   {@link org.tools.encrypt.enums.AlgorithmModeEnum}
     * @param privateKey      私钥
     * @param ivParameter     向量
     * @param wrought         密文
     * @param maxDecryptBlock 最大解密块
     * @return 明文
     * @throws AsymmetricException 异常
     */
    public static String decode(String algorithm, String algorithmMode, PrivateKey privateKey, String ivParameter,
            String wrought, int maxDecryptBlock)
            throws AsymmetricException {
        if (maxDecryptBlock <= 0) {
            maxDecryptBlock = 128;
        }

        byte[] before = decryptBefore(wrought);

        byte[] execute;
        try {
            execute = CipherAsymmetric.getInstance()
                    .actuator(algorithm, algorithmMode, privateKey, ivParameter, Cipher.DECRYPT_MODE, before,
                            maxDecryptBlock);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException | IOException e) {
            log.error(e.getLocalizedMessage());
            throw new AsymmetricException(e);
        }

        return decryptAfter(execute);
    }
}
