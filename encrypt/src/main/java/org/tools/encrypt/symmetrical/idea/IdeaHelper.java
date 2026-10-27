package org.tools.encrypt.symmetrical.idea;

import lombok.Getter;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Idea;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;

import javax.crypto.SecretKey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * IDEA 对称加解密工具。
 *
 * <p>算法模式为 {@code IDEA} 或 {@code IDEA/ECB/ISO10126Padding}，依赖 BouncyCastle Provider
 * （由 {@link org.tools.encrypt.CipherSupply} 自动注册）。{@code load(KeyPair,...)} 路径的密钥为
 * Hex 字符串（与 {@link IdeaKeyGenerator#getSecretKeyStr} 输出对称，32 个 Hex 字符 = 128 位）；
 * 明文按 UTF-8 编码，密文为 Base64。</p>
 *
 * <p>线程安全：{@code load} 与 {@code encode}/{@code decode} 并发不安全（静态全局配置）；
 * 多次 {@code encode}/{@code decode} 之间线程安全。</p>
 *
 * @author sander
 * @since 1.0.6.6
 */
public class IdeaHelper {
    private IdeaHelper() {
    }

    @Getter
    private static IdeaProperties properties = new IdeaProperties();

    private static void init(Idea idea, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(idea.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(idea.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(idea.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(idea.getSignAlgorithm());
        }
    }

    /**
     * 按分组名装载 IDEA 配置（实体需为 Idea，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws IdeaException            配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws IdeaException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new IdeaException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Idea)) {
            throw new IdeaException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Idea ！");
        }
        Idea idea = (Idea) keyPair;
        if (!idea.getEnabled()) {
            throw new IdeaException("Idea enable: false");
        }
        if (idea.getGroupEnabled()) {
            List<KeyPair> groupList = idea.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(idea, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    /**
     * 装载 KeyPair 实体配置（Hex 密钥，缺省字段取默认值）
     *
     * @param keyPair 密钥对配置
     * @throws IdeaException 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws IdeaException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new IdeaException("keyPair 不能为空！");
        }
        IdeaProperties properties = new IdeaProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        SecretKey secretKey = IdeaKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setSecretKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    /**
     * 装载 IDEA 配置（properties 与密钥不能为空）
     *
     * @param properties IDEA 配置
     * @throws IdeaException 配置或密钥为空时抛出
     */
    public static void load(IdeaProperties properties) throws IdeaException {
        if (BaseUtils.isEmpty(properties)) {
            throw new IdeaException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new IdeaException("secretKey 不能为空！");
        }
        IdeaHelper.properties = properties;
    }

    /**
     * 使用全局激活配置加密
     *
     * @param raw 明文
     * @return Base64 密文
     * @throws IdeaException 密钥/模式非法或 JCE 操作失败时抛出
     */
    public static String encode(String raw) throws IdeaException {
        return IdeaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(),
                raw);
    }

    /**
     * 使用指定 Hex 密钥字符串加密
     *
     * @param secretKey Hex 密钥字符串
     * @param raw       明文
     * @return Base64 密文
     * @throws IdeaException 密钥非法或加密失败时抛出
     */
    public static String encode(String secretKey, String raw) throws IdeaException {
        SecretKey key = IdeaKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return IdeaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, raw);
    }

    /**
     * 使用指定密钥对象加密
     *
     * @param secretKey IDEA SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws IdeaException 密钥非法或加密失败时抛出
     */
    public static String encode(SecretKey secretKey, String raw) throws IdeaException {
        return IdeaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, raw);
    }

    /**
     * 使用全局激活配置解密
     *
     * @param wrought Base64 密文
     * @return UTF-8 明文
     * @throws IdeaException 密文/密钥非法或解密失败时抛出
     */
    public static String decode(String wrought) throws IdeaException {
        return IdeaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(),
                wrought);
    }

    /**
     * 使用指定 Hex 密钥字符串解密
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws IdeaException 密钥非法或解密失败时抛出
     */
    public static String decode(String secretKey, String wrought) throws IdeaException {
        SecretKey key = IdeaKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return IdeaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, wrought);
    }

    /**
     * 使用指定密钥对象解密
     *
     * @param secretKey IDEA SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws IdeaException 密钥非法或解密失败时抛出
     */
    public static String decode(SecretKey secretKey, String wrought) throws IdeaException {
        return IdeaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, wrought);
    }
}
