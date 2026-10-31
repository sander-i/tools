package org.tools.fortify.util.file;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tools.bedrock.base.ExcelSheet;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Excel 导出工具：按 ExcelSheet 模型生成工作簿、文件或字节数组。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.3
 */
@Slf4j
public class ExcelUtils {
    private ExcelUtils() {
    }

    /**
     * 将Excel数据写入文件
     *
     * @param path        文件路径
     * @param excelSheets Excel工作表数据
     */
    public static void toFile(String path, ExcelSheet... excelSheets) {
        // 参数验证
        if (path == null || path.trim().isEmpty()) {
            log.warn("file path is empty or null, cannot write file");
            return;
        }

        if (excelSheets == null) {
            log.warn("excel sheet data is null, cannot write file");
            return;
        }

        // 确保父目录存在
        try {
            java.io.File file = new java.io.File(path);
            java.io.File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    log.warn("cannot create parent directory: {}", parentDir.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.warn("exception while checking or creating directory: {}", path, e);
        }

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            fullWorkbook(workbook, excelSheets);
            workbook.write(outputStream);
            outputStream.flush();
            log.debug("successfully wrote excel data to file: {}", path);
        } catch (java.io.FileNotFoundException e) {
            log.error("file path invalid or no permission: {}", path, e);
        } catch (IOException e) {
            log.error("failed to write excel file, path: {}", path, e);
        } catch (Exception e) {
            log.error("unknown error while processing excel data, path: {}", path, e);
        }
    }

    /**
     * 将Excel数据转换为字节数组
     *
     * @param excelSheets Excel工作表数据
     * @return Excel文件的字节数组
     */
    public static byte[] toByteArray(ExcelSheet... excelSheets) {
        // 参数验证
        if (excelSheets == null) {
            log.warn("excel sheet data is null, returning empty byte array");
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            fullWorkbook(workbook, excelSheets);
            workbook.write(outputStream);
            log.debug("successfully converted excel data to byte array, size: {} bytes", outputStream.size());
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("failed to convert excel data to byte array", e);
        } catch (Exception e) {
            log.error("unknown error while processing excel data", e);
        }
        return new byte[0];
    }

    /**
     * 将多个 Sheet 数据依次填充进工作簿
     *
     * @param workbook    目标工作簿
     * @param excelSheets Excel 工作表数据
     */
    public static void fullWorkbook(Workbook workbook, ExcelSheet... excelSheets) {
        for (ExcelSheet excelSheet : excelSheets) {
            Sheet sheet = workbook.createSheet(excelSheet.getName());
            List<List<String>> fillers = Stream.of(excelSheet.getHeaders(), excelSheet.getContents())
                    .reduce(new ArrayList<>(), (a, b) -> {
                        a.addAll(b);
                        return a;
                    });
            fullSheet(sheet, fillers);
        }
    }

    /**
     * 将二维数据填充进 Sheet
     *
     * @param sheet   目标工作表
     * @param fillers 二维数据，每个元素为一行数据
     */
    public static void fullSheet(Sheet sheet, List<List<String>> fillers) {
        for (int i = 0; i < fillers.size(); i++) {
            fullRow(sheet.createRow(i), fillers.get(i));
        }
    }

    /**
     * 将一行数据填充进 Row
     *
     * @param row    目标行
     * @param filler 行数据
     */
    public static void fullRow(Row row, List<String> filler) {
        for (int j = 0; j < filler.size(); j++) {
            row.createCell(j).setCellValue(filler.get(j));
        }
    }
}
