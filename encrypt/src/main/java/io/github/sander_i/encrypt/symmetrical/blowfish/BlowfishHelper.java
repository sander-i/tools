package io.github.sander_i.encrypt.symmetrical.blowfish;

import lombok.Getter;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Blowfish;
import io.github.sander_i.encrypt.entity.symmetrical.BlowfishProperties;
import io.github.sander_i.encrypt.exception.symmetrical.blowfish.BlowfishException;
import io.github.sander_i.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * Blowfish 加解密工具
 *
 * @author sander
 * @version 1.0
 */
public class BlowfishHelper {

    private BlowfishHelper() {
    }

    @Getter
    private static BlowfishProperties properties = new BlowfishProperties();

    /**
     * 用父级配置补齐分组配置缺失项
     */
    private static void init(Blowfish blowfish, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(blowfish.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(blowfish.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(blowfish.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getIvParameter())) {
            keyPair.setIvParameter(blowfish.getIvParameter());
        }
    }

    /**
     * 按分组名加载配置
     *
     * @param keyPair 配置
     * @param name    分组名
     * @throws BlowfishException Blowfish 加解密异常
     */
    public static void load(KeyPair keyPair, String name) throws BlowfishException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new BlowfishException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Blowfish)) {
            throw new BlowfishException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Blowfish ！");
        }
        Blowfish blowfish = (Blowfish) keyPair;
        if (!blowfish.getEnabled()) {
            throw new BlowfishException("Blowfish enable: false");
        }
        if (blowfish.getGroupEnabled()) {
            List<KeyPair> groupList = blowfish.getGroup();
            if (BaseUtils.isNotEmpty(groupList)) {
                for (KeyPair group : groupList) {
                    if (name.equals(group.getName())) {
                        init(blowfish, group);
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
     * @throws BlowfishException Blowfish 加解密异常
     */
    public static void load(KeyPair keyPair) throws BlowfishException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new BlowfishException("keyPair 不能为空！");
        }
        if (BaseUtils.isEmpty(keyPair.getSecretKey())) {
            throw new BlowfishException("secretKey 不能为空！");
        }
        BlowfishProperties properties = new BlowfishProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        properties.setSecretKey(BlowfishKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey()));
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    /**
     * 加载配置
     *
     * @param properties 配置
     * @throws BlowfishException Blowfish 加解密异常
     */
    public static void load(BlowfishProperties properties) throws BlowfishException {
        if (BaseUtils.isEmpty(properties)) {
            throw new BlowfishException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new BlowfishException("secretKey 不能为空！");
        }
        BlowfishHelper.properties = properties;
    }

    public static String encode(String raw) throws BlowfishException {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws BlowfishException {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws BlowfishException {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws BlowfishException {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), wrought);
    }

}
