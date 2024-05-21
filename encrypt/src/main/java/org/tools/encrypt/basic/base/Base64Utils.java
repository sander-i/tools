package org.tools.encrypt.basic.base;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author sander
 * @version 1.0
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
