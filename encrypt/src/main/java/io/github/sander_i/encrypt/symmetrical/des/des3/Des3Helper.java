package io.github.sander_i.encrypt.symmetrical.des.des3;

import lombok.Getter;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Des3;
import io.github.sander_i.encrypt.entity.symmetrical.Des3Properties;
import io.github.sander_i.encrypt.exception.symmetrical.des3.Des3Exception;
import io.github.sander_i.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * 3DES 加解密工具
 *
 * @author sander
 * @version 1.0
 */
public class Des3Helper {

    private Des3Helper() {
    }

    @Getter
    private static Des3Properties properties = new Des3Properties();

    /**
     * 用父级配置补齐分组配置缺失项
     */
    private static void init(Des3 des3, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(des3.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(des3.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(des3.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getIvParameter())) {
            keyPair.setIvParameter(des3.getIvParameter());
        }
    }

    /**
     * 按分组名加载配置
     *
     * @param keyPair 配置
     * @param name    分组名
     * @throws Des3Exception 3DES 加解密异常
     */
    public static void load(KeyPair keyPair, String name) throws Des3Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Des3Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Des3)) {
            throw new Des3Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Des3 ！");
        }
        Des3 des3 = (Des3) keyPair;
        if (!des3.getEnabled()) {
            throw new Des3Exception("Des3 enable: false");
        }
        if (des3.getGroupEnabled()) {
            List<KeyPair> groupList = des3.getGroup();
            if (BaseUtils.isNotEmpty(groupList)) {
                for (KeyPair group : groupList) {
                    if (name.equals(group.getName())) {
                        init(des3, group);
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
     * @throws Des3Exception 3DES 加解密异常
     */
    public static void load(KeyPair keyPair) throws Des3Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Des3Exception("keyPair 不能为空！");
        }
        if (BaseUtils.isEmpty(keyPair.getSecretKey())) {
            throw new Des3Exception("secretKey 不能为空！");
        }
        Des3Properties properties = new Des3Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        properties.setSecretKey(Des3KeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey()));
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
     * @throws Des3Exception 3DES 加解密异常
     */
    public static void load(Des3Properties properties) throws Des3Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Des3Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Des3Exception("secretKey 不能为空！");
        }
        Des3Helper.properties = properties;
    }

    public static String encode(String raw) throws Des3Exception {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws Des3Exception {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws Des3Exception {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws Des3Exception {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), wrought);
    }

}
