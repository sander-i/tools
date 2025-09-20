package org.tools.bedrock.base;

import lombok.*;

import java.util.Date;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
//@Accessors(chain = true)
public class Identity extends BaseBean {

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
