package org.tools.bedrock.http;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页基类：承载分页参数与结果，{@link #initPaging} 提供通用的分页计算（当前页越界裁剪、
 * 总页数、起始记录行号），数据库方言差异（如 {@link org.tools.bedrock.http.mysql.MysqlPaging} 的 LIMIT、
 * {@link org.tools.bedrock.http.oracle.OraclePaging} 的 ROWNUM 结束行号）由子类扩展。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.3
 */
@Data
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

    /**
     * 初始化分页参数：计算总页数、将当前页裁剪到 [1, 总页数]、计算起始记录行号。
     *
     * @param currentPage 当前页码（从 1 开始；超出总页数时按总页数处理）
     * @param pageSize    每页大小（必须大于 0）
     * @param totalCount  总记录数
     */
    public void initPaging(Integer currentPage, Integer pageSize, Integer totalCount) {
        if (pageSize == null || pageSize < 1) {
            throw new IllegalArgumentException("pageSize 必须大于 0");
        }
        setPageSize(pageSize);
        setTotalCount(totalCount);
        setTotalPage((totalCount + pageSize - 1) / pageSize);

        // 超出总页数置为总页数
        Integer totalPage = getTotalPage();
        currentPage = currentPage.compareTo(totalPage) > 0 ? totalPage : currentPage;
        currentPage = currentPage.compareTo(1) < 0 ? 1 : currentPage;
        setCurrentPage(currentPage);

        setStartRecord((currentPage - 1) * pageSize);
    }
}
