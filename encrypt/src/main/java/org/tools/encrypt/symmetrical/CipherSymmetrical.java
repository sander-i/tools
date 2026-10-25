package org.tools.encrypt.symmetrical;


import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.CipherAbst;
import org.tools.encrypt.CipherSupply;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 **/
public class CipherSymmetrical extends CipherAbst {

    private static final CipherSymmetrical INSTANCE = new CipherSymmetrical();

    private CipherSymmetrical() {
    }

    /**
     * 获取单例
     *
     * @return CipherSymmetrical 单例
     */
    public static CipherSymmetrical getInstance() {
        return INSTANCE;
    }

    /**
     * 执行对称加解密（单块处理，返回整段结果）
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
     */
    @Override
    public byte[] actuator(String algorithm, String algorithmMode, Key secretKey, String ivParameter, int mode, byte[] stuff, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        actuatorCheck(algorithm, algorithmMode, secretKey, stuff);
        // Cipher对象实际完成解密操作
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherSupply.getInstance()
                .prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, mode);
        // 执行加解密操作
        return cipher.doFinal(stuff);
    }

    /**
     * 分段处理（对称算法当前未启用分段，返回 null）
     *
     * @param stuff    待处理的字节数据
     * @param cipher   已初始化的 Cipher
     * @param maxBlock 单块最大字节数
     * @return 固定返回 null（对称算法不使用分段）
     */
    @Override
    protected ByteArrayOutputStream subLevel(byte[] stuff, Cipher cipher, int maxBlock) {
        return null;
    }

}
