package org.tools.encrypt.asymmetric.dh;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.asymmetric.Dh;
import org.tools.encrypt.entity.asymmetric.DhProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.asymmetric.dh.DhException;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;

/**
 * DH（Diffie-Hellman）密钥协商工具。
 *
 * <p><b>DH 不是加密算法</b>：本类不提供 {@code encode}/{@code decode}，也不接入
 * {@code AsymmetricUtils} / {@code CipherAsymmetric}。它只负责两件事：
 * 生成密钥对、用一方的私钥与另一方的公钥协商出共享秘密。</p>
 *
 * <p>典型流程：</p>
 * <ol>
 *     <li>甲生成密钥对，把自己的公钥（Base64 X.509）发给乙；</li>
 *     <li>乙用公钥中的域参数（{@link #getParams(PublicKey)}）生成自己的密钥对，回传公钥；</li>
 *     <li>双方各自用「自己的私钥 + 对方的公钥」协商，得到相同的共享秘密。</li>
 * </ol>
 *
 * <p><b>安全提示</b>：{@link KeyAgreement#generateSecret()} 返回的原始共享秘密在数学上
 * 并不均匀（存在偏差），<b>不能直接当作对称密钥使用</b>。必须先经 HKDF、SHA-256 等
 * 密钥派生函数（KDF）派生出固定长度的对称密钥，再交给 AES 等算法。本类只返回原始结果，
 * 不做派生，也不返回 {@code SecretKey}。</p>
 *
 * <p>线程安全：{@code generateSharedSecret} 每次创建独立 {@link KeyAgreement} 实例，
 * 可并发调用；静态 {@code properties} 由 {@code load} 替换，{@code load} 与读取并发不安全
 * （与其它 Helper 一致）。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class DhHelper {
    /**
     * 全局激活的 DH 配置
     */
    @Getter
    private static DhProperties properties = new DhProperties();

    private DhHelper() {
    }

    private static void init(Dh dh, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(dh.getAlgorithm());
        }
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(dh.getKeyBits());
        }
    }

    /**
     * 按分组名装载 DH 配置（实体需为 {@link Dh}，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws DhException 配置为空、实体类型不符、未启用或密钥解析失败时抛出
     */
    public static void load(KeyPair keyPair, String name) throws DhException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new DhException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Dh)) {
            throw new DhException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Dh ！");
        }
        Dh dh = (Dh) keyPair;
        if (!Boolean.TRUE.equals(dh.getEnabled())) {
            throw new DhException("DH enable: false");
        }
        if (Boolean.TRUE.equals(dh.getGroupEnabled()) && BaseUtils.isNotEmpty(dh.getGroup())) {
            List<KeyPair> groupList = dh.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(dh, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    /**
     * 装载 KeyPair / Dh 实体配置（按 Base64 字符串解析公私钥对象与域参数）
     *
     * @param keyPair 密钥对配置
     * @throws DhException 配置为空、公私钥缺失或密钥解析失败时抛出
     */
    public static void load(KeyPair keyPair) throws DhException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new DhException("keyPair 不能为空！");
        }
        // 解析前先校验字符串，避免只配置了 keyBits 时 Base64 解码 null 引发 NPE
        if (BaseUtils.isEmpty(keyPair.getPublicKey()) || BaseUtils.isEmpty(keyPair.getPrivateKey())) {
            throw new DhException("publicKey|privateKey 不能为空！");
        }
        DhProperties properties = new DhProperties();
        properties.setPublicKeyStr(keyPair.getPublicKey());
        properties.setPrivateKeyStr(keyPair.getPrivateKey());
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithm())) {
            properties.setAlgorithm(keyPair.getAlgorithm());
        }
        if (keyPair.getKeyBits() > 0) {
            properties.setKeyBits(keyPair.getKeyBits());
        }
        try {
            properties.setPublicKey(DhKeyGenerator.getPublicKey(AlgorithmEnum.DH.getAlgorithm(), keyPair
                    .getPublicKey()));
            properties.setPrivateKey(DhKeyGenerator.getPrivateKey(AlgorithmEnum.DH.getAlgorithm(), keyPair
                    .getPrivateKey()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
        properties.setParams(DhKeyGenerator.getParams(properties.getPublicKey()));
        // 未显式指定密钥位数时，以公钥携带的域参数为准，避免配置与实际密钥不一致
        if (keyPair.getKeyBits() <= 0) {
            properties.setKeyBits(properties.getParams().getP().bitLength());
        }
        load(properties);
    }

    /**
     * 装载 DH 配置（DH 无加解密入口，此处仅校验并激活配置）
     *
     * <p><b>注意</b>：本方法只接受已解析的 {@code PublicKey/PrivateKey} 对象；
     * 仅含 Base64 字符串（{@code publicKeyStr}/{@code privateKeyStr}）的配置请改用
     * {@link #load(KeyPair)}，由它完成字符串解析。</p>
     *
     * @param properties DH 配置
     * @throws DhException 配置为空、公私钥缺失或域参数缺失时抛出
     */
    public static void load(DhProperties properties) throws DhException {
        if (BaseUtils.isEmpty(properties)) {
            throw new DhException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getPublicKey()) || BaseUtils.isEmpty(properties.getPrivateKey())) {
            throw new DhException("publicKey|privateKey 不能为空！");
        }
        if (BaseUtils.isNull(properties.getParams())) {
            try {
                properties.setParams(DhKeyGenerator.getParams(properties.getPublicKey()));
            } catch (IllegalArgumentException e) {
                log.error(e.getLocalizedMessage());
                throw new DhException(e);
            }
        }
        DhHelper.properties = properties;
    }

    /**
     * 生成默认 2048 位 DH 密钥对
     *
     * @return 密钥对
     * @throws DhException 算法不可用或密钥生成失败时抛出
     */
    public static java.security.KeyPair generateKeyPair() throws DhException {
        return DhKeyGenerator.generateKeyPair();
    }

    /**
     * 生成指定密钥位数的 DH 密钥对
     *
     * @param keyBits 密钥位数，不能小于 512，且须为 64 的整数倍
     * @return 密钥对
     * @throws DhException 位数非法、算法不可用或密钥生成失败时抛出
     */
    public static java.security.KeyPair generateKeyPair(int keyBits) throws DhException {
        return DhKeyGenerator.generateKeyPair(keyBits);
    }

    /**
     * 使用已有域参数生成 DH 密钥对（协商双方需使用同一组参数）
     *
     * @param params DH 域参数
     * @return 密钥对
     * @throws DhException 参数为空、算法不可用或密钥生成失败时抛出
     */
    public static java.security.KeyPair generateKeyPair(DHParameterSpec params) throws DhException {
        return DhKeyGenerator.generateKeyPair(params);
    }

    /**
     * 协商共享秘密（原始字节）
     *
     * <p><b>注意</b>：返回值是 {@link KeyAgreement#generateSecret()} 的原始输出，
     * 分布不均匀，必须经 HKDF/SHA-256 派生后再作为对称密钥使用。双方使用同一组域参数时，
     * 各自「私钥 + 对方公钥」的结果相同；域参数不一致时会抛出 {@link DhException}。</p>
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @return 原始共享秘密字节
     * @throws DhException 密钥为空、参数不兼容或协商失败时抛出
     */
    public static byte[] generateSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws DhException {
        if (BaseUtils.isNull(privateKey, publicKey)) {
            throw new DhException("privateKey|publicKey 不能为空！");
        }
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance(AlgorithmEnum.DH.getAlgorithm());
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            return keyAgreement.generateSecret();
        } catch (GeneralSecurityException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 协商共享秘密并返回 Base64 字符串（原始结果，需再经 KDF 派生）
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @return Base64 编码的原始共享秘密
     * @throws DhException 密钥为空、参数不兼容或协商失败时抛出
     */
    public static String generateSharedSecretStr(PrivateKey privateKey, PublicKey publicKey) throws DhException {
        return Base64.encodeBase64String(generateSharedSecret(privateKey, publicKey));
    }

    /**
     * 协商共享秘密并返回 Base64 字符串（入参为 Base64 密钥字符串）
     *
     * @param privateKeyStr 本方 Base64(PKCS#8) 私钥字符串
     * @param publicKeyStr  对方 Base64(X.509) 公钥字符串
     * @return Base64 编码的原始共享秘密
     * @throws DhException 密钥为空、解析失败、参数不兼容或协商失败时抛出
     */
    public static String generateSharedSecretStr(String privateKeyStr, String publicKeyStr) throws DhException {
        if (BaseUtils.isEmpty(privateKeyStr) || BaseUtils.isEmpty(publicKeyStr)) {
            throw new DhException("privateKeyStr|publicKeyStr 不能为空！");
        }
        try {
            PrivateKey privateKey = DhKeyGenerator.getPrivateKey(AlgorithmEnum.DH.getAlgorithm(), privateKeyStr);
            PublicKey publicKey = DhKeyGenerator.getPublicKey(AlgorithmEnum.DH.getAlgorithm(), publicKeyStr);
            return generateSharedSecretStr(privateKey, publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 提取 DH 公钥携带的域参数
     *
     * @param publicKey DH 公钥
     * @return 域参数
     * @throws DhException 公钥为空或不是 DH 公钥时抛出
     */
    public static DHParameterSpec getParams(PublicKey publicKey) throws DhException {
        if (BaseUtils.isNull(publicKey)) {
            throw new DhException("publicKey 不能为空！");
        }
        try {
            return DhKeyGenerator.getParams(publicKey);
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }
}
