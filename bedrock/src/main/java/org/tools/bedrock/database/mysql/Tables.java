package org.tools.bedrock.database.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tables {

    /**
     * 表目录
     */
    private String tableCatalog;
    /**
     * 表架构
     */
    private String tableSchema;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表类型
     */
    private String tableType;
    /**
     * 引擎
     */
    private String engine;
    /**
     * 版本
     */
    private String version;
    /**
     * 行格式
     */
    private String rowFormat;
    /**
     * 表行数
     */
    private Integer tableRows;
    /**
     * 平均行长度
     */
    private Integer avgRowLength;
    /**
     * 数据长度
     */
    private Integer dataLength;
    /**
     * 最大数据长度
     */
    private Integer maxDataLength;
    /**
     * 索引长度
     */
    private Integer indexLength;
    /**
     * 数据空页
     */
    private Integer dataFree;
    /**
     * 自动增加
     */
    private String autoIncrement;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 检查时间
     */
    private Date checkTime;
    /**
     * 表排序规则
     */
    private String tableCollation;
    /**
     * 校验和
     */
    private String checksum;
    /**
     * 创建选项
     */
    private String createOptions;
    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 列
     */
    private List<Columns> columns;

}
