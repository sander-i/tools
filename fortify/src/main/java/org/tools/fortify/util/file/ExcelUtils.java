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

    public static void toFile(String path, ExcelSheet... excelSheets) {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream outputStream = new FileOutputStream(path)) {
            fullWorkbook(workbook, excelSheets);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static byte[] toByteArray(ExcelSheet... excelSheets) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            fullWorkbook(workbook, excelSheets);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
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
