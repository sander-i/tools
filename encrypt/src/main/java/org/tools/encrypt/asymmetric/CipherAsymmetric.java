package org.tools.encrypt.asymmetric;

import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.CipherAbst;
import org.tools.encrypt.CipherSupply;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * 非对称 Cipher 实现：按算法与模式创建 Cipher 并分块处理数据。
 *
 * @author sander
 * @since 1.0.6.6
 */
public class CipherAsymmetric extends CipherAbst {
    private static final CipherAsymmetric INSTANCE = new CipherAsymmetric();

    private CipherAsymmetric() {
    }

    /**
     * 获取单例
     *
     * @return CipherAsymmetric 单例
     */
    public static CipherAsymmetric getInstance() {
        return INSTANCE;
    }

    /**
     * 执行非对称加解密（超长数据按 maxBlock 分段处理）
     *
     * @param algorithm     加密算法名
     * @param algorithmMode 算法模式名
     * @param secretKey     密钥
     * @param ivParameter   初始向量
     * @param mode          加解密模式（Cipher.ENCRYPT_MODE / DECRYPT_MODE）
     * @param stuff         待处理的字节数据
     * @param maxBlock      单块最大字节数
     * @return 处理后的字节数据
     * @throws IllegalBlockSizeException          块大小非法时抛出
     * @throws BadPaddingException                填充错误时抛出
     * @throws InvalidAlgorithmParameterException 算法参数非法时抛出
     * @throws NoSuchPaddingException             填充方式不可用时抛出
     * @throws NoSuchAlgorithmException           算法不可用时抛出
     * @throws InvalidKeyException                密钥非法时抛出
     * @throws IOException                        分段输出流操作失败时抛出
     */
    @Override
    public byte[] actuator(String algorithm, String algorithmMode, Key secretKey, String ivParameter, int mode,
            byte[] stuff, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
                    NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        actuatorCheck(algorithm, algorithmMode, secretKey, stuff);
        // Cipher对象实际完成解密操作
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherSupply.getInstance().prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter,
                mode);
        // 对数据分段加密
        ByteArrayOutputStream out = subLevel(stuff, cipher, maxBlock);
        byte[] execute = out.toByteArray();
        out.close();

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
    @Override
    protected ByteArrayOutputStream subLevel(byte[] stuff, Cipher cipher, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException {
        int inputLen = stuff.length;
        int offset = 0;
        byte[] cache;
        int i = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 对数据分段
        while (inputLen - offset > 0) {
            // 每次加密的字节数，如果不是最后一块，加密maxBlock字节
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
