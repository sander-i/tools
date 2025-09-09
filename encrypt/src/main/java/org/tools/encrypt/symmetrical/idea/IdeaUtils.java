package org.tools.encrypt.symmetrical.idea;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;
import org.tools.encrypt.symmetrical.CipherSymmetrical;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 **/
@Slf4j
public class IdeaUtils extends SymmetricalUtils {

    private IdeaUtils() {
    }

    /**
     * 加密后处理
     * import org.apache.commons.codec.binary.Base64;
     *
     * @param todo 待处理
     * @return String
     */
    protected static String encryptAfter(byte[] todo) {
        return Base64.encodeBase64String(todo);
    }

    /**
     * 解密前处理
     * import org.apache.commons.codec.binary.Base64;
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected static byte[] decryptBefore(String todo) {
        return Base64.decodeBase64(todo);
    }

    /**
     * 加密
     *
     * @param algorithm     算法
     * @param algorithmMode 算法模式
     * @param secretKey     密钥
     * @param raw           原文
     * @return 密文
     * @throws IdeaException idea异常
     */
    public static String encode(String algorithm, String algorithmMode, SecretKey secretKey, String raw)
            throws IdeaException {

        byte[] before = encryptBefore(raw);

        byte[] execute;
        try {
            execute = CipherSymmetrical.getInstance()
                    .actuator(algorithm, algorithmMode, secretKey, null, Cipher.ENCRYPT_MODE, before, 0);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("encode error! algorithm {}, algorithmMode {}, secretKey {}, mode Cipher.ENCRYPT_MODE , before {}", algorithm, algorithmMode, secretKey, before);
            throw new IdeaException(e);
        }

        return encryptAfter(execute);
    }

    /**
     * 解密
     *
     * @param algorithm     算法
     * @param algorithmMode 算法模式
     * @param secretKey     密钥
     * @param wrought       密文
     * @return 原文
     * @throws IdeaException idea异常
     */
    public static String decode(String algorithm, String algorithmMode, SecretKey secretKey, String wrought)
            throws IdeaException {

        byte[] before = decryptBefore(wrought);

        byte[] execute;
        try {
            execute = CipherSymmetrical.getInstance()
                    .actuator(algorithm, algorithmMode, secretKey, null, Cipher.DECRYPT_MODE, before, 0);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException | InvalidAlgorithmParameterException e) {
            log.error("decode error! algorithm {}, algorithmMode {}, secretKey {}, mode Cipher.ENCRYPT_MODE , before {}", algorithm, algorithmMode, secretKey, before);
            throw new IdeaException(e);
        }

        return decryptAfter(execute);
    }

}
