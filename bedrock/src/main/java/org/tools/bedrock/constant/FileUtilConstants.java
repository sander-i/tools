package org.tools.bedrock.constant;

/**
 * FileUtil相关常量定义
 *
 * @author sander
 * @version 1.0
 */
public class FileUtilConstants {

    private FileUtilConstants() {
    }

    /**
     * 资源文件未找到错误信息
     */
    public static final String RESOURCE_FILE_NOT_FOUND = "Resource file not found: ";

    /**
     * ZIP条目在目标目录外错误信息
     */
    public static final String ENTRY_OUTSIDE_TARGET_DIR = "Entry is outside of the target dir: ";

    /**
     * UTF-8编码
     */
    public static final String UTF_8_ENCODING = "UTF-8";

    /**
     * 缓冲区大小
     */
    public static final int BUFFER_SIZE = 1024;
}