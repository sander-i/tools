package org.tools.bedrock.base.serializable;

import lombok.*;
import lombok.experimental.Accessors;
import org.tools.bedrock.base.Nation;
import org.tools.bedrock.base.Sex;

import java.util.Date;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 1:01
 * @description 身份证
 * @scope all
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Identity extends BaseBean {
    private static final long serialVersionUID = 3510047205897590711L;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private Sex sex;
    /**
     * 民族
     */
    private Nation nation;
    /**
     * 出生
     */
    private Date birth;
    /**
     * 住址
     */
    private Address address;
    /**
     * 公民身份号码
     */
    private String idNo;
    /**
     * 签发机关
     */
    private String issuingAuthority;
    /**
     * 有效期限
     */
    private ExpiryDate expirationDate;

}
