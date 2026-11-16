package io.github.sander_i.example.encrypt.basic;

import io.github.sander_i.encrypt.basic.base.Base64Utils;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/27
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
