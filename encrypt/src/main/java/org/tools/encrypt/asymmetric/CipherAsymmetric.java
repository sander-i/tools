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
 * @author sander
 **/
public class CipherAsymmetric extends CipherAbst {

    private static final CipherAsymmetric INSTANCE = new CipherAsymmetric();

    private CipherAsymmetric() {
    }

    public static CipherAsymmetric getInstance() {
        return INSTANCE;
    }

    @Override
    public byte[] actuator(String algorithm, String algorithmMode, Key secretKey, String ivParameter, int mode, byte[] stuff, int maxBlock)
            throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        actuatorCheck(algorithm, algorithmMode, secretKey, stuff);
        // Cipher对象实际完成解密操作
        AlgorithmEnum algorithmEnum = AlgorithmEnum.enumName(algorithm);
        AlgorithmModeEnum algorithmModeEnum = AlgorithmModeEnum.enumName(algorithmMode);
        Cipher cipher = CipherSupply.getInstance().prepCipher(algorithmEnum, algorithmModeEnum, secretKey, ivParameter, mode);
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
