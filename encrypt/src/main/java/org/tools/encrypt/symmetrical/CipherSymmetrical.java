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

    public static CipherSymmetrical getInstance() {
        return INSTANCE;
    }

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

    @Override
    protected ByteArrayOutputStream subLevel(byte[] stuff, Cipher cipher, int maxBlock) {
        return null;
    }

}
