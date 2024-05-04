package org.tools.encrypt.basic.base;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/28 21:44
 * @description 描述
 * @scope 作用域
 */
public class Base64Utils {

    private Base64Utils() {
    }

    protected static final Base64.Decoder decoder = Base64.getDecoder();
    protected static final Base64.Encoder encoder = Base64.getEncoder();

    public static String encode(byte[] raw) {
        return encoder.encodeToString(raw);
    }

    public static String encode(String raw) {
        return encode(raw.getBytes());
    }

    public static String decode(String wrought) {
        return new String(decoder.decode(wrought), StandardCharsets.UTF_8);
    }

}
