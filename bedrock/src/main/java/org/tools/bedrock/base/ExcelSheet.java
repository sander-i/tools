package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 工作表数据模型：sheet 名 + 表头 + 内容行，单元格统一按字符串承载，
 * 供 ExcelUtils 导入导出使用。
 *
 * @author sander
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExcelSheet {

    /**
     * sheet 名称
     */
    private String name;

    /**
     * 表头（外层为行、内层为列，支持多行复合表头）
     */
    private List<List<String>> headers = new ArrayList<>();

    /**
     * 内容（外层为行、内层为列）
     */
    private List<List<String>> contents = new ArrayList<>();

}
