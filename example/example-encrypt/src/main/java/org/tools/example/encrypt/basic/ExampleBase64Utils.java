package org.tools.example.encrypt.basic;

import org.tools.encrypt.basic.base.Base64Utils;

/**
 * Base64Utils 使用示例：字符串的 Base64 编码与解码
 *
 * @author sander 2024/04/27
 * @since 1.0.6.1
 */
public class ExampleBase64Utils {
    public static void main(String[] args) {
        String raw = "4s5679099kj7hy";

        String encode = Base64Utils.encode(raw);
        System.out.println(encode);
        String decode = Base64Utils.decode(encode);
        System.out.println(decode);
    }
}
