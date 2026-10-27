package org.tools.encrypt.basic.base;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 标准 Base64 编解码工具（RFC 4648，非 URL-Safe），固定使用 UTF-8 字符集。
 *
 * <p>线程安全：无共享可变状态，可并发使用。</p>
 *
 * @author sander
 * @since 1.0.6.1
 */
public class Base64Utils {
    private Base64Utils() {
    }

    protected static final Base64.Decoder DECODER = Base64.getDecoder();

    protected static final Base64.Encoder ENCODER = Base64.getEncoder();

    /**
     * Base64 编码。
     *
     * @param raw 原始字节；为 {@code null} 时抛 NPE
     * @return Base64 字符串
     */
    public static String encode(byte[] raw) {
        return ENCODER.encodeToString(raw);
    }

    /**
     * Base64 编码，入参固定按 UTF-8 取字节
     * （此前使用 JVM 默认 charset，与 {@link #decode(String)} 的 UTF-8 不对称，已修复）。
     *
     * @param raw 原文字符串；为 {@code null} 时抛 NPE
     * @return Base64 字符串
     */
    public static String encode(String raw) {
        return encode(raw.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 解码。
     *
     * @param wrought Base64 字符串；含非法字符时抛 {@link IllegalArgumentException}；为 {@code null} 时抛 NPE
     * @return 按 UTF-8 还原的原文字符串
     */
    public static String decode(String wrought) {
        return new String(DECODER.decode(wrought), StandardCharsets.UTF_8);
    }
}
