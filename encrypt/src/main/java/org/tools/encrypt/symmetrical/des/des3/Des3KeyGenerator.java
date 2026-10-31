package org.tools.encrypt.symmetrical.des.des3;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.Des3Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.des3.Des3Exception;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES（DESede）密钥生成工具。
 *
 * <p>密钥字符串统一为 Hex 编码：16 字节（32 Hex，两密钥 3DES）或 24 字节（48 Hex，三密钥 3DES）。
 * JCA 的 {@code DESede} {@link KeyGenerator} 仅接受 112 或 168 位两种密钥长度，
 * 两者生成的密钥对象均为 24 字节。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@Slf4j
public class Des3KeyGenerator {
    /**
     * 密钥位数：三密钥 3DES（168 位，密钥对象 24 字节）
     */
    private static final int KEY_BITS = 168;

    /**
     * 密钥位数：两密钥 3DES（112 位）
     */
    private static final int TWO_KEY_BITS = 112;

    /**
     * 密钥字节数：两密钥 3DES（16 字节）
     */
    private static final int TWO_KEY_BYTES = 16;

    /**
     * 密钥字节数：三密钥 3DES（24 字节）
     */
    private static final int THREE_KEY_BYTES = 24;

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * Des3 配置文件
     */
    private static Des3Properties properties;

    private Des3KeyGenerator() {
    }

    /**
     * 获取 168 位（三密钥）3DES 密钥对象。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @return SecretKey（DESede 密钥，24 字节）
     * @throws Des3Exception 算法不可用或生成失败时抛出
     */
    public static SecretKey getSecretKey() throws Des3Exception {
        return getSecretKey(KEY_BITS);
    }

    /**
     * 获取指定密钥位数的 3DES 密钥对象。
     *
     * @param keyBits 密钥位数，仅支持 112 或 168
     * @return SecretKey（DESede 密钥，24 字节）
     * @throws Des3Exception 密钥位数不受支持或算法不可用时抛出
     */
    public static SecretKey getSecretKey(int keyBits) throws Des3Exception {
        if (keyBits != TWO_KEY_BITS && keyBits != KEY_BITS) {
            throw new Des3Exception("3DES 密钥位数仅支持 112 或 168，当前为 " + keyBits + "！");
        }
        try {
            KeyGenerator keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.DES_EDE.getAlgorithm());
            keyGenerator.init(keyBits);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
    }

    /**
     * 获取密钥字符串（小写 Hex）
     *
     * @param secretKey 密钥对象
     * @return 密钥字符串（24 字节密钥对应 48 个 Hex 字符）
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 由 Hex 字符串构造密钥对象。
     *
     * @param algorithm 算法名（通常为 {@code DESede}）
     * @param secretKey Hex 密钥字符串，解码后必须为 16 或 24 字节
     * @return 密钥对象
     * @throws Des3Exception 密钥为空、非合法 Hex 或解码后长度不为 16/24 字节时抛出
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws Des3Exception {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new Des3Exception("secretKey 不能为空！");
        }
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
        if (secretKeyBytes.length != TWO_KEY_BYTES && secretKeyBytes.length != THREE_KEY_BYTES) {
            throw new Des3Exception(
                    "3DES 密钥解码后必须为 16 或 24 字节，当前为 " + secretKeyBytes.length + " 字节！");
        }
        if (secretKeyBytes.length == TWO_KEY_BYTES) {
            secretKeyBytes = expandTwoKey(secretKeyBytes);
        }
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 将两密钥 3DES 的 16 字节密钥扩展为 JCA 要求的 24 字节形式（K1‖K2‖K1）。
     *
     * <p>SunJCE 的 {@code DESede} Cipher 在 {@code init} 阶段只接受 24 字节密钥，
     * 直接传入 16 字节会抛 {@code InvalidKeyException: Invalid key length: 16 bytes}；
     * 两密钥 3DES 的等价形式即第三个密钥等于第一个密钥。</p>
     *
     * @param twoKeyBytes 16 字节两密钥
     * @return 24 字节三密钥形式
     */
    private static byte[] expandTwoKey(byte[] twoKeyBytes) {
        byte[] threeKeyBytes = new byte[THREE_KEY_BYTES];
        System.arraycopy(twoKeyBytes, 0, threeKeyBytes, 0, TWO_KEY_BYTES);
        System.arraycopy(twoKeyBytes, 0, threeKeyBytes, TWO_KEY_BYTES, TWO_KEY_BYTES / 2);
        return threeKeyBytes;
    }

    /**
     * 单例模式-线程安全
     */
    private static void initProperties() {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new Des3Properties();
                }
            }
        }
    }

    /**
     * 获取 Des3 配置文件（首次调用自动生成密钥）
     *
     * @return Des3Properties
     * @throws Des3Exception 密钥生成失败时抛出
     */
    public static Des3Properties getProperties() throws Des3Exception {
        initProperties();

        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
