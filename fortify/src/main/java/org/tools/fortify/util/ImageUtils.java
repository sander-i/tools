package org.tools.fortify.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.constant.SpecialSymbolConsts;
import org.tools.bedrock.util.BaseUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author sander
 * @version 1.0
 * @date 2023/03/23 23:21
 * @description 图片工具
 * @scope all
 */
@Slf4j
public class ImageUtils {
    /**
     * request 请求方式
     */
    private static final String REQUEST_METHOD = "GET";
    /**
     * 3000/ms
     */
    private static final int CONNECT_TIMEOUT = 3000;

    private ImageUtils() {
    }

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
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String errorMsg = "图片文件转字节数组";
        try {
            File f = new File(filePath);
            BufferedImage buf = ImageIO.read(f);
            ImageIO.write(buf, formatName(filePath), buffer);
            return buffer.toByteArray();
        } catch (IOException e) {
            log.error(errorMsg, e);
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
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        String errorMsg = "将二进制转换为图片(损失部分画质)";
        try {
            File f = new File(outfilePath);
            if (!f.exists()) {
                log.debug(outfilePath + "路径不存在!");
                File parent = new File(f.getParent());
                if (parent.mkdirs()) {
                    log.debug(outfilePath + "路径创建成功!");
                }
                if (f.createNewFile()) {
                    log.debug(outfilePath + "文件创建成功!");
                }
            }
            BufferedImage buffer = ImageIO.read(input);
            ImageIO.write(buffer, formatName(outfilePath), f);
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            closeStream(errorMsg, input);
        }
    }

    /**
     * 通过文件路径截取文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public static String formatName(String filePath) {
        return filePath.substring(filePath.indexOf(SpecialSymbolConsts.POINT) + Constants.ONE_I);
    }

    /**
     * 图片文件转字符数组
     *
     * @param filePath 图片路径
     * @return 图片字符数组
     */
    public static byte[] imageToBinary(String filePath) {
        File file = new File(filePath);
        FileImageInputStream input = null;
        String errorMsg = "图片文件转字符数组";
        try {
            input = new FileImageInputStream(file);
            return readInputStream(input);
        } catch (IOException e) {
            log.error(errorMsg, e);
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
        File f = new File(outfilePath);
        FileImageOutputStream output = null;
        String errorMsg = "二进制转图片文件";
        try {
            if (!f.exists()) {
                log.debug(outfilePath + "路径不存在!");
                File parent = new File(f.getParent());
                if (parent.mkdirs()) {
                    log.debug(outfilePath + "路径创建成功!");
                }
                if (f.createNewFile()) {
                    log.debug(outfilePath + "文件创建成功!");
                }
            }
            output = new FileImageOutputStream(f);
            // 将字节数组写入硬盘
            output.write(bytes, Constants.ZERO_I, bytes.length);
        } catch (IOException e) {
            log.error(errorMsg, e);
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
            log.error(errorMsg + SpecialSymbolConsts.SPACE + "input.close", e);
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
            log.error(errorMsg + SpecialSymbolConsts.SPACE + "output.close", e);
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
