package org.tools.encrypt.basic.crc32;

import java.util.zip.CRC32;

/**
 * CRC32 校验码工具（java.util.zip.CRC32，IEEE 802.3 标准）。
 *
 * <p>线程安全：每次调用使用独立的 {@link CRC32} 实例，可并发使用
 * （此前共享静态实例，非线程安全且多次调用结果会累积，已修复）。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.1
 */
public class Crc32Utils {
    private Crc32Utils() {
    }

    /**
     * 计算 CRC32 校验值。
     *
     * @param raw 数据字节；为 {@code null} 时抛 NPE
     * @return 无符号校验值（long 低位 32 位）
     */
    public static long encode(byte[] raw) {
        CRC32 crc32 = new CRC32();
        crc32.update(raw);
        return crc32.getValue();
    }
}
