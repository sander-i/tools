package org.tools.fortify.util.file;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.constant.v2.SpecialSymbolConst;
import org.tools.bedrock.util.BaseUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 图片工具
 *
 * @author sander
 * @version 1.0
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
        String errorMsg = "图片URL转字符数组";
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
        String errorMsg = "图片URL转字符数组(无超时限制)";
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
        FileImageInputStream fileInputStream = stream instanceof FileImageInputStream ? (FileImageInputStream) stream : null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String errorMsg = "读取输入流返回字节数组";
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
            log.warn("文件路径为空或null，无法读取图片文件");
            return Constants.EMPTY_BYTE_ARRAY;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String errorMsg = "图片文件转字节数组";

        try {
            File f = new File(filePath);

            // 检查文件是否存在
            if (!f.exists()) {
                log.warn("图片文件不存在: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 检查是否为文件（而不是目录）
            if (!f.isFile()) {
                log.warn("指定路径不是文件: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 读取图片文件
            BufferedImage buf = ImageIO.read(f);
            if (buf == null) {
                log.warn("无法读取图片文件，可能不是有效的图片格式: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 获取文件扩展名
            String format = formatName(filePath);
            // 如果没有扩展名或扩展名无效，使用默认格式
            if (BaseUtils.isEmpty(format)) {
                format = "jpg"; // 默认使用jpg格式
                log.debug("文件无扩展名，使用默认格式: {}", format);
            }

            // 写入图片数据
            boolean success = ImageIO.write(buf, format, buffer);
            if (!success) {
                log.warn("写入图片数据失败，可能不支持的格式: {}", format);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            log.debug("成功将图片文件转换为字节数组，文件: {}, 大小: {} 字节", filePath, buffer.size());
            return buffer.toByteArray();

        } catch (IOException e) {
            log.error("读取或转换图片文件失败，路径: {}", filePath, e);
        } catch (Exception e) {
            log.error("处理图片文件时发生未知错误，路径: {}", filePath, e);
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
            log.warn("图片字节数组为null，无法写入文件");
            return;
        }

        if (bytes.length == 0) {
            log.warn("图片字节数组为空，无法写入文件");
            return;
        }

        if (BaseUtils.isEmpty(outfilePath)) {
            log.warn("输出文件路径为空或null，无法写入文件");
            return;
        }

        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        String errorMsg = "将二进制转换为图片(损失部分画质)";

        try {
            File f = new File(outfilePath);

            // 确保父目录存在
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.debug("创建目录成功: {}", parentDir.getAbsolutePath());
                } else {
                    log.warn("创建目录失败: {}", parentDir.getAbsolutePath());
                }
            }

            // 创建文件（如果不存在）
            if (!f.exists()) {
                if (f.createNewFile()) {
                    log.debug("创建文件成功: {}", f.getAbsolutePath());
                } else {
                    log.warn("创建文件失败: {}", f.getAbsolutePath());
                }
            } else {
                log.debug("文件已存在，将覆盖: {}", f.getAbsolutePath());
            }

            BufferedImage buffer = ImageIO.read(input);
            if (buffer == null) {
                log.warn("无法读取图片数据，可能不是有效的图片格式");
                return;
            }

            ImageIO.write(buffer, formatName(outfilePath), f);
            log.debug("成功将字节数组转换为图片文件，路径: {}", outfilePath);

        } catch (IOException e) {
            log.error("转换图片文件失败，路径: {}", outfilePath, e);
        } catch (Exception e) {
            log.error("处理图片文件时发生未知错误，路径: {}", outfilePath, e);
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
            log.warn("文件路径为空或null，无法读取图片文件");
            return Constants.EMPTY_BYTE_ARRAY;
        }

        File file = new File(filePath);
        FileImageInputStream input = null;
        String errorMsg = "图片文件转字符数组";

        try {
            // 检查文件是否存在
            if (!file.exists()) {
                log.warn("图片文件不存在: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 检查是否为文件（而不是目录）
            if (!file.isFile()) {
                log.warn("指定路径不是文件: {}", filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            input = new FileImageInputStream(file);
            return readInputStream(input);
        } catch (IOException e) {
            log.error("读取图片文件失败，路径: {}", filePath, e);
        } catch (Exception e) {
            log.error("处理图片文件时发生未知错误，路径: {}", filePath, e);
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
            log.warn("图片字节数组为null，无法写入文件");
            return;
        }

        if (bytes.length == 0) {
            log.warn("图片字节数组为空，无法写入文件");
            return;
        }

        if (BaseUtils.isEmpty(outfilePath)) {
            log.warn("输出文件路径为空或null，无法写入文件");
            return;
        }

        File f = new File(outfilePath);
        FileImageOutputStream output = null;
        String errorMsg = "二进制转图片文件";

        try {
            // 确保父目录存在
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.debug("创建目录成功: {}", parentDir.getAbsolutePath());
                } else {
                    log.warn("创建目录失败: {}", parentDir.getAbsolutePath());
                }
            }

            // 创建文件（如果不存在）
            if (!f.exists()) {
                if (f.createNewFile()) {
                    log.debug("创建文件成功: {}", f.getAbsolutePath());
                } else {
                    log.warn("创建文件失败: {}", f.getAbsolutePath());
                }
            } else {
                log.debug("文件已存在，将覆盖: {}", f.getAbsolutePath());
            }

            output = new FileImageOutputStream(f);
            // 将字节数组写入硬盘
            output.write(bytes, Constants.ZERO_I, bytes.length);
            output.flush();
            log.debug("成功将字节数组写入图片文件，文件大小: {} 字节，路径: {}", bytes.length, outfilePath);

        } catch (IOException e) {
            log.error("写入图片文件失败，路径: {}", outfilePath, e);
        } catch (Exception e) {
            log.error("处理图片文件时发生未知错误，路径: {}", outfilePath, e);
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
        String errorMsg = "按照比例质量压缩图片";
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
        String errorMsg = "按照比例质量压缩图片";
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
        String errorMsg = "按照比例质量格式压缩图片";
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
        String errorMsg = "按照比例质量格式压缩图片";
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
