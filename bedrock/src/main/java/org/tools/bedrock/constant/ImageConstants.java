package org.tools.bedrock.constant;

/**
 * 图像处理相关常量定义类
 * 包含图像处理工具类中使用的所有常量定义
 *
 * @author sander
 * @version 1.0
 */
public class ImageConstants {

    private ImageConstants() {
    }

    /**
     * ImageUtil类相关常量
     */
    // 请求方式
    public static final String REQUEST_METHOD = "GET";

    // 错误消息常量
    public static final String IMAGE_URL_TO_BINARY_ERROR = "图片URL转字符数组";
    public static final String IMAGE_URL_TO_BINARY_NO_TIMEOUT_ERROR = "图片URL转字符数组(无超时限制)";
    public static final String READ_INPUT_STREAM_ERROR = "读取输入流返回字节数组";
    public static final String IMAGE_TO_BINARY_LOSS_ERROR = "图片文件转字节数组";
    public static final String BINARY_TO_IMAGE_LOSS_ERROR = "将二进制转换为图片(损失部分画质)";
    public static final String IMAGE_TO_BINARY_ERROR = "图片文件转字符数组";
    public static final String BINARY_TO_IMAGE_ERROR = "二进制转图片文件";
    public static final String INPUT_CLOSE_ERROR = "input.close";
    public static final String OUTPUT_CLOSE_ERROR = "output.close";
    public static final String FILE_NOT_FOUND_WARN = "图片文件不存在: {}";
    public static final String NOT_A_FILE_WARN = "指定路径不是文件: {}";
    public static final String CANNOT_READ_IMAGE_WARN = "无法读取图片文件，可能不是有效的图片格式: {}";
    public static final String WRITE_IMAGE_FAILED_WARN = "写入图片数据失败，可能不支持的格式: {}";
    public static final String CONVERT_SUCCESS_DEBUG = "成功将图片文件转换为字节数组，文件: {}, 大小: {} 字节";
    public static final String CREATE_DIR_DEBUG = "创建目录成功: {}";
    public static final String CREATE_DIR_FAILED_WARN = "创建目录失败: {}";
    public static final String CREATE_FILE_DEBUG = "创建文件成功: {}";
    public static final String FILE_EXISTS_DEBUG = "文件已存在，将覆盖: {}";
    public static final String CANNOT_READ_IMAGE_DATA_WARN = "无法读取图片数据，可能不是有效的图片格式";
    public static final String CONVERT_IMAGE_SUCCESS_DEBUG = "成功将字节数组转换为图片文件，路径: {}";
    public static final String WRITE_FILE_SUCCESS_DEBUG = "成功将字节数组写入图片文件，文件大小: {} 字节，路径: {}";

    // 魔法值常量
    public static final int CONNECT_TIMEOUT = 3000;
    public static final int GRID_SPACING = 10;
    public static final int CORNER_THRESHOLD = 30;
    public static final int MATCH_THRESHOLD = 1000;
    public static final int DESCRIPTOR_SIZE = 16;
    public static final int DEFAULT_IMAGE_SIZE = 100;
    public static final int RESIZE_WIDTH = 150;
    public static final int RESIZE_HEIGHT = 150;
    public static final int GRAY_IMAGE_TYPE = java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
    public static final int RGB_IMAGE_TYPE = java.awt.image.BufferedImage.TYPE_INT_RGB;

    /**
     * ImageStructureUtil类相关常量
     */
    // SSIM参数
    public static final double SSIM_K1 = 0.01;
    public static final double SSIM_K2 = 0.03;
    public static final int SSIM_L = 255;

    /**
     * ThumbnailUtil类相关常量
     */
    // 错误消息常量
    public static final String WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO = "宽度和高度必须大于0";
    public static final String IMAGE_PATH_CANNOT_BE_EMPTY = "图片路径不能为空";
    public static final String IMAGE_URL_CANNOT_BE_NULL = "图片URL不能为空";
    public static final String SCALE_MUST_BE_POSITIVE = "缩放比例必须在(0, 1]范围内";
    public static final String QUALITY_MUST_BE_IN_RANGE = "质量参数必须在[0, 1]范围内";
    public static final String ORIGINAL_IMAGE_CANNOT_BE_NULL = "原始图片不能为空";
    public static final String CANNOT_FIND_JPEG_WRITER = "无法找到JPEG图像写入器";
    public static final String FAILED_TO_READ_IMAGE_DATA = "无法读取图片数据";
    public static final String IMAGE_FILE_NOT_EXISTS = "图片文件不存在: ";
    public static final String CANNOT_READ_IMAGE_FILE = "无法读取图片文件，可能不是有效的图片格式: ";
    public static final String CANNOT_READ_IMAGE_FROM_URL = "无法从URL读取图片: ";
    public static final String FAILED_TO_CLOSE_IMAGE_OUTPUT_STREAM = "关闭ImageOutputStream失败";
    public static final String FAILED_TO_CLOSE_INPUT_STREAM = "关闭输入流失败";
    public static final String FAILED_TO_CLOSE_OUTPUT_STREAM = "关闭输出流失败";
    public static final String IMAGE_BYTES_CANNOT_BE_EMPTY = "图片字节数组不能为空";

    // 默认值常量
    public static final String DEFAULT_FORMAT = "jpg";
    public static final String JPEG_FORMAT = "jpg";
    public static final String PNG_FORMAT = "png";

    // 消息常量
    public static final String FAILED_TO_GENERATE_THUMBNAIL = "生成缩略图失败";
    public static final String CANNOT_DETERMINE_IMAGE_FORMAT_USING_DEFAULT_JPG = "无法判断图片格式，使用默认格式jpg";
}