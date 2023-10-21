package org.tools.bedrock.base.serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:30
 * @description 有效期限
 * @scope all
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
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
