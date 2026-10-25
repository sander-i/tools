package org.tools.bedrock.base.serializable;

import lombok.*;

import org.tools.bedrock.base.Nation;
import org.tools.bedrock.base.Sex;

import java.time.LocalDate;

/**
 * 公民身份实体（JDK 序列化变体）：承载居民身份证各项信息。
 *
 * <p>字段结构与 {@link org.tools.bedrock.base.Identity} 保持一致，
 * 差异仅在于整体实现 {@link java.io.Serializable}；继承本包的
 * {@link BaseBean}（同为可序列化版本）。</p>
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
