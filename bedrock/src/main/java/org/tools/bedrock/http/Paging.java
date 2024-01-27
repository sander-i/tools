package org.tools.bedrock.http;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author xietao
 * @version 1.0
 * @date 2023/12/23 16:16
 * @description 分页
 * @scope http
 */
@Data
@ToString
public abstract class Paging<T> implements Serializable {

    private static final long serialVersionUID = -1130734574933924097L;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 开始记录
     */
    private Integer startRecord;
    /**
     * 总记录数
     */
    private Integer totalCount;
    /**
     * 参数
     */
    private T param;
    /**
     * 结果列表
     */
    private List<T> data;

    public abstract void initPaging(Integer currentPage, Integer pageSize, Integer totalCount);

}
