package org.tools.bedrock.util.file;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

/**
 * FileUtil 冒烟测试类
 * 测试文件工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class FileUtilTest {

    private Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

    @Test
    public void testReadResourceFile() throws IOException {
        // 测试读取存在的资源文件
        String content = FileUtil.readResourceFile("static/315805.jpg");
        assertNotNull(content);
        assertFalse(content.isEmpty());
    }

    @Test
    public void testReadResourceFileBytes() throws IOException {
        // 测试读取存在的资源文件为字节数组
        byte[] content = FileUtil.readResourceFileBytes("static/315805.jpg");
        assertNotNull(content);
        assertTrue(content.length > 0);
    }

    @Test(expected = IOException.class)
    public void testReadResourceFile_NotFound() throws IOException {
        // 测试读取不存在的资源文件应该抛出异常
        FileUtil.readResourceFile("nonexistent.txt");
    }

    @Test
    public void testGetResourceAsStream() {
        // 测试获取资源文件输入流
        InputStream inputStream = FileUtil.getResourceAsStream("static/315805.jpg");
        assertNotNull(inputStream);
    }

    @Test
    public void testGetResourceAsStream_NotFound() {
        // 测试获取不存在的资源文件输入流应该返回null
        InputStream inputStream = FileUtil.getResourceAsStream("nonexistent.txt");
        assertNull(inputStream);
    }

    @Test
    public void testGetResourceURL() {
        // 测试获取资源文件URL
        URL url = FileUtil.getResourceURL("static/315805.jpg");
        assertNotNull(url);
    }

    @Test
    public void testGetResourceURL_NotFound() {
        // 测试获取不存在的资源文件URL应该返回null
        URL url = FileUtil.getResourceURL("nonexistent.txt");
        assertNull(url);
    }

    @Test
    public void testReadFile() throws IOException {
        // 创建测试文件
        String content = "Hello, World!";
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, content.getBytes());

        try {
            // 测试读取文件内容为字符串
            String readContent = FileUtil.readFile(testFile.toString());
            assertEquals(content, readContent);

            // 测试读取文件内容为字节数组
            byte[] readBytes = FileUtil.readFileBytes(testFile.toString());
            assertArrayEquals(content.getBytes(), readBytes);
        } finally {
            // 清理测试文件
            Files.deleteIfExists(testFile);
        }
    }

    @Test
    public void testCopyFile() throws IOException {
        // 创建源文件
        String content = "Hello, Copy!";
        Path sourceFile = tempDir.resolve("source.txt");
        Files.write(sourceFile, content.getBytes());

        try {
            // 拷贝文件
            Path targetFile = tempDir.resolve("target.txt");
            FileUtil.copyFile(sourceFile.toString(), targetFile.toString());

            try {
                // 验证文件拷贝成功
                assertTrue(Files.exists(targetFile));
                assertEquals(content, FileUtil.readFile(targetFile.toString()));
            } finally {
                // 清理目标文件
                Files.deleteIfExists(targetFile);
            }
        } finally {
            // 清理源文件
            Files.deleteIfExists(sourceFile);
        }
    }

    @Test
    public void testMove() throws IOException {
        // 创建源文件
        String content = "Hello, Move!";
        Path sourceFile = tempDir.resolve("source.txt");
        Files.write(sourceFile, content.getBytes());

        try {
            // 移动文件
            Path targetFile = tempDir.resolve("target.txt");
            FileUtil.move(sourceFile.toString(), targetFile.toString());

            try {
                // 验证文件移动成功
                assertFalse(Files.exists(sourceFile));
                assertTrue(Files.exists(targetFile));
                assertEquals(content, FileUtil.readFile(targetFile.toString()));
            } finally {
                // 清理目标文件
                Files.deleteIfExists(targetFile);
            }
        } finally {
            // 清理源文件（如果还存在）
            Files.deleteIfExists(sourceFile);
        }
    }

    @Test
    public void testCompressAndDecompressZip() throws IOException {
        // 创建测试文件
        String content = "Hello, Zip!";
        Path sourceFile = tempDir.resolve("source.txt");
        Files.write(sourceFile, content.getBytes());

        try {
            // 压缩文件
            Path zipFile = tempDir.resolve("test.zip");
            FileUtil.compressToZip(sourceFile.toString(), zipFile.toString());

            try {
                // 验证zip文件创建成功
                assertTrue(Files.exists(zipFile));

                // 解压文件
                Path unzipDir = tempDir.resolve("unzip");
                Files.createDirectories(unzipDir);
                
                try {
                    FileUtil.decompressZip(zipFile.toString(), unzipDir.toString());

                    // 验证解压成功
                    Path unzippedFile = unzipDir.resolve("source.txt");
                    assertTrue(Files.exists(unzippedFile));
                    assertEquals(content, FileUtil.readFile(unzippedFile.toString()));
                } finally {
                    // 清理解压目录
                    deleteRecursively(unzipDir);
                }
            } finally {
                // 清理zip文件
                Files.deleteIfExists(zipFile);
            }
        } finally {
            // 清理源文件
            Files.deleteIfExists(sourceFile);
        }
    }

    @Test
    public void testCompressAndDecompressGzip() throws IOException {
        // 创建测试文件
        String content = "Hello, Gzip!";
        Path sourceFile = tempDir.resolve("source.txt");
        Files.write(sourceFile, content.getBytes());

        try {
            // 压缩为gzip
            Path gzipFile = tempDir.resolve("test.gz");
            FileUtil.compressToGzip(sourceFile.toString(), gzipFile.toString());

            try {
                // 验证gzip文件创建成功
                assertTrue(Files.exists(gzipFile));

                // 解压gzip文件
                Path unzippedFile = tempDir.resolve("unzipped.txt");
                FileUtil.decompressGzip(gzipFile.toString(), unzippedFile.toString());
                
                try {
                    // 验证解压成功
                    assertTrue(Files.exists(unzippedFile));
                    assertEquals(content, FileUtil.readFile(unzippedFile.toString()));
                } finally {
                    // 清理解压文件
                    Files.deleteIfExists(unzippedFile);
                }
            } finally {
                // 清理gzip文件
                Files.deleteIfExists(gzipFile);
            }
        } finally {
            // 清理源文件
            Files.deleteIfExists(sourceFile);
        }
    }

    @Test
    public void testCopyDirectory() throws IOException {
        // 创建源目录结构
        Path sourceDir = tempDir.resolve("sourceDir");
        Files.createDirectories(sourceDir);

        try {
            // 在源目录中创建文件
            String content1 = "File 1 content";
            String content2 = "File 2 content";
            Path file1 = sourceDir.resolve("file1.txt");
            Path file2 = sourceDir.resolve("file2.txt");
            Files.write(file1, content1.getBytes());
            Files.write(file2, content2.getBytes());

            // 创建子目录
            Path subDir = sourceDir.resolve("subDir");
            Files.createDirectories(subDir);
            String content3 = "Subdir file content";
            Path file3 = subDir.resolve("file3.txt");
            Files.write(file3, content3.getBytes());

            // 拷贝目录
            Path targetDir = tempDir.resolve("targetDir");
            
            try {
                FileUtil.copyDirectory(sourceDir.toString(), targetDir.toString());

                // 验证目录拷贝成功
                assertTrue(Files.exists(targetDir));
                assertTrue(Files.exists(targetDir.resolve("file1.txt")));
                assertTrue(Files.exists(targetDir.resolve("file2.txt")));
                assertTrue(Files.exists(targetDir.resolve("subDir")));
                assertTrue(Files.exists(targetDir.resolve("subDir").resolve("file3.txt")));

                assertEquals(content1, FileUtil.readFile(targetDir.resolve("file1.txt").toString()));
                assertEquals(content2, FileUtil.readFile(targetDir.resolve("file2.txt").toString()));
                assertEquals(content3, FileUtil.readFile(targetDir.resolve("subDir").resolve("file3.txt").toString()));
            } finally {
                // 清理目标目录
                deleteRecursively(targetDir);
            }
        } finally {
            // 清理源目录
            deleteRecursively(sourceDir);
        }
    }
    
    /**
     * 递归删除目录及其内容
     * 
     * @param path 要删除的路径
     * @throws IOException IO异常
     */
    private void deleteRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                File[] files = path.toFile().listFiles();
                if (files != null) {
                    for (File file : files) {
                        deleteRecursively(file.toPath());
                    }
                }
            }
            Files.deleteIfExists(path);
        }
    }
}