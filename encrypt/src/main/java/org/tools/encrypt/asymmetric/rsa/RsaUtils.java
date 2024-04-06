package org.tools.encrypt.asymmetric.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.AlgorithmModeEnum;
import org.tools.encrypt.CipherUtils;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Objects;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/30
 */
@Slf4j
public class RsaUtils {

    private RsaUtils() {
    }

    protected static Signature signature;

    protected static byte[] decodeBase64(String str) {
        return Base64.decodeBase64(str);
    }

    protected static String encodeBase64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 执行加解密操作
     *
     * @param algorithm     加密算法枚举
     * @param algorithmMode 加密算法模式枚举
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param stuff         待处理
     * @return byte[]
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    protected static byte[] actuator(String algorithm, String algorithmMode, PublicKey secretKey, String ivParameter, byte[] stuff, int maxBlock)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        // 用密匙初始化Cipher对象
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherUtils.prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, Cipher.ENCRYPT_MODE);
        // 对数据分段加密
        ByteArrayOutputStream out = subLevel(stuff, cipher, maxBlock);
        byte[] execute = out.toByteArray();
        out.close();
        // 加密处理后的字符
        return execute;
    }

    /**
     * 执行加解密操作
     *
     * @param algorithm     加密算法枚举
     * @param algorithmMode 加密算法模式枚举
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param stuff         待处理
     * @return String
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    protected static String actuator(String algorithm, String algorithmMode, PrivateKey secretKey, String ivParameter, byte[] stuff, int maxBlock)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        // 用密匙初始化Cipher对象
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherUtils.prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, Cipher.DECRYPT_MODE);
        // 对数据分段解密
        ByteArrayOutputStream out = subLevel(stuff, cipher, maxBlock);
        String execute = out.toString("UTF-8");
        out.close();
        // 解密处理后的字符
        return execute;
    }

    /**
     * 对数据进行分段处理
     *
     * @param todo     待处理
     * @param cipher   密码对象
     * @param maxBlock 最大明密文大小
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected static ByteArrayOutputStream subLevel(byte[] todo, Cipher cipher, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException {
        int inputLen = todo.length;
        int offset = 0;
        byte[] cache;
        int i = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 对数据分段
        while (inputLen - offset > 0) {
            if (inputLen - offset > maxBlock) {
                cache = cipher.doFinal(todo, offset, maxBlock);
            } else {
                cache = cipher.doFinal(todo, offset, inputLen - offset);
            }
            baos.write(cache, 0, cache.length);
            i++;
            offset = i * maxBlock;
        }
        return baos;
    }

    public static String encode(String algorithm, String algorithmMode, PublicKey publicKey, String ivParameter, String raw, int maxEncryptBlock)
            throws EncryptException {
        // 加密前处理
        byte[] before = raw.getBytes();
        // 执行加密
        byte[] execute;
        try {
            if (maxEncryptBlock <= 0) {
                maxEncryptBlock = 117;
            }
            execute = actuator(algorithm, algorithmMode, publicKey, ivParameter, before, maxEncryptBlock);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException | InvalidAlgorithmParameterException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
        // 加密后处理
        return encodeBase64(execute);
    }

    public static String decode(String algorithm, String algorithmMode, PrivateKey privateKey, String ivParameter, String wrought, int maxDecryptBlock)
            throws EncryptException {
        // 解密前处理
        byte[] before = decodeBase64(wrought);
        // 执行解密
        String execute;
        try {
            if (maxDecryptBlock <= 0) {
                maxDecryptBlock = 128;
            }
            execute = actuator(algorithm, algorithmMode, privateKey, ivParameter, before, maxDecryptBlock);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException | IOException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
        // 解密后处理
        return execute;
    }

    public static String sign(String signAlgorithm, PrivateKey privateKey, String wrought) throws EncryptException {
        try {
            if (Objects.isNull(signature)) {
                signature = Signature.getInstance(signAlgorithm);
            }
            signature.initSign(privateKey);
            return SignatureUtils.sign(signature, wrought);
        } catch (DigitalSignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

    public static Boolean verify(String signAlgorithm, PublicKey publicKey, String wrought, String sign) throws EncryptException {
        try {
            if (Objects.isNull(signature)) {
                signature = Signature.getInstance(signAlgorithm);
            }
            signature.initVerify(publicKey);
            return SignatureUtils.verify(signature, wrought, sign);
        } catch (DigitalSignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

}
