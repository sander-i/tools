package org.tools.encrypt.basic;

import org.junit.Test;
import org.tools.encrypt.basic.sha.ShaUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/14 17:57
 * @description 描述
 * @scope 作用域
 */
public class ShaTest {

    @Test
    public void test() throws NoSuchAlgorithmException {


        String encode = ShaUtils.encode("SHA-512", "666");
        System.out.println(encode);

    }

    public static void main(String[] args) throws Exception {
        // 假设我们已经有了一个密钥对象
        String key = "1234567887654321";
        Key key1 = new SecretKeySpec(key.getBytes(), "AES");

        // 创建 Cipher 对象并初始化为 WRAP_MODE
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.WRAP_MODE, key1); // 这里需要一个有关密钥的SecretKey或者Key

        // 执行封装操作
        byte[] wrappedKey = cipher.wrap(key1);

        // 输出或处理封装后的密钥
        System.out.println("Wrapped Key: " + bytesToHex(wrappedKey));
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

}
