package io.github.sander_i.encrypt.digitalsignature.ecdsa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.digitalsignature.SignatureUtils;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.digitalsignature.EcdsaProperties;
import io.github.sander_i.encrypt.exception.digitalsignature.ecdsa.EcdsaException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

/**
 * ECDSA 数字签名工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class EcdsaHelper {

    private EcdsaHelper() {
    }

    @Getter
    private static EcdsaProperties properties = new EcdsaProperties();

    /**
     * 按分组名加载配置
     *
     * @param keyPair 配置
     * @param name    分组名
     * @throws EcdsaException ECDSA 异常
     */
    public static void load(KeyPair keyPair, String name) throws EcdsaException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EcdsaException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof io.github.sander_i.encrypt.entity.digitalsignature.Ecdsa)) {
            throw new EcdsaException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Ecdsa ！");
        }
        io.github.sander_i.encrypt.entity.digitalsignature.Ecdsa ecdsa =
                (io.github.sander_i.encrypt.entity.digitalsignature.Ecdsa) keyPair;
        if (!ecdsa.getEnabled()) {
            throw new EcdsaException("Ecdsa enable: false");
        }
        if (ecdsa.getGroupEnabled()) {
            List<KeyPair> groupList = ecdsa.getGroup();
            if (BaseUtils.isNotEmpty(groupList)) {
                for (KeyPair group : groupList) {
                    if (name.equals(group.getName())) {
                        if (BaseUtils.isEmpty(group.getSignAlgorithm())) {
                            group.setSignAlgorithm(ecdsa.getSignAlgorithm());
                        }
                        load(group);
                        return;
                    }
                }
            }
        }
        load(keyPair);
    }

    /**
     * 加载配置
     *
     * @param keyPair 配置
     * @throws EcdsaException ECDSA 异常
     */
    public static void load(KeyPair keyPair) throws EcdsaException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EcdsaException("keyPair 不能为空！");
        }
        if (!BaseUtils.isNotEmpty(keyPair.getPublicKey(), keyPair.getPrivateKey())) {
            throw new EcdsaException("publicKey|privateKey 不能为空！");
        }
        EcdsaProperties properties = new EcdsaProperties();
        properties.setPublicKeyStr(keyPair.getPublicKey());
        properties.setPrivateKeyStr(keyPair.getPrivateKey());
        properties.setSignAlgorithm(keyPair.getSignAlgorithm());
        properties.setPublicKey(EcdsaKeyGenerator.getPublicKey(keyPair.getPublicKey()));
        properties.setPrivateKey(EcdsaKeyGenerator.getPrivateKey(keyPair.getPrivateKey()));
        load(properties);
    }

    /**
     * 加载配置
     *
     * @param properties 配置
     * @throws EcdsaException ECDSA 异常
     */
    public static void load(EcdsaProperties properties) throws EcdsaException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EcdsaException("properties 不能为空！");
        }
        if (!BaseUtils.isNotEmpty(properties.getPublicKey(), properties.getPrivateKey())) {
            throw new EcdsaException("publicKey|privateKey 不能为空！");
        }
        EcdsaHelper.properties = properties;
    }

    /**
     * 使用已加载配置签名
     *
     * @param wrought 明文
     * @return Base64 签名
     * @throws EcdsaException ECDSA 异常
     */
    public static String sign(String wrought) throws EcdsaException {
        try {
            return SignatureUtils.sign(properties.getSignAlgorithm(), properties.getPrivateKey(), wrought);
        } catch (Exception e) {
            throw new EcdsaException(e);
        }
    }

    /**
     * 使用已加载配置验签
     *
     * @param wrought 明文
     * @param sign    Base64 签名
     * @return 是否通过
     * @throws EcdsaException ECDSA 异常
     */
    public static boolean verify(String wrought, String sign) throws EcdsaException {
        try {
            return SignatureUtils.verify(properties.getSignAlgorithm(), properties.getPublicKey(), wrought, sign);
        } catch (Exception e) {
            throw new EcdsaException(e);
        }
    }

    /**
     * 用指定私钥签名
     *
     * @param privateKey 私钥（Base64）
     * @param wrought    明文
     * @return Base64 签名
     * @throws EcdsaException ECDSA 异常
     */
    public static String sign(String privateKey, String wrought) throws EcdsaException {
        return sign(EcdsaKeyGenerator.getPrivateKey(privateKey), wrought);
    }

    /**
     * 用指定公钥验签
     *
     * @param publicKey 公钥（Base64）
     * @param wrought   明文
     * @param sign      Base64 签名
     * @return 是否通过
     * @throws EcdsaException ECDSA 异常
     */
    public static boolean verify(String publicKey, String wrought, String sign) throws EcdsaException {
        return verify(EcdsaKeyGenerator.getPublicKey(publicKey), wrought, sign);
    }

    /**
     * 用私钥对象签名
     *
     * @param privateKey 私钥对象
     * @param wrought    明文
     * @return Base64 签名
     * @throws EcdsaException ECDSA 异常
     */
    public static String sign(PrivateKey privateKey, String wrought) throws EcdsaException {
        try {
            return SignatureUtils.sign(properties.getSignAlgorithm(), privateKey, wrought);
        } catch (Exception e) {
            throw new EcdsaException(e);
        }
    }

    /**
     * 用公钥对象验签
     *
     * @param publicKey 公钥对象
     * @param wrought   明文
     * @param sign      Base64 签名
     * @return 是否通过
     * @throws EcdsaException ECDSA 异常
     */
    public static boolean verify(PublicKey publicKey, String wrought, String sign) throws EcdsaException {
        try {
            return SignatureUtils.verify(properties.getSignAlgorithm(), publicKey, wrought, sign);
        } catch (Exception e) {
            throw new EcdsaException(e);
        }
    }

}
