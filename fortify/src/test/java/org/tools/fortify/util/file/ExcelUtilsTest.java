package org.tools.fortify.util.file;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.tools.bedrock.base.ExcelSheet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ExcelUtils 冒烟测试
 * 测试基本的Excel处理功能
 */
public class ExcelUtilsTest {

    private static final String TEST_FILE_PATH = "test_excel.xlsx";

    // 创建测试用的ExcelSheet数据
    private ExcelSheet createTestExcelSheet() {
        List<String> headers = Arrays.asList("姓名", "年龄", "城市");
        List<List<String>> contents = new ArrayList<>();
        contents.add(Arrays.asList("张三", "25", "北京"));
        contents.add(Arrays.asList("李四", "30", "上海"));
        contents.add(Arrays.asList("王五", "28", "广州"));

        ExcelSheet excelSheet = new ExcelSheet();
        excelSheet.setName("测试表格");
        excelSheet.setHeaders(Collections.singletonList(headers));
        excelSheet.setContents(contents);
        return excelSheet;
    }

    @Test
    public void testToFile() {
        // 测试将Excel数据写入文件功能
        ExcelSheet excelSheet = createTestExcelSheet();

        // 执行写入文件操作
        ExcelUtils.toFile(TEST_FILE_PATH, excelSheet);

        // 验证文件是否创建成功
        File file = new File(TEST_FILE_PATH);
        assertTrue("Excel文件应该被创建", file.exists());

        // 验证文件内容是否正确
        try {
            Workbook workbook = WorkbookFactory.create(file);
            assertEquals("工作簿应该包含1个工作表", 1, workbook.getNumberOfSheets());
            assertEquals("工作表名称应该正确", "测试表格", workbook.getSheetName(0));
            assertNotNull("工作表不应该为null", workbook.getSheetAt(0));
            workbook.close();
        } catch (IOException e) {
            fail("读取Excel文件时发生异常: " + e.getMessage());
        }

        // 清理测试文件
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testToFileWithMultipleSheets() {
        // 测试将多个Excel工作表写入文件功能
        ExcelSheet sheet1 = createTestExcelSheet();

        List<String> headers2 = Arrays.asList("产品", "价格", "库存");
        List<List<String>> contents2 = new ArrayList<>();
        contents2.add(Arrays.asList("苹果", "5.0", "100"));
        contents2.add(Arrays.asList("香蕉", "3.0", "200"));

        ExcelSheet sheet2 = new ExcelSheet();
        sheet2.setName("产品信息");
        sheet2.setHeaders(Collections.singletonList(headers2));
        sheet2.setContents(contents2);

        // 执行写入文件操作
        ExcelUtils.toFile(TEST_FILE_PATH, sheet1, sheet2);

        // 验证文件是否创建成功
        File file = new File(TEST_FILE_PATH);
        assertTrue("Excel文件应该被创建", file.exists());

        // 验证文件内容是否正确
        try {
            Workbook workbook = WorkbookFactory.create(file);
            assertEquals("工作簿应该包含2个工作表", 2, workbook.getNumberOfSheets());
            assertEquals("第一个工作表名称应该正确", "测试表格", workbook.getSheetName(0));
            assertEquals("第二个工作表名称应该正确", "产品信息", workbook.getSheetName(1));
            workbook.close();
        } catch (IOException e) {
            fail("读取Excel文件时发生异常: " + e.getMessage());
        }

        // 清理测试文件
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testToFileWithEmptyPath() {
        // 测试空路径写入文件功能
        ExcelSheet excelSheet = createTestExcelSheet();

        // 执行写入文件操作，应该处理异常但不抛出
        ExcelUtils.toFile("", excelSheet);
        // 不应抛出异常
    }

    @Test
    public void testToFileWithNullPath() {
        // 测试null路径写入文件功能
        ExcelSheet excelSheet = createTestExcelSheet();

        // 执行写入文件操作，应该处理异常但不抛出
        ExcelUtils.toFile(null, excelSheet);
        // 不应抛出异常
    }

    @Test
    public void testToByteArray() {
        // 测试将Excel数据转换为字节数组功能
        ExcelSheet excelSheet = createTestExcelSheet();

        // 执行转换操作
        byte[] bytes = ExcelUtils.toByteArray(excelSheet);

        // 验证返回结果
        assertNotNull("返回的字节数组不应该为null", bytes);
        assertTrue("返回的字节数组长度应该大于0", bytes.length > 0);

        // 验证字节数组是否能正确解析为Excel文件
        try {
            Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));
            assertEquals("工作簿应该包含1个工作表", 1, workbook.getNumberOfSheets());
            assertEquals("工作表名称应该正确", "测试表格", workbook.getSheetName(0));
            workbook.close();
        } catch (IOException e) {
            fail("解析字节数组为Excel文件时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testToByteArrayWithMultipleSheets() {
        // 测试将多个Excel工作表转换为字节数组功能
        ExcelSheet sheet1 = createTestExcelSheet();

        List<String> headers2 = Arrays.asList("部门", "人数", "负责人");
        List<List<String>> contents2 = new ArrayList<>();
        contents2.add(Arrays.asList("研发部", "20", "张经理"));
        contents2.add(Arrays.asList("市场部", "15", "李经理"));

        ExcelSheet sheet2 = new ExcelSheet();
        sheet2.setName("组织架构");
        sheet2.setHeaders(Collections.singletonList(headers2));
        sheet2.setContents(contents2);

        // 执行转换操作
        byte[] bytes = ExcelUtils.toByteArray(sheet1, sheet2);

        // 验证返回结果
        assertNotNull("返回的字节数组不应该为null", bytes);
        assertTrue("返回的字节数组长度应该大于0", bytes.length > 0);

        // 验证字节数组是否能正确解析为Excel文件
        try {
            Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));
            assertEquals("工作簿应该包含2个工作表", 2, workbook.getNumberOfSheets());
            assertEquals("第一个工作表名称应该正确", "测试表格", workbook.getSheetName(0));
            assertEquals("第二个工作表名称应该正确", "组织架构", workbook.getSheetName(1));
            workbook.close();
        } catch (IOException e) {
            fail("解析字节数组为Excel文件时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testToByteArrayWithNoSheets() {
        // 测试无工作表转换为字节数组功能
        // 执行转换操作
        byte[] bytes = ExcelUtils.toByteArray();

        // 验证返回结果
        assertNotNull("返回的字节数组不应该为null", bytes);
        assertTrue("返回的字节数组长度应该大于0", bytes.length > 0);

        // 验证字节数组是否能正确解析为Excel文件
        try {
            Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes));
            assertEquals("工作簿应该包含0个工作表", 0, workbook.getNumberOfSheets());
            workbook.close();
        } catch (IOException e) {
            fail("解析字节数组为Excel文件时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testFullWorkbook() {
        // 测试填充工作簿功能
        try (Workbook workbook = new XSSFWorkbook()) {
            ExcelSheet excelSheet = createTestExcelSheet();

            // 执行填充工作簿操作
            ExcelUtils.fullWorkbook(workbook, excelSheet);

            // 验证工作簿内容
            assertEquals("工作簿应该包含1个工作表", 1, workbook.getNumberOfSheets());
            assertNotNull("工作表不应该为null", workbook.getSheetAt(0));
        } catch (IOException e) {
            fail("操作工作簿时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testFullSheet() {
        // 测试填充工作表功能
        try (Workbook workbook = new XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("测试表");

            List<String> headers = Arrays.asList("列1", "列2", "列3");
            List<String> row1 = Arrays.asList("值1", "值2", "值3");
            List<String> row2 = Arrays.asList("值4", "值5", "值6");
            List<List<String>> fillers = Arrays.asList(headers, row1, row2);

            // 执行填充工作表操作
            ExcelUtils.fullSheet(sheet, fillers);

            // 验证工作表内容
            assertEquals("工作表应该包含3行", 3, sheet.getLastRowNum() + 1);
            assertNotNull("第一行不应该为null", sheet.getRow(0));
            assertNotNull("第二行不应该为null", sheet.getRow(1));
            assertNotNull("第三行不应该为null", sheet.getRow(2));
        } catch (IOException e) {
            fail("操作工作簿时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testFullRow() {
        // 测试填充行功能
        try (Workbook workbook = new XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("测试表");
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(0);

            List<String> filler = Arrays.asList("单元格1", "单元格2", "单元格3");

            // 执行填充行操作
            ExcelUtils.fullRow(row, filler);

            // 验证行内容
            assertEquals("行应该包含3个单元格", 3, row.getLastCellNum());
            assertNotNull("第一个单元格不应该为null", row.getCell(0));
            assertNotNull("第二个单元格不应该为null", row.getCell(1));
            assertNotNull("第三个单元格不应该为null", row.getCell(2));
            assertEquals("第一个单元格值应该正确", "单元格1", row.getCell(0).getStringCellValue());
            assertEquals("第二个单元格值应该正确", "单元格2", row.getCell(1).getStringCellValue());
            assertEquals("第三个单元格值应该正确", "单元格3", row.getCell(2).getStringCellValue());
        } catch (IOException e) {
            fail("操作工作簿时发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testRoundTripConversion() {
        // 测试往返转换：ExcelSheet->字节数组->文件->ExcelSheet
        ExcelSheet originalSheet = createTestExcelSheet();

        // ExcelSheet转换为字节数组
        byte[] bytes = ExcelUtils.toByteArray(originalSheet);

        // 字节数组写入文件
        ExcelUtils.toFile(TEST_FILE_PATH, originalSheet);

        // 验证文件是否创建成功
        File file = new File(TEST_FILE_PATH);
        assertTrue("Excel文件应该被创建", file.exists());

        // 清理测试文件
        if (file.exists()) {
            file.delete();
        }

        // 验证字节数组有效性
        assertTrue("字节数组长度应该大于0", bytes.length > 0);
    }

    @Test
    public void testEdgeCases() {
        // 测试边界情况

        // 测试空的ExcelSheet
        ExcelSheet emptySheet = new ExcelSheet();
        emptySheet.setName("空表");
        emptySheet.setHeaders(new ArrayList<>());
        emptySheet.setContents(new ArrayList<>());
        byte[] bytes = ExcelUtils.toByteArray(emptySheet);
        assertNotNull("空表转换应该返回非null字节数组", bytes);

        // 测试只有表头的ExcelSheet
        List<String> headersOnly = Arrays.asList("标题1", "标题2");
        ExcelSheet headersOnlySheet = new ExcelSheet();
        headersOnlySheet.setName("只有表头");
        headersOnlySheet.setHeaders(Collections.singletonList(headersOnly));
        headersOnlySheet.setContents(new ArrayList<>());
        bytes = ExcelUtils.toByteArray(headersOnlySheet);
        assertNotNull("只有表头的表转换应该返回非null字节数组", bytes);

        // 测试null参数处理
        ExcelUtils.toFile(TEST_FILE_PATH, (ExcelSheet[]) null);
        bytes = ExcelUtils.toByteArray((ExcelSheet[]) null);
        assertNotNull("null参数处理应该返回非null字节数组", bytes);
    }
}
