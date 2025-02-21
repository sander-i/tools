package org.tools.encrypt;

import org.springframework.util.Assert;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/05
 */
@Deprecated
public class CipherUtils {

    private CipherUtils() {
    }

    /**
     * 检查加解密参数
     *
     * @param algorithm     加密算法
     * @param algorithmMode 加密算法模式
     * @param secretKey     密钥
     * @param stuff         待处理数据
     */
    public static void actuatorCheck(String algorithm, String algorithmMode, Key secretKey, byte[] stuff) {
        Assert.notNull(algorithm, "加密算法不能为空！");
        Assert.notNull(algorithmMode, "加密算法模式不能为空！");
        Assert.notNull(secretKey, "密钥不能为空！");
        Assert.notNull(stuff, "待处理数据不能为空！");
    }

    /**
     * 执行加解密操作
     *
     * @param algorithm     加密算法枚举
     * @param algorithmMode 加密算法模式枚举
     * @param secretKey     密钥
     * @param mode          加密对象模式
     * @param stuff         待处理
     * @return byte[]
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持异常
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException          非法块大小异常
     * @throws BadPaddingException                错误填充异常
     */
    public static byte[] actuator(String algorithm, String algorithmMode, Key secretKey, int mode, byte[] stuff)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return actuator(algorithm, algorithmMode, secretKey, null, mode, stuff);
    }

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
     * @throws NoSuchAlgorithmException           算法不存在或不支持异常
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException          非法块大小异常
     * @throws BadPaddingException                错误填充异常
     */
    public static byte[] actuator(String algorithm, String algorithmMode, Key secretKey, String ivParameter, int mode, byte[] stuff)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        actuatorCheck(algorithm, algorithmMode, secretKey, stuff);
        // Cipher对象实际完成解密操作
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherSupply.getInstance().prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, mode);
        // 执行加解密操作
        return cipher.doFinal(stuff);
    }

    /**
     * 执行加解密操作
     *
     * @param algorithm     加密算法枚举
     * @param algorithmMode 加密算法模式枚举
     * @param secretKey     密钥
     * @param ivParameter   向量
     * @param stuff         待处理
     * @param maxBlock      最大块
     * @return byte[]
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持异常
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException          非法块大小异常
     * @throws BadPaddingException                错误填充异常
     * @throws IOException                        IO流异常
     */
    public static byte[] actuator(String algorithm, String algorithmMode, PublicKey secretKey, String ivParameter, byte[] stuff, int maxBlock)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        actuatorCheck(algorithm, algorithmMode, secretKey, stuff);
        // 用密匙初始化Cipher对象
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherSupply.getInstance().prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, Cipher.ENCRYPT_MODE);
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
     * @param maxBlock      最大块
     * @return String
     * @throws NoSuchPaddingException             指定填充方案不存在或不支持
     * @throws NoSuchAlgorithmException           算法不存在或不支持异常
     * @throws InvalidKeyException                密钥错误或密钥的格式不正确
     * @throws InvalidAlgorithmParameterException 算法参数不适用于当前操作或算法参数值不适合算法
     * @throws IllegalBlockSizeException          非法块大小异常
     * @throws BadPaddingException                错误填充异常
     * @throws IOException                        IO流异常
     */
    public static String actuator(String algorithm, String algorithmMode, PrivateKey secretKey, String ivParameter, byte[] stuff, int maxBlock)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        actuatorCheck(algorithm, algorithmMode, secretKey, stuff);
        // 用密匙初始化Cipher对象
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherSupply.getInstance().prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, Cipher.DECRYPT_MODE);
        // 对数据分段解密
        ByteArrayOutputStream out = subLevel(stuff, cipher, maxBlock);
        String execute = out.toString(StandardCharsets.UTF_8.name());
        out.close();
        // 解密处理后的字符
        return execute;
    }

    /**
     * 对数据进行分段处理
     *
     * @param stuff    待处理
     * @param cipher   密码对象
     * @param maxBlock 最大明密文大小
     * @return 字节排列输出流
     * @throws IllegalBlockSizeException 非法块大小异常
     * @throws BadPaddingException       错误填充异常
     */
    protected static ByteArrayOutputStream subLevel(byte[] stuff, Cipher cipher, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException {
        int inputLen = stuff.length;
        int offset = 0;
        byte[] cache;
        int i = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 对数据分段
        while (inputLen - offset > 0) {
            if (inputLen - offset > maxBlock) {
                cache = cipher.doFinal(stuff, offset, maxBlock);
            } else {
                cache = cipher.doFinal(stuff, offset, inputLen - offset);
            }
            baos.write(cache, 0, cache.length);
            i++;
            offset = i * maxBlock;
        }
        return baos;
    }

}
