package org.tools.bedrock.base.serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tools.bedrock.annotations.Inherit;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 通用实体基类（JDK 序列化变体）。
 *
 * <p>字段结构与 {@link org.tools.bedrock.base.BaseBean} 保持一致，
 * 差异仅在于实现 {@link Serializable}，供 RPC、缓存等 JDK 序列化场景使用。
 * 审计时间采用 {@link Timestamp}：承载的是与时区无关的时间戳 instant，
 * 存储/传输无需关心时区转换，且与 JDBC TIMESTAMP 类型原生互转。</p>
 *
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Inherit
public class BaseBean implements Serializable {

    private static final long serialVersionUID = 5233315758488710799L;

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
