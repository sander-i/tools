package org.tools.bedrock.base;

import lombok.*;

import java.time.LocalDate;

/**
 * 公民身份实体：承载居民身份证各项信息。
 *
 * <p>继承 {@link BaseBean} 的主键与审计字段；JDK 序列化场景使用
 * {@link org.tools.bedrock.base.serializable.Identity}。</p>
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
     * 出生日期
     */
    private LocalDate birth;

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
