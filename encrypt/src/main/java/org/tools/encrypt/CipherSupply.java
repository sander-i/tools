package org.tools.encrypt;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @author sander
 **/
public class CipherSupply extends CipherSupplyAbst {

    private static final CipherSupply INSTANCE = new CipherSupply();

    private CipherSupply() {
    }

    public static CipherSupply getInstance() {
        return INSTANCE;
    }

    public void initSupport(AlgorithmModeEnum algorithmMode) {
        switch (algorithmMode) {
            case IDEA:
            case IDEA_ECB_ISO10126Padding: {
                // IDEA 算法需要 BouncyCastleProvider 支持
                Security.addProvider(new BouncyCastleProvider());
                break;
            }
        }
    }

    public void initCipher(Cipher cipher, AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode, Key secretKey, String ivParameter, int mode)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        Key key;
        SecureRandom random;
        IvParameterSpec iv;
        switch (algorithmMode) {
            case AES:
            case DES:
            case RSA: {
                key = new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm());
                // 算法要求有一个可信任的随机数源
                random = new SecureRandom();
                cipher.init(mode, key, random);
                break;
            }
            case IDEA:
            case IDEA_ECB_ISO10126Padding:
            case DES_ECB_NO_PADDING: {
                key = new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm());
                cipher.init(mode, key);
                break;
            }
            case RSA_ECB_OAEPWITHSHA256ANDMGF1PADDING: {
                cipher.init(mode, secretKey);
                break;
            }
            case AES_CBC_PKCS5PADDING:
            case DES_EDE_CBC_PKCS5Padding: {
                key = new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm());
                iv = new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8));
                cipher.init(mode, key, iv);
                break;
            }
        }
    }

}
