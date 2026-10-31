package org.tools.encrypt.symmetrical.rc.rc4;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.Rc4Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc4.Rc4Exception;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * RC4 密钥生成工具：生成或解析 Hex 密钥并读取配置。
 *
 * <p>RC4 允许 40~1024 位密钥（SunJCE 实现），默认 128 位；实际可用的最大位数
 * 还受 JCE 策略限制（见 {@link Cipher#getMaxAllowedKeyLength(String)}）。
 * 密钥字符串为 <b>Hex</b>，与 {@link #getSecretKeyStr(SecretKey)} 输出对称。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class Rc4KeyGenerator {
    /**
     * 默认密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 最小密钥位数
     */
    private static final int MIN_KEY_BITS = 40;

    /**
     * 最大密钥位数
     */
    private static final int MAX_KEY_BITS = 1024;

    /**
     * 允许的最小密钥字节数（40 位 = 5 字节）
     */
    private static final int MIN_KEY_BYTES = 5;

    /**
     * 允许的最大密钥字节数（1024 位 = 128 字节）
     */
    private static final int MAX_KEY_BYTES = 128;

    /**
     * KeyGenerator 备用名称：SunJCE 以 {@code ARCFOUR} 登记，{@code RC4} 为别名
     */
    private static final String KEY_GENERATOR_FALLBACK = "ARCFOUR";

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * RC4 配置文件
     */
    private static Rc4Properties properties;

    private Rc4KeyGenerator() {
    }

    /**
     * 创建 RC4 密钥生成器。
     *
     * <p>SunJCE 的 {@link KeyGenerator} 标准名称为 {@code ARCFOUR}（{@code RC4} 只是别名），
     * 若当前 JDK 的 {@code KeyGenerator.getInstance("RC4")} 抛 {@link NoSuchAlgorithmException}，
     * 回退到 {@code ARCFOUR}。</p>
     *
     * @return 新建的 KeyGenerator（JCA 生成器非线程安全，调用方独立持有）
     * @throws NoSuchAlgorithmException 两种名称均不可用时抛出
     */
    private static KeyGenerator newKeyGenerator() throws NoSuchAlgorithmException {
        try {
            return EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.RC4.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            return EncryptKeyGenerator.getKeyGenerator(KEY_GENERATOR_FALLBACK);
        }
    }

    /**
     * 获取默认 128 位密钥对象。
     *
     * @return SecretKey
     * @throws Rc4Exception 密钥生成失败时抛出
     */
    public static SecretKey getSecretKey() throws Rc4Exception {
        return getSecretKey(KEY_BITS);
    }

    /**
     * 获取指定密钥位数的密钥对象。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @param keyBits 密钥位数，须在 40~1024 之间且不超过当前 JCE 允许的最大值
     * @return SecretKey
     * @throws Rc4Exception 位数非法或密钥生成失败时抛出
     */
    public static SecretKey getSecretKey(int keyBits) throws Rc4Exception {
        if (keyBits < MIN_KEY_BITS || keyBits > MAX_KEY_BITS) {
            throw new Rc4Exception("keyBits 必须在 " + MIN_KEY_BITS + ".." + MAX_KEY_BITS + " 之间！");
        }
        try {
            int maxAllowed = Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC4.getAlgorithm());
            if (keyBits > maxAllowed) {
                throw new Rc4Exception("keyBits 超过当前 JCE 允许的最大值 " + maxAllowed + "！");
            }
            KeyGenerator keyGenerator = newKeyGenerator();
            keyGenerator.init(keyBits);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
    }

    /**
     * 获取密钥字符串
     *
     * @param secretKey 密钥对象
     * @return Hex 密钥字符串
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 获取密钥对象。
     *
     * <p>密钥字符串按 <b>Hex</b> 解码（与 {@link #getSecretKeyStr(SecretKey)} 输出对称，
     * 32 个 Hex 字符 = 16 字节 = 128 位）。</p>
     *
     * @param algorithm 算法
     * @param secretKey Hex 密钥字符串
     * @return 密钥对象
     * @throws Rc4Exception 密钥字符串为空、不是合法 Hex 或解码后长度不在 5..128 字节时抛出
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws Rc4Exception {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new Rc4Exception("secretKey 不能为空！");
        }
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
        // 解析阶段即拒绝越界长度，避免把非法密钥推迟到 Cipher.init 才暴露
        if (secretKeyBytes.length < MIN_KEY_BYTES || secretKeyBytes.length > MAX_KEY_BYTES) {
            throw new Rc4Exception("secretKey 解码后长度必须在 " + MIN_KEY_BYTES + ".." + MAX_KEY_BYTES
                    + " 字节之间！");
        }
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 懒加载 RC4 配置文件（双检锁；缺密钥时自动生成）
     *
     * @return Rc4Properties
     * @throws Rc4Exception 密钥生成失败时抛出
     */
    public static Rc4Properties getProperties() throws Rc4Exception {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new Rc4Properties();
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
