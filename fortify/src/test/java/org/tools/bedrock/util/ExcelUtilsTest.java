package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.ExcelSheet;
import org.tools.fortify.util.ExcelUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtilsTest {

    private final ExcelSheet excelSheet = new ExcelSheet();
    private final ExcelSheet excelSheet2 = new ExcelSheet();

    {
        List<List<String>> headers = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add("姓名");
        header.add("年龄");
        headers.add(header);

        List<List<String>> rows = new ArrayList<>();
        List<String> cell = new ArrayList<>();
        cell.add("张三");
        cell.add("6");
        rows.add(cell);

        List<String> cell2 = new ArrayList<>();
        cell2.add("张六");
        cell2.add("12");
        rows.add(cell2);

        List<List<String>> rows2 = new ArrayList<>();
        List<String> cell3 = new ArrayList<>();
        cell3.add("李四");
        cell3.add("8");
        rows2.add(cell3);

        List<String> cell4 = new ArrayList<>();
        cell4.add("李八");
        cell4.add("16");
        rows2.add(cell4);

        excelSheet.setName("法外狂徒").setContents(rows).setHeaders(headers);
        excelSheet2.setName("法内狂徒").setContents(rows2).setHeaders(headers);
    }

    @Test
    public void toByteArray() {
        try (FileOutputStream output = new FileOutputStream("example.xlsx")) {
            byte[] data = ExcelUtils.toByteArray(excelSheet, excelSheet2);
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toFile() {
        ExcelUtils.toFile("example.xlsx", excelSheet, excelSheet2);
    }
}