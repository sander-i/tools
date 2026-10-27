package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 通用实体基类。
 *
 * <p>沉淀数据表实体的公共字段：主键与业务标识（id/pid/bid/sfid/uuid）、
 * 排序与状态（sort/state）、备注与审计字段（creator/createdAt 等）。
 * 审计时间采用 {@link Timestamp}：承载的是与时区无关的时间戳 instant，
 * 存储/传输无需关心时区转换，且与 JDBC TIMESTAMP 类型原生互转。
 * 需要 JDK 序列化的场景使用 {@link org.tools.bedrock.base.serializable.BaseBean}。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseBean {
    /**
     * 主键 id
     */
    private Long id;

    /**
     * 父节点 id（树形结构场景）
     */
    private Long pid;

    /**
     * 业务 id
     */
    private String bid;

    /**
     * 雪花 id
     */
    private String sfid;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 排序号，小值在前
     */
    private Long sort;

    /**
     * 状态位，语义由业务定义
     */
    private Byte state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间（时间戳，与时区无关）
     */
    private Timestamp createdAt;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间（时间戳，与时区无关）
     */
    private Timestamp updatedAt;
}
