package org.tools.bedrock.util.file.image;

import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.base.image.SimilarityConfig;
import org.tools.bedrock.base.image.SimilarityResult;
import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.constant.ImageConstants;
import org.tools.bedrock.constant.v2.SpecialSymbolConst;
import org.tools.bedrock.util.BaseUtils;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 图像处理工具类，提供基础图像操作功能和图像相似度融合计算
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class ImageUtil {

    private ImageUtil() {
    }

    /**
     * 调整图片大小
     *
     * @param originalImage 原始图片
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @return 调整后的图片
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, ImageConstants.RGB_IMAGE_TYPE);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    /**
     * 计算两张图片的融合相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @param config 相似度计算配置
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     */
    public static SimilarityResult calculateFusedSimilarity(BufferedImage image1, BufferedImage image2, SimilarityConfig config) {
        Map<String, Double> similarities = new HashMap<>();
        double totalWeight = 0.0;
        double weightedSum = 0.0;

        // 计算像素匹配相似度
        if (config.isPixelMatchingEnabled()) {
            double pixelMatchingSimilarity = ImagePixelUtil.calculatePixelSimilarityWithResize(image1, image2);
            similarities.put("PixelMatching", pixelMatchingSimilarity);
            weightedSum += pixelMatchingSimilarity * config.getPixelMatchingWeight();
            totalWeight += config.getPixelMatchingWeight();
        }

        // 计算直方图相似度
        if (config.isHistogramEnabled()) {
            double histogramSimilarity = ImageHistogramUtil.calculateSimilarity(image1, image2);
            similarities.put("Histogram", histogramSimilarity);
            weightedSum += histogramSimilarity * config.getHistogramWeight();
            totalWeight += config.getHistogramWeight();
        }

        // 计算SSIM相似度
        if (config.isSsimEnabled()) {
            double ssimSimilarity = ImageStructureUtil.calculateSSIM(image1, image2);
            similarities.put("SSIM", ssimSimilarity);
            weightedSum += ssimSimilarity * config.getSsimWeight();
            totalWeight += config.getSsimWeight();
        }

        // 计算哈希相似度
        if (config.isAvgHashEnabled() || config.isPHashEnabled() || config.isDHashEnabled()) {
            Map<String, Double> hashSimilarities = calculateHashSimilarities(image1, image2, config);
            for (Map.Entry<String, Double> entry : hashSimilarities.entrySet()) {
                similarities.put(entry.getKey(), entry.getValue());
                switch (entry.getKey()) {
                    case "AverageHash":
                        weightedSum += entry.getValue() * config.getAvgHashWeight();
                        totalWeight += config.getAvgHashWeight();
                        break;
                    case "PerceptualHash":
                        weightedSum += entry.getValue() * config.getPHashWeight();
                        totalWeight += config.getPHashWeight();
                        break;
                    case "DifferenceHash":
                        weightedSum += entry.getValue() * config.getDHashWeight();
                        totalWeight += config.getDHashWeight();
                        break;
                    default:
                        // 不应该到达这里，因为我们只放入了已知的键
                        break;
                }
            }
        }

        // 计算特征匹配相似度
        if (config.isFeatureMatchingEnabled()) {
            double featureMatchingSimilarity = ImageFeatureUtil.calculateFeatureMatchingSimilarity(image1, image2);
            similarities.put("FeatureMatching", featureMatchingSimilarity);
            weightedSum += featureMatchingSimilarity * config.getFeatureMatchingWeight();
            totalWeight += config.getFeatureMatchingWeight();
        }

        // 计算加权平均相似度
        double overallSimilarity = totalWeight > 0 ? weightedSum / totalWeight : 0.0;

        return new SimilarityResult(similarities, overallSimilarity);
    }

    /**
     * 计算图片的哈希相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @param config 相似度计算配置
     * @return 哈希相似度映射
     */
    private static Map<String, Double> calculateHashSimilarities(BufferedImage image1, BufferedImage image2, SimilarityConfig config) {
        Map<String, Double> hashSimilarities = new HashMap<>();

        // 计算平均哈希相似度
        if (config.isAvgHashEnabled()) {
            double avgHashSimilarity = ImageHashUtil.calculateAverageHashSimilarity(image1, image2);
            hashSimilarities.put("AverageHash", avgHashSimilarity);
        }

        // 计算感知哈希相似度
        if (config.isPHashEnabled()) {
            double pHashSimilarity = ImageHashUtil.calculatePerceptualHashSimilarity(image1, image2);
            hashSimilarities.put("PerceptualHash", pHashSimilarity);
        }

        // 计算差异哈希相似度
        if (config.isDHashEnabled()) {
            double dHashSimilarity = ImageHashUtil.calculateDifferenceHashSimilarity(image1, image2);
            hashSimilarities.put("DifferenceHash", dHashSimilarity);
        }

        return hashSimilarities;
    }

    /**
     * 计算两张图片的融合相似度（使用默认配置）
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     */
    public static SimilarityResult calculateFusedSimilarity(BufferedImage image1, BufferedImage image2) {
        return calculateFusedSimilarity(image1, image2, new SimilarityConfig());
    }

    /**
     * 计算两张图片的融合相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @param config     相似度计算配置
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     * @throws IOException IO异常
     */
    public static SimilarityResult calculateFusedSimilarity(String imagePath1, String imagePath2, SimilarityConfig config) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateFusedSimilarity(image1, image2, config);
    }

    /**
     * 计算两张图片的融合相似度（使用默认配置）
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     * @throws IOException IO异常
     */
    public static SimilarityResult calculateFusedSimilarity(String imagePath1, String imagePath2) throws IOException {
        return calculateFusedSimilarity(imagePath1, imagePath2, new SimilarityConfig());
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
        String errorMsg = ImageConstants.IMAGE_URL_TO_BINARY_ERROR;
        try {
            url = new URL(urlPath);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod(ImageConstants.REQUEST_METHOD);
            httpConn.setConnectTimeout(ImageConstants.CONNECT_TIMEOUT);
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
        String errorMsg = ImageConstants.IMAGE_URL_TO_BINARY_NO_TIMEOUT_ERROR;
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
    public static <T> byte[] readInputStream(T stream) {
        InputStream inputStream = stream instanceof InputStream ? (InputStream) stream : null;
        FileImageInputStream fileImageInputStream = stream instanceof FileImageInputStream ? (FileImageInputStream) stream : null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String errorMsg = ImageConstants.READ_INPUT_STREAM_ERROR;
        try {
            byte[] buffer = Constants.MAXIMUM_BYTE_ARRAY;
            int len;
            if (inputStream != null) {
                while ((len = inputStream.read(buffer)) != Constants.NEGATIVE_ONE_I) {
                    output.write(buffer, Constants.ZERO_I, len);
                }
            }
            if (fileImageInputStream != null) {
                while ((len = fileImageInputStream.read(buffer)) != Constants.NEGATIVE_ONE_I) {
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
        String errorMsg = ImageConstants.IMAGE_TO_BINARY_LOSS_ERROR;

        try {
            File f = new File(filePath);

            // 检查文件是否存在
            if (!f.exists()) {
                log.warn(ImageConstants.FILE_NOT_FOUND_WARN, filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 检查是否为文件（而不是目录）
            if (!f.isFile()) {
                log.warn(ImageConstants.NOT_A_FILE_WARN, filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 读取图片文件
            BufferedImage buf = ImageIO.read(f);
            if (buf == null) {
                log.warn(ImageConstants.CANNOT_READ_IMAGE_WARN, filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 获取文件扩展名
            String format = FileUtil.formatName(filePath);
            // 如果没有扩展名或扩展名无效，使用默认格式
            if (BaseUtils.isEmpty(format)) {
                format = "jpg"; // 默认使用jpg格式
                log.debug("文件无扩展名，使用默认格式: {}", format);
            }

            // 写入图片数据
            boolean success = ImageIO.write(buf, format, buffer);
            if (!success) {
                log.warn(ImageConstants.WRITE_IMAGE_FAILED_WARN, format);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            log.debug(ImageConstants.CONVERT_SUCCESS_DEBUG, filePath, buffer.size());
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
        String errorMsg = ImageConstants.BINARY_TO_IMAGE_LOSS_ERROR;

        try {
            File f = new File(outfilePath);

            // 确保父目录存在
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.debug(ImageConstants.CREATE_DIR_DEBUG, parentDir.getAbsolutePath());
                } else {
                    log.warn(ImageConstants.CREATE_DIR_FAILED_WARN, parentDir.getAbsolutePath());
                }
            }

            // 创建文件（如果不存在）
            if (!f.exists()) {
                if (f.createNewFile()) {
                    log.debug(ImageConstants.CREATE_FILE_DEBUG, f.getAbsolutePath());
                } else {
                    log.warn(ImageConstants.CREATE_DIR_FAILED_WARN, f.getAbsolutePath());
                }
            } else {
                log.debug(ImageConstants.FILE_EXISTS_DEBUG, f.getAbsolutePath());
            }

            BufferedImage buffer = ImageIO.read(input);
            if (buffer == null) {
                log.warn(ImageConstants.CANNOT_READ_IMAGE_DATA_WARN);
                return;
            }

            ImageIO.write(buffer, FileUtil.formatName(outfilePath), f);
            log.debug(ImageConstants.CONVERT_IMAGE_SUCCESS_DEBUG, outfilePath);

        } catch (IOException e) {
            log.error("转换图片文件失败，路径: {}", outfilePath, e);
        } catch (Exception e) {
            log.error("处理图片文件时发生未知错误，路径: {}", outfilePath, e);
        } finally {
            closeStream(errorMsg, input);
        }
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
        String errorMsg = ImageConstants.IMAGE_TO_BINARY_ERROR;

        try {
            // 检查文件是否存在
            if (!file.exists()) {
                log.warn(ImageConstants.FILE_NOT_FOUND_WARN, filePath);
                return Constants.EMPTY_BYTE_ARRAY;
            }

            // 检查是否为文件（而不是目录）
            if (!file.isFile()) {
                log.warn(ImageConstants.NOT_A_FILE_WARN, filePath);
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
        String errorMsg = ImageConstants.BINARY_TO_IMAGE_ERROR;

        try {
            // 确保父目录存在
            File parentDir = f.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    log.debug(ImageConstants.CREATE_DIR_DEBUG, parentDir.getAbsolutePath());
                } else {
                    log.warn(ImageConstants.CREATE_DIR_FAILED_WARN, parentDir.getAbsolutePath());
                }
            }

            // 创建文件（如果不存在）
            if (!f.exists()) {
                if (f.createNewFile()) {
                    log.debug(ImageConstants.CREATE_FILE_DEBUG, f.getAbsolutePath());
                } else {
                    log.warn(ImageConstants.CREATE_DIR_FAILED_WARN, f.getAbsolutePath());
                }
            } else {
                log.debug(ImageConstants.FILE_EXISTS_DEBUG, f.getAbsolutePath());
            }

            output = new FileImageOutputStream(f);
            // 将字节数组写入硬盘
            output.write(bytes, Constants.ZERO_I, bytes.length);
            output.flush();
            log.debug(ImageConstants.WRITE_FILE_SUCCESS_DEBUG, bytes.length, outfilePath);

        } catch (IOException e) {
            log.error("写入图片文件失败，路径: {}", outfilePath, e);
        } catch (Exception e) {
            log.error("处理图片文件时发生未知错误，路径: {}", outfilePath, e);
        } finally {
            closeStream(errorMsg, output);
        }
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param httpConn http连接
     * @param input    输入流
     */
    public static void closeStream(String errorMsg, HttpURLConnection httpConn, InputStream input) {
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
    public static void closeStream(String errorMsg, InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            log.error(errorMsg + SpecialSymbolConst.SPACE + ImageConstants.INPUT_CLOSE_ERROR, e);
        }
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param output   输出流
     */
    public static void closeStream(String errorMsg, OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            log.error(errorMsg + SpecialSymbolConst.SPACE + ImageConstants.OUTPUT_CLOSE_ERROR, e);
        }
    }

    /**
     * 关闭流
     *
     * @param errorMsg 异常信息
     * @param input    输入流
     * @param output   输出流
     */
    public static void closeStream(String errorMsg, InputStream input, OutputStream output) {
        closeStream(errorMsg, input);
        closeStream(errorMsg, output);
    }

    /**
     * 关闭流
     *
     * @param errorMsg    异常信息
     * @param imageStream 图片流
     */
    public static void closeStream(String errorMsg, ImageInputStream... imageStream) {
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