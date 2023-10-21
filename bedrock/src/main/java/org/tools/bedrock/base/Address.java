package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:29
 * @description 地址
 * @scope all
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Address {

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

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Accessors(chain = true)
    private static class Area {

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

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Accessors(chain = true)
    private static class County {

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
