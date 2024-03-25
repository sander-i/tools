package org.tools.encrypt.asymmetric.rsa.abst;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.asymmetric.rsa.Rsa;
import org.tools.encrypt.asymmetric.rsa.RsaProperties;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 17:32
 * @description rsa加密抽象类
 * @scope rsa
 */
@Deprecated
@Slf4j
public abstract class RsaAbst implements Rsa {

    private RsaProperties rsaProperties;
    private Signature signature;
    private Cipher publicCipher;
    private Cipher privateCipher;

    @Override
    public RsaProperties getRsaProperties() {
        return rsaProperties;
    }

    @Override
    public Rsa setRsaProperties(RsaProperties rsaProperties) {
        this.rsaProperties = rsaProperties;
        return this;
    }

    protected Signature getSignature() throws NoSuchAlgorithmException {
        if (BaseUtils.isNull(signature)) {
            signature = Signature.getInstance(RsaProperties.SIGN_ALGORITHM);
        }
        return signature;
    }

    protected Cipher getPublicCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (BaseUtils.isNull(publicCipher)) {
            publicCipher = Cipher.getInstance(rsaProperties.getAlgorithmMode());
        }
        return publicCipher;
    }

    protected Cipher getPrivateCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (BaseUtils.isNull(privateCipher)) {
            privateCipher = Cipher.getInstance(rsaProperties.getAlgorithmMode());
        }
        return privateCipher;
    }

    protected byte[] decodeBase64(String str) {
        return Base64.decodeBase64(str);
    }

    protected String encodeBase64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 执行加密操作
     *
     * @param todo      待处理
     * @param publicKey 公钥
     * @return byte[]
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws RsaException
     */
    protected byte[] actuator(byte[] todo, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, RsaException {
        // Cipher对象实际完成加密操作
        Cipher cipher = getPublicCipher();
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 对数据分段加密
        ByteArrayOutputStream out = subLevel(todo, cipher, rsaProperties.getMaxEncryptBlock());
        byte[] execute = out.toByteArray();
        out.close();
        // 加密处理后的字符
        return execute;
    }

    /**
     * 执行解密操作
     *
     * @param todo       待处理
     * @param privateKey 私钥
     * @return byte[]
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws RsaException
     */
    protected String actuator(byte[] todo, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, RsaException {
        // Cipher对象实际完成解密操作
        Cipher cipher = getPrivateCipher();
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 对数据分段解密
        ByteArrayOutputStream out = subLevel(todo, cipher, rsaProperties.getMaxDecryptBlock());
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
    protected ByteArrayOutputStream subLevel(byte[] todo, Cipher cipher, int maxBlock) throws IllegalBlockSizeException, BadPaddingException {
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

}
