package org.tools.encrypt.basic.crc32;

import java.util.zip.CRC32;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/28 21:47
 * @description crc32 校验工具类
 * @scope all
 */
public class Crc32Tool {

    private Crc32Tool() {
    }

    private final CRC32 crc32 = new CRC32();

    public long encode(byte[] raw) {
        crc32.update(raw);
        return crc32.getValue();
    }

}
