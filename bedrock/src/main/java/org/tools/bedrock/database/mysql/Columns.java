package org.tools.bedrock.database.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Columns {

    /**
     * 表目录
     */
    private String tableCatalog;
    /**
     * 表
     */
    private String tableSchema;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 序号位置
     */
    private String ordinalPosition;
    /**
     * 默认值
     */
    private String columnDefault;
    /**
     * 允许为空
     */
    private String isNullable;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 字符最大长度
     */
    private String characterMaximumLength;
    /**
     * 字符八位字节长度
     */
    private String characterOctetLength;
    /**
     * 数字精度
     */
    private String numericPrecision;
    /**
     * 数字刻度
     */
    private String numericScale;
    /**
     * 日期时间精度
     */
    private String datetimePrecision;
    /**
     * 字符集名称
     */
    private String characterSetName;
    /**
     * 排序规则名称
     */
    private String collationName;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 列键
     */
    private String columnKey;
    /**
     * 扩展
     */
    private String extra;
    /**
     * 权限
     */
    private String privileges;
    /**
     * 列注释
     */
    private String columnComment;
    /**
     * 生成表达式
     */
    private String generationExpression;
    /**
     * id
     */
    private String srsId;
}
