package io.github.sander_i.encrypt.basic.crc32;

import java.util.zip.CRC32;

/**
 * CRC32 校验工具
 *
 * <p>
 * {@link CRC32} 实例是有状态的，{@code update} 会累积上一次的输入。
 * 因此每次计算都必须使用新实例，否则连续调用会得到错误结果。
 * 本类不持有静态实例，天然线程安全。
 * </p>
 *
 * @author sander
 * @version 1.0
 */
public class Crc32Utils {

    private Crc32Utils() {
    }

    /**
     * 计算 CRC32 值
     *
     * @param raw 待计算字节
     * @return CRC32 值
     */
    public static long encode(byte[] raw) {
        CRC32 crc32 = new CRC32();
        crc32.update(raw);
        return crc32.getValue();
    }

}
