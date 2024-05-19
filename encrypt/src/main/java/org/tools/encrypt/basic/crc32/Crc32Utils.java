package org.tools.encrypt.basic.crc32;

import java.util.zip.CRC32;

/**
 * @author sander
 * @version 1.0
 */
public class Crc32Utils {

    private Crc32Utils() {
    }

    private static final CRC32 crc32 = new CRC32();

    public static long encode(byte[] raw) {
        crc32.update(raw);
        return crc32.getValue();
    }

}
