package org.tools.encrypt.symmetrical.rc.rc5;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.symmetrical.Rc5Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc5.Rc5Exception;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * RC5 密钥生成工具：生成随机密钥、解析 Hex 密钥字符串并读取配置。
 *
 * <p>RC5 由 BouncyCastle Provider 提供，本类在生成或校验密钥前确保 BC 已注册。
 * BC 的 {@code KeyGenerator.getInstance("RC5", "BC")} <b>不支持</b>
 * {@code init(AlgorithmParameterSpec)}（会抛
 * {@code InvalidAlgorithmParameterException: Not Implemented}），因此只能使用
 * {@code init(int keyBits)}；实测 BC 1.78.1 下该路径可用，生成结果的字节长度为
 * {@code ceil(keyBits / 8)}。若后续版本不再支持 {@code init(int)}，可回退为
 * {@link java.security.SecureRandom} 生成 {@code ceil(keyBits / 8)} 字节的
 * {@link SecretKeySpec}（当前不需要该回退）。</p>
 *
 * <p>密钥位数允许 8 位至 {@code min(getMaxAllowedKeyLength("RC5"), 2040)}：JDK 8 默认策略下
 * JCE 上限为 128 位；JDK 9+ 默认无限策略返回 {@link Integer#MAX_VALUE}，因此再按 RC5 规范的
 * 255 字节（2040 位）上限收敛。RC5 的轮数、分组长度与
 * 字长不是密钥属性，而由 {@link Rc5Helper#ROUNDS}、{@link Rc5Helper#BLOCK_SIZE}、
 * {@link Rc5Helper#WORD_SIZE} 固定，并在 {@link org.tools.encrypt.CipherSupply} 中传入。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class Rc5KeyGenerator {
    /**
     * 默认密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 允许的最小密钥位数
     */
    private static final int MIN_KEY_BITS = 8;

    /**
     * 允许的最大密钥位数：RC5 密钥最长 255 字节（RFC 2040）
     */
    private static final int MAX_KEY_BITS = 255 * 8;

    /**
     * 允许的最小密钥字节数
     */
    private static final int MIN_KEY_BYTES = 1;

    /**
     * 允许的最大密钥字节数（RFC 2040）
     */
    private static final int MAX_KEY_BYTES = 255;

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * Rc5 配置文件
     */
    private static Rc5Properties properties;

    private Rc5KeyGenerator() {
    }

    /**
     * 确保 BouncyCastle Provider 已注册（已注册时不做任何事）
     */
    private static void initBouncyCastle() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 获取默认位数（128 位）的随机密钥。
     *
     * @return SecretKey
     * @throws Rc5Exception 位数非法、算法不可用或密钥生成失败时抛出
     */
    public static SecretKey getSecretKey() throws Rc5Exception {
        return getSecretKey(KEY_BITS);
    }

    /**
     * 获取指定位数的随机密钥。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全）。
     * 仅使用 {@code init(int)}，原因见类注释：BC 不支持
     * {@code init(RC5ParameterSpec)}。</p>
     *
     * @param keyBits 密钥位数，必须在 8 位至 JCE 允许的最大值之间
     * @return SecretKey（算法名为 RC5）
     * @throws Rc5Exception 位数越界、算法不可用或密钥生成失败时抛出
     */
    public static SecretKey getSecretKey(int keyBits) throws Rc5Exception {
        if (keyBits < MIN_KEY_BITS) {
            throw new Rc5Exception("keyBits 不能小于 " + MIN_KEY_BITS + "！");
        }
        try {
            initBouncyCastle();
            // JDK 9+ 默认无限策略下 getMaxAllowedKeyLength 返回 Integer.MAX_VALUE，
            // 需再按 RFC 2040 的 255 字节上限收敛，避免 ceil(keyBits/8) 溢出
            int maxAllowedKeyLength = Math.min(
                    Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC5.getAlgorithm()), MAX_KEY_BITS);
            if (keyBits > maxAllowedKeyLength) {
                throw new Rc5Exception("keyBits 不能超过允许的最大值 " + maxAllowedKeyLength + "！");
            }
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AlgorithmEnum.RC5.getAlgorithm(),
                    BouncyCastleProvider.PROVIDER_NAME);
            // BC 的 RC5 KeyGenerator 未实现 init(AlgorithmParameterSpec)，只能按位数初始化
            keyGenerator.init(keyBits);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
    }

    /**
     * 获取密钥字符串（Hex）。
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
     * <p>密钥字符串为 <b>Hex</b>（与 {@link #getSecretKeyStr(SecretKey)} 输出对称，
     * 32 个 Hex 字符 = 16 字节 = 128 位）。</p>
     *
     * @param algorithm 算法
     * @param secretKey Hex 密钥字符串
     * @return 密钥对象
     * @throws Rc5Exception 密钥字符串为空、不是合法 Hex 或解码后长度不在 1..255 字节时抛出
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws Rc5Exception {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new Rc5Exception("secretKey 不能为空！");
        }
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
        // 解析阶段即拒绝越界长度，避免把非法密钥推迟到 Cipher.init 才暴露
        if (secretKeyBytes.length < MIN_KEY_BYTES || secretKeyBytes.length > MAX_KEY_BYTES) {
            throw new Rc5Exception("secretKey 解码后长度必须在 " + MIN_KEY_BYTES + ".." + MAX_KEY_BYTES
                    + " 字节之间！");
        }
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 懒加载 Rc5 配置文件（双检锁）
     */
    private static void initProperties() {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new Rc5Properties();
                }
            }
        }
    }

    /**
     * 获取 Rc5 配置文件（首次调用时生成默认密钥）
     *
     * @return Rc5Properties
     * @throws Rc5Exception 密钥生成失败时抛出
     */
    public static Rc5Properties getProperties() throws Rc5Exception {
        initProperties();

        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
