package org.tools.bedrock.database.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/15 0:27
 * @description 架构
 * @scope mysql
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Schemata {

    /**
     * 目录名称
     */
    private String catalogName;
    /**
     * 架构名称
     */
    private String schemaName;
    /**
     * 默认字符集名称
     */
    private String defaultCharacterSetName;
    /**
     * 默认排序规则名称
     */
    private String defaultCollationName;
    /**
     * sql路径
     */
    private String sqlPath;
    /**
     * 默认加密
     */
    private String defaultEncryption;

    /**
     * 表
     */
    private List<Tables> tables;

}
