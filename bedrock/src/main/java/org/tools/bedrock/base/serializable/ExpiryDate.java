package org.tools.bedrock.base.serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 生效期限实体（JDK 序列化变体）：开始/结束日期构成的区间，
 * 字段结构与 {@link org.tools.bedrock.base.ExpiryDate} 保持一致。
 *
 * @author sander
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpiryDate implements Serializable {

    private static final long serialVersionUID = 7502769832110568563L;

    /**
     * 开始日期
     */
    private Date beginDate;

    /**
     * 结束日期
     */
    private Date endDate;

}
