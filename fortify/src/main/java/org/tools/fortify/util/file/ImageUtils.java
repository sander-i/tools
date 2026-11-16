package org.tools.fortify.util.file;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.util.file.image.ImageUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


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
            ImageUtil.closeStream(errorMsg, input, output);
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
            ImageUtil.closeStream(errorMsg, input, output);
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
            ImageUtil.closeStream(errorMsg, input, output);
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
            ImageUtil.closeStream(errorMsg, input, output);
        }
        return imageBytes;
    }

}
