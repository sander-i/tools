package org.tools.encrypt.symmetrical.aes;

import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Aes;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

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
public class AesHelper {

    private AesHelper() {
    }

    @Getter
    private static AesProperties properties;

    private static void init(Aes aes, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(aes.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(aes.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(aes.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(aes.getSignAlgorithm());
        }
    }

    public static void load(KeyPair keyPair, String name) throws AesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new AesException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Aes)) {
            throw new AesException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Aes ！");
        }
        Aes aes = (Aes) keyPair;
        if (!aes.getEnabled()) {
            throw new AesException("Aes enable: false");
        }
        if (aes.getGroupEnabled()) {
            List<KeyPair> groupList = aes.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(aes, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    public static void load(KeyPair keyPair) throws AesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new AesException("keyPair 不能为空！");
        }
        AesProperties properties = new AesProperties();
        properties.setSecretKey(keyPair.getSecretKey());
        SecretKey secretKey = AesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setAesKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    public static void load(AesProperties properties) throws AesException {
        if (BaseUtils.isEmpty(properties)) {
            throw new AesException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getAesKey())) {
            throw new AesException("secretKey 不能为空！");
        }
        AesHelper.properties = properties;
    }

    public static String encode(String raw) throws AesException {
        return AesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getAesKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws AesException {
        SecretKey key;
        try {
            key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AesException(e);
        }
        return AesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws AesException {
        return AesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws AesException {
        return AesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getAesKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws AesException {
        SecretKey key;
        try {
            key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AesException(e);
        }
        return AesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws AesException {
        return AesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), wrought);
    }

}
