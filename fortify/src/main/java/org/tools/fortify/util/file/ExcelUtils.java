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
 * @author sander
 * @version 1.0
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
            log.warn("文件路径为空或null，无法写入文件");
            return;
        }

        if (excelSheets == null) {
            log.warn("Excel工作表数据为null，无法写入文件");
            return;
        }

        // 确保父目录存在
        try {
            java.io.File file = new java.io.File(path);
            java.io.File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    log.warn("无法创建父目录: {}", parentDir.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.warn("检查或创建目录时发生异常: {}", path, e);
        }

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            fullWorkbook(workbook, excelSheets);
            workbook.write(outputStream);
            outputStream.flush();
            log.debug("成功将Excel数据写入文件: {}", path);
        } catch (java.io.FileNotFoundException e) {
            log.error("文件路径无效或无权限访问: {}", path, e);
        } catch (IOException e) {
            log.error("写入Excel文件失败，路径: {}", path, e);
        } catch (Exception e) {
            log.error("处理Excel数据时发生未知错误，路径: {}", path, e);
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
            log.warn("Excel工作表数据为null，返回空字节数组");
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            fullWorkbook(workbook, excelSheets);
            workbook.write(outputStream);
            log.debug("成功将Excel数据转换为字节数组，大小: {} 字节", outputStream.size());
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("转换Excel数据为字节数组失败", e);
        } catch (Exception e) {
            log.error("处理Excel数据时发生未知错误", e);
        }
        return new byte[0];
    }

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

    public static void fullSheet(Sheet sheet, List<List<String>> fillers) {
        for (int i = 0; i < fillers.size(); i++) {
            fullRow(sheet.createRow(i), fillers.get(i));
        }
    }

    public static void fullRow(Row row, List<String> filler) {
        for (int j = 0; j < filler.size(); j++) {
            row.createCell(j).setCellValue(filler.get(j));
        }
    }

}
