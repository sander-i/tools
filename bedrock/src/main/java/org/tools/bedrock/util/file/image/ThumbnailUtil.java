package org.tools.bedrock.util.file.image;

import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.constant.ImageConstants;
import org.tools.bedrock.util.BaseUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * 缩略图处理工具类，提供基于尺寸和质量参数的缩略图生成功能
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class ThumbnailUtil {

    private ThumbnailUtil() {
    }

    /**
     * 生成指定尺寸的缩略图
     *
     * @param originalImage 原始图片
     * @param width         缩略图宽度
     * @param height        缩略图高度
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(BufferedImage originalImage, int width, int height) {
        if (originalImage == null) {
            throw new IllegalArgumentException(ImageConstants.ORIGINAL_IMAGE_CANNOT_BE_NULL);
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(ImageConstants.WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO);
        }
        return resizeImage(originalImage, width, height);
    }

    /**
     * 生成指定尺寸的缩略图
     *
     * @param originalImage 原始图片
     * @param width         缩略图宽度
     * @param height        缩略图高度
     * @param quality       图片质量 (0-1)，此参数在此版本中不生效
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(BufferedImage originalImage, int width, int height, float quality) {
        if (originalImage == null) {
            throw new IllegalArgumentException(ImageConstants.ORIGINAL_IMAGE_CANNOT_BE_NULL);
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(ImageConstants.WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO);
        }
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException(ImageConstants.QUALITY_MUST_BE_IN_RANGE);
        }

        // 质量参数在返回BufferedImage时无法应用，仅调整尺寸
        return resizeImage(originalImage, width, height);
    }

    /**
     * 生成指定尺寸的缩略图
     *
     * @param imagePath 图片路径
     * @param width     缩略图宽度
     * @param height    缩略图高度
     * @return 缩略图
     * @throws IOException IO异常
     */
    public static BufferedImage createThumbnail(String imagePath, int width, int height) throws IOException {
        if (BaseUtils.isEmpty(imagePath)) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_PATH_CANNOT_BE_EMPTY);
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(ImageConstants.WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO);
        }
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException(ImageConstants.IMAGE_FILE_NOT_EXISTS + imagePath);
        }
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException(ImageConstants.CANNOT_READ_IMAGE_FILE + imagePath);
        }
        return createThumbnail(originalImage, width, height);
    }

    /**
     * 生成指定尺寸的缩略图
     *
     * @param imageUrl 图片URL
     * @param width    缩略图宽度
     * @param height   缩略图高度
     * @return 缩略图
     * @throws IOException IO异常
     */
    public static BufferedImage createThumbnail(URL imageUrl, int width, int height) throws IOException {
        if (imageUrl == null) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_URL_CANNOT_BE_NULL);
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(ImageConstants.WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO);
        }
        BufferedImage originalImage = ImageIO.read(imageUrl);
        if (originalImage == null) {
            throw new IOException(ImageConstants.CANNOT_READ_IMAGE_FROM_URL + imageUrl);
        }
        return createThumbnail(originalImage, width, height);
    }

    /**
     * 生成按比例缩放的缩略图
     *
     * @param originalImage 原始图片
     * @param scale         缩放比例 (0-1)
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(BufferedImage originalImage, double scale) {
        if (originalImage == null) {
            throw new IllegalArgumentException(ImageConstants.ORIGINAL_IMAGE_CANNOT_BE_NULL);
        }
        if (scale <= 0 || scale > 1) {
            throw new IllegalArgumentException(ImageConstants.SCALE_MUST_BE_POSITIVE);
        }
        int width = (int) (originalImage.getWidth() * scale);
        int height = (int) (originalImage.getHeight() * scale);
        return resizeImage(originalImage, width, height);
    }

    /**
     * 生成按比例缩放的缩略图
     *
     * @param imagePath 图片路径
     * @param scale     缩放比例 (0-1)
     * @return 缩略图
     * @throws IOException IO异常
     */
    public static BufferedImage createThumbnail(String imagePath, double scale) throws IOException {
        if (BaseUtils.isEmpty(imagePath)) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_PATH_CANNOT_BE_EMPTY);
        }
        if (scale <= 0 || scale > 1) {
            throw new IllegalArgumentException(ImageConstants.SCALE_MUST_BE_POSITIVE);
        }
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException(ImageConstants.IMAGE_FILE_NOT_EXISTS + imagePath);
        }
        BufferedImage originalImage = ImageIO.read(imageFile);
        if (originalImage == null) {
            throw new IOException(ImageConstants.CANNOT_READ_IMAGE_FILE + imagePath);
        }
        return createThumbnail(originalImage, scale);
    }

    /**
     * 生成按比例缩放的缩略图
     *
     * @param imageUrl 图片URL
     * @param scale    缩放比例 (0-1)
     * @return 缩略图
     * @throws IOException IO异常
     */
    public static BufferedImage createThumbnail(URL imageUrl, double scale) throws IOException {
        if (imageUrl == null) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_URL_CANNOT_BE_NULL);
        }
        if (scale <= 0 || scale > 1) {
            throw new IllegalArgumentException(ImageConstants.SCALE_MUST_BE_POSITIVE);
        }
        BufferedImage originalImage = ImageIO.read(imageUrl);
        if (originalImage == null) {
            throw new IOException(ImageConstants.CANNOT_READ_IMAGE_FROM_URL + imageUrl);
        }
        return createThumbnail(originalImage, scale);
    }

    /**
     * 生成指定尺寸和质量的缩略图（字节数组输入）
     *
     * @param imageBytes 图片字节数组
     * @param width      缩略图宽度
     * @param height     缩略图高度
     * @param quality    图片质量 (0-1)
     * @param format     图片格式 (如 "jpg", "png")
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(byte[] imageBytes, int width, int height, float quality, String format) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_BYTES_CANNOT_BE_EMPTY);
        }
        // 当宽度或高度小于等于0时，抛出异常
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(ImageConstants.WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO);
        }
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException(ImageConstants.QUALITY_MUST_BE_IN_RANGE);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        try {
            BufferedImage originalImage = ImageIO.read(input);
            if (originalImage == null) {
                throw new IllegalArgumentException(ImageConstants.FAILED_TO_READ_IMAGE_DATA);
            }

            // 生成缩略图（保持原始尺寸）
            return resizeImage(originalImage, width, height);
        } catch (IOException e) {
            log.error(ImageConstants.FAILED_TO_GENERATE_THUMBNAIL, e);
            throw new RuntimeException("无法从字节数组生成缩略图", e);
        } finally {
            closeStream(input);
        }
    }

    /**
     * 生成指定尺寸和质量的缩略图（字节数组输入）
     *
     * @param imageBytes 图片字节数组
     * @param width      缩略图宽度
     * @param height     缩略图高度
     * @param quality    图片质量 (0-1)
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(byte[] imageBytes, int width, int height, float quality) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_BYTES_CANNOT_BE_EMPTY);
        }
        // 当宽度或高度小于等于0时，抛出异常
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(ImageConstants.WIDTH_HEIGHT_MUST_GREATER_THAN_ZERO);
        }
        // 自动判断图片格式
        String format = ImageConstants.DEFAULT_FORMAT;
        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (originalImage != null && (originalImage.getType() == BufferedImage.TYPE_INT_ARGB ||
                    originalImage.getType() == BufferedImage.TYPE_4BYTE_ABGR ||
                    originalImage.getType() == BufferedImage.TYPE_4BYTE_ABGR_PRE)) {
                format = ImageConstants.PNG_FORMAT;
            }
        } catch (IOException e) {
            log.warn(ImageConstants.CANNOT_DETERMINE_IMAGE_FORMAT_USING_DEFAULT_JPG, e);
        }
        return createThumbnail(imageBytes, width, height, quality, format);
    }

    /**
     * 生成按比例缩放和质量的缩略图（字节数组输入）
     *
     * @param imageBytes 图片字节数组
     * @param scale      缩放比例 (0-1)
     * @param quality    图片质量 (0-1)
     * @param format     图片格式 (如 "jpg", "png")
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(byte[] imageBytes, double scale, float quality, String format) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_BYTES_CANNOT_BE_EMPTY);
        }
        if (scale <= 0 || scale > 1) {
            throw new IllegalArgumentException(ImageConstants.SCALE_MUST_BE_POSITIVE);
        }
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException(ImageConstants.QUALITY_MUST_BE_IN_RANGE);
        }

        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        try {
            BufferedImage originalImage = ImageIO.read(input);
            if (originalImage == null) {
                throw new IllegalArgumentException(ImageConstants.FAILED_TO_READ_IMAGE_DATA);
            }

            // 根据比例计算目标尺寸
            int targetWidth = Math.max(1, (int) (originalImage.getWidth() * scale));
            int targetHeight = Math.max(1, (int) (originalImage.getHeight() * scale));

            // 生成缩略图
            return resizeImage(originalImage, targetWidth, targetHeight);
        } catch (IOException e) {
            log.error(ImageConstants.FAILED_TO_GENERATE_THUMBNAIL, e);
            throw new RuntimeException("无法从字节数组生成缩略图", e);
        } finally {
            closeStream(input);
        }
    }

    /**
     * 生成按比例缩放和质量的缩略图（字节数组输入）
     *
     * @param imageBytes 图片字节数组
     * @param scale      缩放比例 (0-1)
     * @param quality    图片质量 (0-1)
     * @return 缩略图
     */
    public static BufferedImage createThumbnail(byte[] imageBytes, double scale, float quality) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException(ImageConstants.IMAGE_BYTES_CANNOT_BE_EMPTY);
        }
        // 自动判断图片格式
        String format = ImageConstants.DEFAULT_FORMAT;
        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (originalImage != null && (originalImage.getType() == BufferedImage.TYPE_INT_ARGB ||
                    originalImage.getType() == BufferedImage.TYPE_4BYTE_ABGR ||
                    originalImage.getType() == BufferedImage.TYPE_4BYTE_ABGR_PRE)) {
                format = ImageConstants.PNG_FORMAT;
            }
        } catch (IOException e) {
            log.warn(ImageConstants.CANNOT_DETERMINE_IMAGE_FORMAT_USING_DEFAULT_JPG, e);
        }
        return createThumbnail(imageBytes, scale, quality, format);
    }

    /**
     * 调整图片大小
     *
     * @param originalImage 原始图片
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @return 调整后的图片
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();

        // 设置渲染提示以提高图像质量
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    /**
     * 写入图像并控制质量
     *
     * @param image   图像
     * @param output  输出流
     * @param quality 质量 (0-1)
     * @param format  图像格式
     * @throws IOException IO异常
     */
    private static void writeImage(BufferedImage image, OutputStream output, float quality, String format) throws IOException {
        // 对JPEG格式使用专门的质量控制
        if (ImageConstants.JPEG_FORMAT.equalsIgnoreCase(format)) {
            writeJpegImage(image, output, quality);
            return;
        }

        // 对PNG等无损格式，质量参数不生效，使用标准写入方法
        ImageIO.write(image, format, output);
    }

    /**
     * 写入JPEG图像并控制质量
     *
     * @param image   图像
     * @param output  输出流
     * @param quality 质量 (0-1)
     * @throws IOException IO异常
     */
    private static void writeJpegImage(BufferedImage image, OutputStream output, float quality) throws IOException {
        // 查找JPEG图像写入器
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(ImageConstants.JPEG_FORMAT);
        if (!writers.hasNext()) {
            throw new IOException(ImageConstants.CANNOT_FIND_JPEG_WRITER);
        }

        ImageWriter writer = writers.next();
        ImageOutputStream ios = null;
        try {
            ios = ImageIO.createImageOutputStream(output);
            writer.setOutput(ios);

            // 创建JPEG参数并设置质量
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(quality);

            // 写入图像
            writer.write(null, new IIOImage(image, null, null), jpegParams);
        } finally {
            if (ios != null) {
                try {
                    ios.close();
                } catch (IOException e) {
                    log.warn(ImageConstants.FAILED_TO_CLOSE_IMAGE_OUTPUT_STREAM, e);
                }
            }
            writer.dispose();
        }
    }

    /**
     * 关闭输入流
     *
     * @param stream 输入流
     */
    private static void closeStream(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                log.warn(ImageConstants.FAILED_TO_CLOSE_INPUT_STREAM, e);
            }
        }
    }
}