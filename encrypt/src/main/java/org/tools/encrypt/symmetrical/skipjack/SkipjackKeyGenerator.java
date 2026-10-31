package org.tools.encrypt.symmetrical.skipjack;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.CipherSupply;
import org.tools.encrypt.entity.symmetrical.SkipjackProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.skipjack.SkipjackException;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Skipjack 密钥生成工具：生成或解析 Hex 密钥并读取配置。
 *
 * <p>Skipjack 仅支持固定 80 位（10 字节）密钥，由 BouncyCastle Provider 提供
 * （JDK 未内置）。密钥字符串为 <b>Hex</b>，与 {@link #getSecretKeyStr(SecretKey)} 输出对称。
 * 若 BouncyCastle 的 KeyGenerator 不可用，回退为 {@link SecureRandom} 生成 10 字节随机密钥。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@Slf4j
public class SkipjackKeyGenerator {
    /**
     * Skipjack 密钥位数（固定 80 位）
     */
    private static final int KEY_BITS = 80;

    /**
     * Skipjack 密钥长度（字节）
     */
    private static final int KEY_BYTES = KEY_BITS / 8;

    /**
     * BouncyCastle Provider 名称
     */
    private static final String PROVIDER = "BC";

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * Skipjack 配置文件
     */
    private static SkipjackProperties properties;

    private SkipjackKeyGenerator() {
    }

    /**
     * 获取 80 位密钥对象。
     *
     * <p>优先使用 BouncyCastle 的 {@code SKIPJACK} KeyGenerator；若 Provider 不可用或
     * 生成结果不是 10 字节，回退为 {@link SecureRandom} 生成 10 字节随机密钥。
     * 每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @return SecretKey（固定 10 字节）
     * @throws SkipjackException 密钥生成失败时抛出
     */
    public static SecretKey getSecretKey() throws SkipjackException {
        CipherSupply.getInstance().initSupport(AlgorithmModeEnum.SKIPJACK);
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AlgorithmEnum.SKIPJACK.getAlgorithm(), PROVIDER);
            keyGenerator.init(KEY_BITS);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] encoded = secretKey.getEncoded();
            if (encoded != null && encoded.length == KEY_BYTES) {
                // BouncyCastle 返回的算法名为 "Skipjack"，统一规范为标准算法名 "SKIPJACK"
                return new SecretKeySpec(encoded, AlgorithmEnum.SKIPJACK.getAlgorithm());
            }
            log.error("BouncyCastle Skipjack 生成的密钥长度不是 {} 字节，改用 SecureRandom", KEY_BYTES);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidParameterException e) {
            log.error(e.getLocalizedMessage());
        }
        return generateBySecureRandom();
    }

    /**
     * 使用 {@link SecureRandom} 生成 10 字节 Skipjack 密钥。
     *
     * @return SecretKey
     */
    private static SecretKey generateBySecureRandom() {
        byte[] secretKeyBytes = new byte[KEY_BYTES];
        new SecureRandom().nextBytes(secretKeyBytes);
        return new SecretKeySpec(secretKeyBytes, AlgorithmEnum.SKIPJACK.getAlgorithm());
    }

    /**
     * 获取密钥字符串
     *
     * @param secretKey 密钥对象
     * @return Hex 密钥字符串（20 个字符）
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 获取密钥对象。
     *
     * <p>密钥字符串按 <b>Hex</b> 解码（与 {@link #getSecretKeyStr(SecretKey)} 输出对称），
     * 解码后必须恰好为 10 字节（80 位），否则拒绝。</p>
     *
     * @param algorithm 算法（为空时使用 {@code SKIPJACK}）
     * @param secretKey Hex 密钥字符串，解码后必须为 10 字节
     * @return 密钥对象
     * @throws SkipjackException Hex 解码失败或密钥长度不是 10 字节时抛出
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws SkipjackException {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new SkipjackException("secretKey 不能为空！");
        }
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
        }
        if (secretKeyBytes.length != KEY_BYTES) {
            throw new SkipjackException(
                    "Skipjack 密钥必须为 " + KEY_BITS + " 位（" + KEY_BYTES + " 字节），实际为 "
                            + secretKeyBytes.length + " 字节！");
        }
        String keyAlgorithm = BaseUtils.isEmpty(algorithm)
                ? AlgorithmEnum.SKIPJACK.getAlgorithm() : algorithm;
        return new SecretKeySpec(secretKeyBytes, keyAlgorithm);
    }

    /**
     * 懒加载 Skipjack 配置文件（双检锁；缺密钥时自动生成）
     *
     * @return SkipjackProperties
     * @throws SkipjackException 密钥生成失败时抛出
     */
    public static SkipjackProperties getProperties() throws SkipjackException {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new SkipjackProperties();
                }
            }
        }
        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
