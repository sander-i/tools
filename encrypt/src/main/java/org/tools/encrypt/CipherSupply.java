package org.tools.encrypt;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.CipherSupplyException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.MGF1ParameterSpec;

/**
 * @author sander
 **/
public class CipherSupply extends CipherSupplyAbst {

    private static final CipherSupply INSTANCE = new CipherSupply();

    private CipherSupply() {
    }

    /** 获取单例 */
    public static CipherSupply getInstance() {
        return INSTANCE;
    }

    /** 按算法模式注册所需 Provider（IDEA / RSA-OAEP 需 BouncyCastle） */
    @Override
    public void initSupport(AlgorithmModeEnum algorithmMode) {
        switch (algorithmMode) {
            case IDEA:
            case IDEA_ECB_ISO10126Padding:
            case OAEPWithSHA__256AndMGF1Padding: {
                Security.addProvider(new BouncyCastleProvider());
                break;
            }
            default:
                break;
        }
    }

    /** 创建指定算法模式的 Cipher 实例（OAEP 模式经 BC Provider 创建） */
    @Override
    public Cipher getInstanceCipher(AlgorithmModeEnum algorithmMode) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Cipher cipher;
        switch (algorithmMode) {
            case OAEPWithSHA__256AndMGF1Padding: {
                cipher = Cipher.getInstance(AlgorithmModeEnum.RSA_ECB_OAEPWITHSHA__256ANDMGF1PADDING.getAlgorithmMode(), "BC");
                break;
            }
            default: {
                cipher = Cipher.getInstance(algorithmMode.getAlgorithmMode());
                break;
            }
        }
        return cipher;
    }

    /** 按算法模式初始化 Cipher（处理 IV、随机源与 OAEP 参数；未支持的模式抛 CipherSupplyException） */
    @Override
    public void initCipher(Cipher cipher, AlgorithmEnum algorithm, AlgorithmModeEnum algorithmMode, Key secretKey, String ivParameter, int mode)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        switch (algorithmMode) {
            case RSA_ECB_NO_PADDING:
            case RSA_ECB_OAEPWITHSHA__256ANDMGF1PADDING: {
                cipher.init(mode, secretKey);
                break;
            }
            case IDEA:
            case IDEA_ECB_ISO10126Padding:
            case DES_ECB_NO_PADDING: {
                cipher.init(mode, new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm()));
                break;
            }
            case AES:
            case DES:
            case RSA: {
                cipher.init(mode, new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm()),
                        // 算法要求有一个可信任的随机数源
                        new SecureRandom());
                break;
            }
            case AES_CBC_PKCS5PADDING:
            case DES_EDE_CBC_PKCS5Padding: {
                cipher.init(mode, new SecretKeySpec(secretKey.getEncoded(), algorithm.getAlgorithm()),
                        new IvParameterSpec(ivParameter.getBytes(StandardCharsets.UTF_8)));
                break;
            }
            case OAEPWithSHA__256AndMGF1Padding: {
                cipher.init(mode, secretKey,
                        new OAEPParameterSpec(AlgorithmEnum.SHA256.getAlgorithm(), "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
                break;
            }
            default:
                throw new CipherSupplyException(String.format("Encrypt mode %s did not find the corresponding initialization scheme!", algorithmMode.getAlgorithmMode()));
        }
    }

}
