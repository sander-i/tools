package org.tools.encrypt.symmetrical.des;

import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/30
 * @description 描述
 * @scope 作用域
 */
@Slf4j
public class DesUtils extends SymmetricalUtils {

    private DesUtils() {
    }

    /**
     * DES算法要求有一个可信任的随机数源
     *
     * @return SecureRandom
     */
    protected static SecureRandom getSecureRandom() {
        return new SecureRandom();
    }

    /**
     * 加密前处理
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected static byte[] encryptBefore(String todo) {
        return todo.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 加密后处理
     *
     * @param todo 待处理
     * @return String
     */
    protected static String encryptAfter(byte[] todo) {
        return Base64.getEncoder().encodeToString(todo);
    }

    /**
     * 解密前处理
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected static byte[] decryptBefore(String todo) {
        return Base64.getDecoder().decode(todo);
    }

    /**
     * 解密后处理
     *
     * @param todo 待处理
     * @return String
     */
    protected static String decryptAfter(byte[] todo) {
        return new String(todo);
    }

}
