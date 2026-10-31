package org.tools.fortify.util.file;

import lombok.extern.slf4j.Slf4j;

import net.coobird.thumbnailator.Thumbnails;

import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.constant.v2.SpecialSymbolConst;
import org.tools.bedrock.util.BaseUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片工具
 *
 * @author sander
 * @version 1.0
 * @since 1.0.3
 */
@Slf4j
public class ImageUtils {
    private ImageUtils() {
    }

    /**
     * request 请求方式
     */
    private static final String REQUEST_METHOD = "GET";

    /**
     * 3000/ms
     */
    private static final int CONNECT_TIMEOUT = 3000;

    /**
     * 图片URL转字符数组
     *
     * @param urlPath url路径
     * @return 图片字节数组
     */
    public static byte[] imageUrLToBinary(String urlPath) {
        if (BaseUtils.isEmpty(urlPath)) {
            return Constants.EMPTY_BYTE_ARRAY;
        }
        URL url;
        InputStream input = null;
        HttpURLConnection httpConn = null;
        String errorMsg = "image URL to byte array";
        try {
            url = new URL(urlPath);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod(REQUEST_METHOD);
            httpConn.setConnectTimeout(CONNECT_TIMEOUT);
            input = httpConn.getInputStream();
            return readInputStream(input);
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, httpConn, input);
        }
        return Constants.EMPTY_BYTE_ARRAY;
    }

    /**
     * 图片URL转字符数组(无超时限制)
     *
     * @param httpUrl 图片URL
     * @return 图片字节数组
     */
    public static byte[] imageUrLToBinary2(String httpUrl) {
        URL url;
        InputStream input = null;
        HttpURLConnection httpConn = null;
        String errorMsg = "image URL to byte array (no timeout limit)";
        try {
            url = new URL(httpUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.connect();
            input = httpConn.getInputStream();
            return readInputStream(input);
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, httpConn, input);
        }
        return Constants.EMPTY_BYTE_ARRAY;
    }

    /**
     * 读取输入流返回字节数组
     *
     * @param stream 流
     * @return 字节数组
     */
    private static <T> byte[] readInputStream(T stream) {
        InputStream inputStream = stream instanceof InputStream ? (InputStream) stream : null;
        FileImageInputStream fileInputStream = stream instanceof FileImageInputStream ? (
                FileImageInputStream) stream : null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String errorMsg = "read input stream to byte array";
        try {
            byte[] buffer = Constants.MAXIMUM_BYTE_ARRAY;
            int len;
            if (inputStream != null) {
                while ((len = inputStream.read(buffer)) != Constants.NEGATIVE_ONE_I) {
                    output.write(buffer, Constants.ZERO_I, len);
                }
            }
            if (fileInputStream != null) {
                while ((len = fileInputStream.read(buffer)) != Constants.NEGATIVE_ONE_I) {
                    output.write(buffer, Constants.ZERO_I, len);
                }
            }
            return output.toByteArray();
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, output);
        }
        return Constants.EMPTY_BYTE_ARRAY;
    }

    /**
     * 图片文件转字节数组
     * 损失部分画质(jpg)
     * 图片文件变大(png)
     * 动态图片无法还原(gif)
     *
     * @param filePath 文件路径
     * @return 图片字节数组
     */
    public static byte[] imageToBinaryLoss(String filePath) {
        // 参数验证
        if (BaseUtils.isEmpty(filePath)) {
            log.warn("file path is empty or null, cannot read image file");
            return Constants.EMPTY_BYTE_ARRAY;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String errorMsg = "image file to byte array (lossy)";

        try {
            File f = new File(filePath);

            // 检查文件是否存在
            if (!f.exists()) {
                log.warn("image file not found: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 检查是否为文件（而不是目录）
            if (!f.isFile()) {
                log.warn("specified path is not a file: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 读取图片文件
            BufferedImage buf = ImageIO.read(f);
            if (buf == null) {
                log.warn("cannot read image file, may not be a valid image format: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 获取文件扩展名
            String format = formatName(filePath);
            // 如果没有扩展名或扩展名无效，使用默认格式
            if (BaseUtils.isEmpty(format)) {
                format = "jpg"; // 默认使用jpg格式
                log.debug("file has no extension, using default format: {}", format);
            }

            // 写入图片数据
            boolean success = ImageIO.write(buf, format, buffer);
            if (!success) {
                log.warn("failed to write image data, possibly unsupported format: {}", format);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            log.debug("successfully converted image file to byte array, file: {}, size: {} bytes", filePath, buffer.size());
            return buffer.toByteArray();
        } catch (IOException e) {
            log.error("failed to read or convert image file, path: {}", filePath, e);
        } catch (Exception e) {
            log.error("unknown error while processing image file, path: {}", filePath, e);
        } finally {
            closeStream(errorMsg, buffer);
        }

        return Constants.EMPTY_BYTE_ARRAY;
    }

    /**
     * 将二进制转换为图片
     * 损失部分画质(jpg)
     * 图片文件变大(png)
     * 动态图片无法还原(gif)
     *
     * @param bytes       图片字节数组
     * @param outfilePath 输出文件路径
     */
    public static void binaryToImageLoss(byte[] bytes, String outfilePath) {
        // 参数验证
        if (bytes == null) {
            log.warn("image byte array is null, cannot write file");
            return;
        }

        if (bytes.length == 0) {
            log.warn("image byte array is empty, cannot write file");
            return;
        }

        if (BaseUtils.isEmpty(outfilePath)) {
            log.warn("output file path is empty or null, cannot write file");
            return;
        }

        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        String errorMsg = "binary to image (lossy)";

        try {
            File f = new File(outfilePath);

            // 确保父目录存在
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.debug("created directory: {}", parentDir.getAbsolutePath());
                } else {
                    log.warn("failed to create directory: {}", parentDir.getAbsolutePath());
                }
            }

            // 创建文件（如果不存在）
            if (!f.exists()) {
                if (f.createNewFile()) {
                    log.debug("created file: {}", f.getAbsolutePath());
                } else {
                    log.warn("failed to create file: {}", f.getAbsolutePath());
                }
            } else {
                log.debug("file already exists, will overwrite: {}", f.getAbsolutePath());
            }

            BufferedImage buffer = ImageIO.read(input);
            if (buffer == null) {
                log.warn("cannot read image data, may not be a valid image format");
                return;
            }

            ImageIO.write(buffer, formatName(outfilePath), f);
            log.debug("successfully converted byte array to image file, path: {}", outfilePath);
        } catch (IOException e) {
            log.error("failed to convert image file, path: {}", outfilePath, e);
        } catch (Exception e) {
            log.error("unknown error while processing image file, path: {}", outfilePath, e);
        } finally {
            closeStream(errorMsg, input);
        }
    }

    /**
     * 通过文件路径截取文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型，如果无扩展名则返回空字符串
     */
    public static String formatName(String filePath) {
        // 参数验证
        if (BaseUtils.isEmpty(filePath)) {
            return "";
        }

        // 查找最后一个点号的位置
        int lastDotIndex = filePath.lastIndexOf(SpecialSymbolConst.POINT);

        // 如果没有找到点号，或者点号在路径的末尾
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
            return "";
        }

        // 如果点号在路径的开头（如 .config 文件），返回点号后的所有内容
        if (lastDotIndex == 0) {
            return filePath.substring(1);
        }

        // 返回点号后的扩展名
        return filePath.substring(lastDotIndex + Constants.ONE_I);
    }

    /**
     * 图片文件转字符数组
     *
     * @param filePath 图片路径
     * @return 图片字符数组
     */
    public static byte[] imageToBinary(String filePath) {
        // 参数验证
        if (BaseUtils.isEmpty(filePath)) {
            log.warn("file path is empty or null, cannot read image file");
            return Constants.EMPTY_BYTE_ARRAY;
        }

        File file = new File(filePath);
        FileImageInputStream input = null;
        String errorMsg = "image file to byte array (raw)";

        try {
            // 检查文件是否存在
            if (!file.exists()) {
                log.warn("image file not found: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 检查是否为文件（而不是目录）
            if (!file.isFile()) {
                log.warn("specified path is not a file: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            input = new FileImageInputStream(file);
            return readInputStream(input);
        } catch (IOException e) {
            log.error("failed to read image file, path: {}", filePath, e);
        } catch (Exception e) {
            log.error("unknown error while processing image file, path: {}", filePath, e);
        } finally {
            closeStream(errorMsg, input);
        }

        return Constants.EMPTY_BYTE_ARRAY;
    }

    /**
     * 二进制转图片文件
     *
     * @param bytes       图片字节数组
     * @param outfilePath 输出文件路径
     */
    public static void binaryToImage(byte[] bytes, String outfilePath) {
        // 参数验证
        if (bytes == null) {
            log.warn("image byte array is null, cannot write file");
            return;
        }

        if (bytes.length == 0) {
            log.warn("image byte array is empty, cannot write file");
            return;
        }

        if (BaseUtils.isEmpty(outfilePath)) {
            log.warn("output file path is empty or null, cannot write file");
            return;
        }

        File f = new File(outfilePath);
        FileImageOutputStream output = null;
        String errorMsg = "binary to image file";

        try {
            // 确保父目录存在
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.debug("created directory: {}", parentDir.getAbsolutePath());
                } else {
                    log.warn("failed to create directory: {}", parentDir.getAbsolutePath());
                }
            }

            // 创建文件（如果不存在）
            if (!f.exists()) {
                if (f.createNewFile()) {
                    log.debug("created file: {}", f.getAbsolutePath());
                } else {
                    log.warn("failed to create file: {}", f.getAbsolutePath());
                }
            } else {
                log.debug("file already exists, will overwrite: {}", f.getAbsolutePath());
            }

            output = new FileImageOutputStream(f);
            // 将字节数组写入硬盘
            output.write(bytes, Constants.ZERO_I, bytes.length);
            output.flush();
            log.debug("successfully wrote byte array to image file, size: {} bytes, path: {}", bytes.length, outfilePath);
        } catch (IOException e) {
            log.error("failed to write image file, path: {}", outfilePath, e);
        } catch (Exception e) {
            log.error("unknown error while processing image file, path: {}", outfilePath, e);
        } finally {
            closeStream(errorMsg, output);
        }
    }

    /**
     * 按照质量压缩图片
     *
     * @param imageBytes 图片字节数组
     * @param quality    质量(0-1)
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, float quality) {
        return compressPicByScale(imageBytes, Constants.ONE_D, quality);
    }

    /**
     * 按照比例图片
     *
     * @param imageBytes 图片字节数组
     * @param scale      比例(0-1)
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, double scale) {
        return compressPicByScale(imageBytes, scale, Constants.ONE_F);
    }

    /**
     * 按照比例质量图片
     *
     * @param imageBytes 图片字节数组
     * @param width      宽
     * @param height     高
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, int width, int height) {
        return compressPicByScale(imageBytes, width, height, Constants.ONE_F);
    }

    /**
     * 按照比例质量压缩图片
     *
     * @param imageBytes 图片字节数组
     * @param scale      比例(0-1)
     * @param quality    质量(0-1)
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, double scale, float quality) {
        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream(imageBytes.length);
        String errorMsg = "compress image by scale and quality";
        try {
            Thumbnails.of(input)
                    .scale(scale)
                    .outputQuality(quality)
                    .useOriginalFormat()
                    .toOutputStream(output);
            imageBytes = output.toByteArray();
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, input, output);
        }
        return imageBytes;
    }

    /**
     * 按照比例质量压缩图片
     *
     * @param imageBytes 图片字节数组
     * @param width      宽
     * @param height     高
     * @param quality    质量(0-1)
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, int width, int height, float quality) {
        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream(imageBytes.length);
        String errorMsg = "compress image by scale and quality";
        try {
            Thumbnails.of(input)
                    .size(width, height)
                    .outputQuality(quality)
                    .useOriginalFormat()
                    .toOutputStream(output);
            imageBytes = output.toByteArray();
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, input, output);
        }
        return imageBytes;
    }

    /**
     * 按照比例质量格式压缩图片
     *
     * @param imageBytes 图片字节数组
     * @param width      宽
     * @param height     高
     * @param quality    质量(0-1)
     * @param format     图片格式
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, int width, int height, float quality, String format) {
        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream(imageBytes.length);
        String errorMsg = "compress image by scale, quality and format";
        try {
            Thumbnails.of(input)
                    .size(width, height)
                    .outputQuality(quality)
                    .outputFormat(format)
                    .toOutputStream(output);
            imageBytes = output.toByteArray();
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, input, output);
        }
        return imageBytes;
    }

    /**
     * 按照比例质量格式压缩图片
     *
     * @param imageBytes 图片字节数组
     * @param scale      比例(0-1)
     * @param quality    质量(0-1)
     * @param format     图片格式
     * @return 图片字节数组
     */
    public static byte[] compressPicByScale(byte[] imageBytes, double scale, float quality, String format) {
        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream(imageBytes.length);
        String errorMsg = "compress image by scale, quality and format";
        try {
            Thumbnails.of(input)
                    .scale(scale)
                    .outputQuality(quality)
                    .outputFormat(format)
                    .toOutputStream(output);
            imageBytes = output.toByteArray();
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, input, output);
        }
        return imageBytes;
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param httpConn http连接
     * @param input    输入流
     */
    private static void closeStream(String errorMsg, HttpURLConnection httpConn, InputStream input) {
        if (httpConn != null) {
            httpConn.disconnect();
        }
        closeStream(errorMsg, input, null);
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param input    输入流
     */
    private static void closeStream(String errorMsg, InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            log.error(errorMsg + SpecialSymbolConst.SPACE + "input.close", e);
        }
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param output   输出流
     */
    private static void closeStream(String errorMsg, OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            log.error(errorMsg + SpecialSymbolConst.SPACE + "output.close", e);
        }
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param input    输入流
     * @param output   输出流
     */
    private static void closeStream(String errorMsg, InputStream input, OutputStream output) {
        closeStream(errorMsg, input);
        closeStream(errorMsg, output);
    }

    /**
     * 关闭流
     *
     * @param errorMsg    异常信息
     * @param imageStream 图片流
     */
    private static void closeStream(String errorMsg, ImageInputStream... imageStream) {
        for (ImageInputStream imageInputStream : imageStream) {
            try {
                if (imageInputStream != null) {
                    imageInputStream.close();
                }
            } catch (IOException e) {
                log.error(errorMsg, e);
            }
        }
    }
}
