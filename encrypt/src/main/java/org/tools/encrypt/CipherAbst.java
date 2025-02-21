package org.tools.encrypt;


import org.springframework.util.Assert;

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
 * @author sander
 **/
public abstract class CipherAbst {

    /**
     * 检查加解密参数
     *
     * @param algorithm     加密算法
     * @param algorithmMode 加密算法模式
     * @param secretKey     密钥
     * @param stuff         待处理数据
     */
    protected void actuatorCheck(String algorithm, String algorithmMode, Key secretKey, byte[] stuff) {
        Assert.notNull(algorithm, "The encryption algorithm cannot be null！");
        Assert.notNull(algorithmMode, "The encryption algorithm mode cannot be null！");
        Assert.notNull(secretKey, "The key cannot be null！");
        Assert.notNull(stuff, "The data to be processed cannot be null！");
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
     * @param maxBlock      最大块
     * @return byte[]
     */
    public abstract byte[] actuator(String algorithm, String algorithmMode, Key secretKey, String ivParameter, int mode, byte[] stuff, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException;

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
    protected abstract ByteArrayOutputStream subLevel(byte[] stuff, Cipher cipher, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException;

}
