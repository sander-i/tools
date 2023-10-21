package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tools.bedrock.annotations.Inherit;

import java.util.Date;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:59
 * @description 数据实体基础对象
 * @scope all
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Inherit
public class BaseBean {

    private Long id;
    /**
     * 父id
     * parent
     */
    private Long pid;
    /**
     * 业务id
     * business
     */
    private String bid;
    /**
     * 雪花id
     * snowflake
     */
    private String sfid;
    private String uuid;
    private Long sort;
    private Byte state;
    private String remark;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", bid='" + bid + '\'' +
                ", sfid='" + sfid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", sort='" + sort + '\'' +
                ", state=" + state +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime;
    }

}
