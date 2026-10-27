package org.tools.bedrock.base.serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通用地址实体（JDK 序列化变体）。
 *
 * <p>字段结构与 {@link org.tools.bedrock.base.Address} 保持一致，
 * 差异仅在于整体实现 {@link Serializable}。外层为国家-省-市三级行政区划，
 * 城市地址细分到区（{@link Area}），农村地址细分到县（{@link County}）。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address implements Serializable {
    private static final long serialVersionUID = 3283872528783904171L;

    /**
     * 国家
     */
    private String nation;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private Area area;

    /**
     * 县
     */
    private County county;

    /**
     * 备注
     */
    private String remark;

    /**
     * 城市区级地址：区-街道-门牌
     * @since 1.0.0
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class Area implements Serializable {
        private static final long serialVersionUID = 2082499026810332599L;

        /**
         * 区名
         */
        private String areaName;

        /**
         * 街道
         */
        private String street;

        /**
         * 房屋编号
         */
        private String houseNum;
    }

    /**
     * 县级地址：县-乡-村-组-门牌
     * @since 1.0.0
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class County implements Serializable {
        private static final long serialVersionUID = 4647403445576367083L;

        /**
         * 县名
         */
        private String countyName;

        /**
         * 乡
         */
        private String townShip;

        /**
         * 村
         */
        private String village;

        /**
         * 组
         */
        private String group;

        /**
         * 房屋编号
         */
        private String houseNum;
    }
}
