package io.github.sander_i.encrypt.symmetrical.rc.rc2;

import lombok.Getter;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.entity.symmetrical.Rc2;
import io.github.sander_i.encrypt.entity.symmetrical.Rc2Properties;
import io.github.sander_i.encrypt.exception.symmetrical.rc2.Rc2Exception;
import io.github.sander_i.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * RC2 加解密工具
 *
 * @author sander
 * @version 1.0
 */
public class Rc2Helper {

    private Rc2Helper() {
    }

    @Getter
    private static Rc2Properties properties = new Rc2Properties();

    /**
     * 用父级配置补齐分组配置缺失项
     */
    private static void init(Rc2 rc2, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(rc2.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(rc2.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(rc2.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getIvParameter())) {
            keyPair.setIvParameter(rc2.getIvParameter());
        }
    }

    /**
     * 按分组名加载配置
     *
     * @param keyPair 配置
     * @param name    分组名
     * @throws Rc2Exception RC2 加解密异常
     */
    public static void load(KeyPair keyPair, String name) throws Rc2Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc2Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rc2)) {
            throw new Rc2Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rc2 ！");
        }
        Rc2 rc2 = (Rc2) keyPair;
        if (!rc2.getEnabled()) {
            throw new Rc2Exception("Rc2 enable: false");
        }
        if (rc2.getGroupEnabled()) {
            List<KeyPair> groupList = rc2.getGroup();
            if (BaseUtils.isNotEmpty(groupList)) {
                for (KeyPair group : groupList) {
                    if (name.equals(group.getName())) {
                        init(rc2, group);
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
     * @throws Rc2Exception RC2 加解密异常
     */
    public static void load(KeyPair keyPair) throws Rc2Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc2Exception("keyPair 不能为空！");
        }
        if (BaseUtils.isEmpty(keyPair.getSecretKey())) {
            throw new Rc2Exception("secretKey 不能为空！");
        }
        Rc2Properties properties = new Rc2Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        properties.setSecretKey(Rc2KeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey()));
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
     * @throws Rc2Exception RC2 加解密异常
     */
    public static void load(Rc2Properties properties) throws Rc2Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Rc2Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Rc2Exception("secretKey 不能为空！");
        }
        Rc2Helper.properties = properties;
    }

    public static String encode(String raw) throws Rc2Exception {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws Rc2Exception {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws Rc2Exception {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                properties.getSecretKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws Rc2Exception {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(),
                secretKey, properties.getIvParameter(), wrought);
    }

}
