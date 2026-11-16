package io.github.sander_i.encrypt.symmetrical.rc.rc4;

import lombok.Getter;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Rc4;
import io.github.sander_i.encrypt.entity.symmetrical.Rc4Properties;
import io.github.sander_i.encrypt.exception.symmetrical.rc4.Rc4Exception;
import io.github.sander_i.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * RC4 加解密工具（流加密，无向量）
 *
 * @author sander
 * @version 1.0
 */
public class Rc4Helper {

    private Rc4Helper() {
    }

    @Getter
    private static Rc4Properties properties = new Rc4Properties();

    /**
     * 用父级配置补齐分组配置缺失项
     */
    private static void init(Rc4 rc4, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(rc4.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(rc4.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(rc4.getAlgorithmMode());
        }
    }

    /**
     * 按分组名加载配置
     *
     * @param keyPair 配置
     * @param name    分组名
     * @throws Rc4Exception RC4 加解密异常
     */
    public static void load(KeyPair keyPair, String name) throws Rc4Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc4Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rc4)) {
            throw new Rc4Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rc4 ！");
        }
        Rc4 rc4 = (Rc4) keyPair;
        if (!rc4.getEnabled()) {
            throw new Rc4Exception("Rc4 enable: false");
        }
        if (rc4.getGroupEnabled()) {
            List<KeyPair> groupList = rc4.getGroup();
            if (BaseUtils.isNotEmpty(groupList)) {
                for (KeyPair group : groupList) {
                    if (name.equals(group.getName())) {
                        init(rc4, group);
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
     * @throws Rc4Exception RC4 加解密异常
     */
    public static void load(KeyPair keyPair) throws Rc4Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc4Exception("keyPair 不能为空！");
        }
        if (BaseUtils.isEmpty(keyPair.getSecretKey())) {
            throw new Rc4Exception("secretKey 不能为空！");
        }
        Rc4Properties properties = new Rc4Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        properties.setSecretKey(Rc4KeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey()));
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    /**
     * 加载配置
     *
     * @param properties 配置
     * @throws Rc4Exception RC4 加解密异常
     */
    public static void load(Rc4Properties properties) throws Rc4Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Rc4Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Rc4Exception("secretKey 不能为空！");
        }
        Rc4Helper.properties = properties;
    }

    public static String encode(String raw) throws Rc4Exception {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws Rc4Exception {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws Rc4Exception {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws Rc4Exception {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), wrought);
    }

}
