package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sander
 * @version 1.0
 * @date 2023/10/22 17:03
 * @description excel sheet页
 * @scope excel
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ExcelSheet {

    /**
     * sheet 名称
     */
    private String name;
    /**
     * 标头
     */
    private List<List<String>> headers = new ArrayList<>();
    /**
     * 内容
     */
    private List<List<String>> contents = new ArrayList<>();

}
