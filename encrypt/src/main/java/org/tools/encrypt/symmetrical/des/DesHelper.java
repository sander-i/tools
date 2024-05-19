package org.tools.encrypt.symmetrical.des;

import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Des;
import org.tools.encrypt.exception.symmetrical.des.DesException;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/05
 */
public class DesHelper {

    private DesHelper() {
    }

    @Getter
    private static DesProperties properties;

    private static void init(Des des, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(des.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(des.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(des.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(des.getSignAlgorithm());
        }
    }

    public static void load(KeyPair keyPair, String name) throws DesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new DesException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Des)) {
            throw new DesException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Des ！");
        }
        Des des = (Des) keyPair;
        if (!des.getEnabled()) {
            throw new DesException("Des enable: false");
        }
        if (des.getGroupEnabled()) {
            List<KeyPair> groupList = des.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(des, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    public static void load(KeyPair keyPair) throws DesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new DesException("keyPair 不能为空！");
        }
        DesProperties properties = new DesProperties();
        properties.setSecretKey(keyPair.getSecretKey());
        SecretKey secretKey = DesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setDesKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    public static void load(DesProperties properties) throws DesException {
        if (BaseUtils.isEmpty(properties)) {
            throw new DesException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getDesKey())) {
            throw new DesException("secretKey 不能为空！");
        }
        DesHelper.properties = properties;
    }

    public static String encode(String raw) throws DesException {
        return DesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getDesKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws DesException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new DesException(e);
        }
        return DesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws DesException {
        return DesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws DesException {
        return DesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getDesKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws DesException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new DesException(e);
        }
        return DesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws DesException {
        return DesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), wrought);
    }

}
