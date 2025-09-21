package org.tools.bedrock.util.file;

import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.constant.FileUtilConstants;
import org.tools.bedrock.constant.v2.SpecialSymbolConst;
import org.tools.bedrock.util.BaseUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类，提供文件读取、拷贝、迁移、压缩、解压等功能
 *
 * @author sander
 * @version 1.0
 */
public class FileUtil {

    private FileUtil() {
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
     * 读取resources目录下文件内容为字符串
     *
     * @param resourcePath resources目录下的文件路径
     * @return 文件内容字符串
     * @throws IOException IO异常
     */
    public static String readResourceFile(String resourcePath) throws IOException {
        try (InputStream is = getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException(FileUtilConstants.RESOURCE_FILE_NOT_FOUND + resourcePath);
            }
            return readInputStreamAsString(is);
        }
    }

    /**
     * 读取resources目录下文件内容为字节数组
     *
     * @param resourcePath resources目录下的文件路径
     * @return 文件内容字节数组
     * @throws IOException IO异常
     */
    public static byte[] readResourceFileBytes(String resourcePath) throws IOException {
        try (InputStream is = getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException(FileUtilConstants.RESOURCE_FILE_NOT_FOUND + resourcePath);
            }
            return readInputStreamAsBytes(is);
        }
    }

    /**
     * 获取resources目录下的文件输入流
     *
     * @param resourcePath resources目录下的文件路径
     * @return 文件输入流
     */
    public static InputStream getResourceAsStream(String resourcePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);

        if (inputStream == null) {
            // 如果使用当前线程的类加载器找不到，则尝试使用FileUtil类的类加载器
            inputStream = FileUtil.class.getClassLoader().getResourceAsStream(resourcePath);
        }

        return inputStream;
    }

    /**
     * 获取resources目录下的文件URL
     *
     * @param resourcePath resources目录下的文件路径
     * @return 文件URL
     */
    public static URL getResourceURL(String resourcePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(resourcePath);

        if (url == null) {
            // 如果使用当前线程的类加载器找不到，则尝试使用FileUtil类的类加载器
            url = FileUtil.class.getClassLoader().getResource(resourcePath);
        }

        return url;
    }

    /**
     * 从输入流读取内容为字符串
     *
     * @param inputStream 输入流
     * @return 字符串内容
     * @throws IOException IO异常
     */
    private static String readInputStreamAsString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[FileUtilConstants.BUFFER_SIZE];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(FileUtilConstants.UTF_8_ENCODING);
    }

    /**
     * 从输入流读取内容为字节数组
     *
     * @param inputStream 输入流
     * @return 字节数组内容
     * @throws IOException IO异常
     */
    private static byte[] readInputStreamAsBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[FileUtilConstants.BUFFER_SIZE];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }

    /**
     * 读取文件内容为字符串
     *
     * @param filePath 文件路径
     * @return 文件内容字符串
     * @throws IOException IO异常
     */
    public static String readFile(String filePath) throws IOException {
        return readFile(Paths.get(filePath));
    }

    /**
     * 读取文件内容为字符串
     *
     * @param path 文件路径
     * @return 文件内容字符串
     * @throws IOException IO异常
     */
    public static String readFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    /**
     * 读取文件内容为字节数组
     *
     * @param filePath 文件路径
     * @return 文件内容字节数组
     * @throws IOException IO异常
     */
    public static byte[] readFileBytes(String filePath) throws IOException {
        return readFileBytes(Paths.get(filePath));
    }

    /**
     * 读取文件内容为字节数组
     *
     * @param path 文件路径
     * @return 文件内容字节数组
     * @throws IOException IO异常
     */
    public static byte[] readFileBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    /**
     * 拷贝文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @throws IOException IO异常
     */
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        copyFile(Paths.get(sourcePath), Paths.get(targetPath));
    }

    /**
     * 拷贝文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @throws IOException IO异常
     */
    public static void copyFile(Path sourcePath, Path targetPath) throws IOException {
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 拷贝目录
     *
     * @param sourceDir 源目录路径
     * @param targetDir 目标目录路径
     * @throws IOException IO异常
     */
    public static void copyDirectory(String sourceDir, String targetDir) throws IOException {
        copyDirectory(Paths.get(sourceDir), Paths.get(targetDir));
    }

    /**
     * 拷贝目录
     *
     * @param sourceDir 源目录路径
     * @param targetDir 目标目录路径
     * @throws IOException IO异常
     */
    public static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
                if (!Files.exists(targetPath)) {
                    Files.createDirectories(targetPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetPath = targetDir.resolve(sourceDir.relativize(file));
                Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 移动文件或目录
     *
     * @param sourcePath 源路径
     * @param targetPath 目标路径
     * @throws IOException IO异常
     */
    public static void move(String sourcePath, String targetPath) throws IOException {
        move(Paths.get(sourcePath), Paths.get(targetPath));
    }

    /**
     * 移动文件或目录
     *
     * @param sourcePath 源路径
     * @param targetPath 目标路径
     * @throws IOException IO异常
     */
    public static void move(Path sourcePath, Path targetPath) throws IOException {
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 压缩文件或目录为ZIP格式
     *
     * @param sourcePath 源文件或目录路径
     * @param zipPath    ZIP文件路径
     * @throws IOException IO异常
     */
    public static void compressToZip(String sourcePath, String zipPath) throws IOException {
        compressToZip(Paths.get(sourcePath), Paths.get(zipPath));
    }

    /**
     * 压缩文件或目录为ZIP格式
     *
     * @param sourcePath 源文件或目录路径
     * @param zipPath    ZIP文件路径
     * @throws IOException IO异常
     */
    public static void compressToZip(Path sourcePath, Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            if (Files.isDirectory(sourcePath)) {
                compressDirectoryToZip(sourcePath, sourcePath.getFileName().toString(), zos);
            } else {
                compressFileToZip(sourcePath, zos);
            }
        }
    }

    /**
     * 压缩目录到ZIP输出流
     *
     * @param dirPath 目录路径
     * @param parent  父路径
     * @param zipOut  ZIP输出流
     * @throws IOException IO异常
     */
    private static void compressDirectoryToZip(Path dirPath, String parent, ZipOutputStream zipOut) throws IOException {
        File dir = dirPath.toFile();
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                compressDirectoryToZip(file.toPath(), parent + "/" + file.getName(), zipOut);
            } else {
                ZipEntry zipEntry = new ZipEntry(parent + "/" + file.getName());
                zipOut.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] bytes = new byte[FileUtilConstants.BUFFER_SIZE];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
                zipOut.closeEntry();
            }
        }
    }

    /**
     * 压缩单个文件到ZIP输出流
     *
     * @param filePath 文件路径
     * @param zipOut   ZIP输出流
     * @throws IOException IO异常
     */
    private static void compressFileToZip(Path filePath, ZipOutputStream zipOut) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
        zipOut.putNextEntry(zipEntry);

        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            byte[] bytes = new byte[FileUtilConstants.BUFFER_SIZE];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
        zipOut.closeEntry();
    }

    /**
     * 解压ZIP文件
     *
     * @param zipPath    ZIP文件路径
     * @param targetPath 解压目标路径
     * @throws IOException IO异常
     */
    public static void decompressZip(String zipPath, String targetPath) throws IOException {
        decompressZip(Paths.get(zipPath), Paths.get(targetPath));
    }

    /**
     * 解压ZIP文件
     *
     * @param zipPath    ZIP文件路径
     * @param targetPath 解压目标路径
     * @throws IOException IO异常
     */
    public static void decompressZip(Path zipPath, Path targetPath) throws IOException {
        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                Path newPath = targetPath.resolve(zipEntry.getName()).normalize();

                // 防止路径遍历漏洞
                if (!newPath.startsWith(targetPath)) {
                    throw new IOException(FileUtilConstants.ENTRY_OUTSIDE_TARGET_DIR + zipEntry.getName());
                }

                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    // 合并两个if语句为一个
                    if (newPath.getParent() != null && Files.notExists(newPath.getParent())) {
                        Files.createDirectories(newPath.getParent());
                    }
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
    }

    /**
     * 压缩文件为GZIP格式
     *
     * @param sourcePath 源文件路径
     * @param gzipPath   GZIP文件路径
     * @throws IOException IO异常
     */
    public static void compressToGzip(String sourcePath, String gzipPath) throws IOException {
        compressToGzip(Paths.get(sourcePath), Paths.get(gzipPath));
    }

    /**
     * 压缩文件为GZIP格式
     *
     * @param sourcePath 源文件路径
     * @param gzipPath   GZIP文件路径
     * @throws IOException IO异常
     */
    public static void compressToGzip(Path sourcePath, Path gzipPath) throws IOException {
        try (GZIPOutputStream gzos = new GZIPOutputStream(Files.newOutputStream(gzipPath));
             FileInputStream fis = new FileInputStream(sourcePath.toFile())) {
            byte[] buffer = new byte[FileUtilConstants.BUFFER_SIZE];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, len);
            }
        }
    }

    /**
     * 解压GZIP文件
     *
     * @param gzipPath   GZIP文件路径
     * @param targetPath 解压后文件路径
     * @throws IOException IO异常
     */
    public static void decompressGzip(String gzipPath, String targetPath) throws IOException {
        decompressGzip(Paths.get(gzipPath), Paths.get(targetPath));
    }

    /**
     * 解压GZIP文件
     *
     * @param gzipPath   GZIP文件路径
     * @param targetPath 解压后文件路径
     * @throws IOException IO异常
     */
    public static void decompressGzip(Path gzipPath, Path targetPath) throws IOException {
        try (GZIPInputStream gzis = new GZIPInputStream(Files.newInputStream(gzipPath));
             FileOutputStream fos = new FileOutputStream(targetPath.toFile())) {
            byte[] buffer = new byte[FileUtilConstants.BUFFER_SIZE];
            int len;
            while ((len = gzis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }
    }
}