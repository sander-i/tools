package org.tools.bedrock.http;

import org.tools.bedrock.constant.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 说明：
 * 通过前端传来当前页码，使用默认的页面大小，通过数据操作层获取数据的总记录数
 * 这时我们得到的数据有 页码、页面大小、总记录数
 * 通过 总记录数 / 页面大小 得出 总页数
 * 通过 页面大小 * ( 当前页码 - 1 ) 得出 开始记录 这个开始记录指的是后面我们在数据操作层获取分页数据的开头
 * 通过 开始记录 + 页面大小 我们在数据操作层获取到分页后的数据并赋值给 结果列表
 * 最终返回给前端
 * </p>
 *
 * @author sander
 * @version 1.0
 * @date 2023/02/19 12:02
 * @description 分页工具
 * @scope http
 */
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 5760097915453738435L;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 开始记录
     */
    private Integer startRecord;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总记录数
     */
    private Integer totalCount;
    /**
     * 结果列表
     */
    private List<T> data;

    /**
     * 默认数据，前端未传当前页码
     */
    public PageBean() {
        this.setCurrentPage(1);
        this.setPageSize(Constants.DEFAULT_PAGE_SIZE);
    }

    /**
     * 前端传来当前页码
     */
    public PageBean(Integer currentPage) {
        this.setCurrentPage(currentPage);
        this.setPageSize(Constants.DEFAULT_PAGE_SIZE);
    }

    /**
     * 前端传来当前页码、控制器获取总记录数
     */
    public PageBean(Integer currentPage, Integer totalCount) {
        this.setCurrentPage(currentPage);
        this.setPageSize(Constants.DEFAULT_PAGE_SIZE);
        this.setTotalCount(totalCount);
    }

    /**
     * 开始记录
     */
    public Integer getStartRecord() {
        return startRecord;
    }

    /**
     * 每页大小
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 当前页码
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * 总页数
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 总记录数
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 数据集
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 当前页码
     * <p>代码优化
     * currentPage < 1 ? 1 : currentPage
     * </p>
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = Math.max(currentPage, Constants.ONE_I);
    }

    /**
     * 页面大小
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize < 0 ? pageSize : Constants.DEFAULT_PAGE_SIZE;
        /*
         * 通过 页面大小 * ( 当前页码 - 1 ) 得出 开始记录
         */
        this.setStartRecord(this.pageSize * (this.currentPage - Constants.ONE_I));
    }

    /**
     * 开始记录
     */
    public void setStartRecord(Integer startRecord) {
        this.startRecord = startRecord;
    }

    /**
     * 总页数
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 总记录数
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        // 总记录数
        Integer countRecords = this.getTotalCount();
        /*
         * 通过总记录数、每页大小计算得出总页数
         */
        Integer totalPages = countRecords % this.pageSize == Constants.ZERO_I ? countRecords / this.pageSize : (countRecords / this.pageSize + Constants.ONE_I);
        this.setTotalPage(totalPages);
    }

    /**
     * 设置结果
     *
     * @param data 分页之后查询到的结果
     *             <p> 优化三元运算符嵌套，提示代码可读性
     *             /*\
     *             * 通过总记录数 / 每页大小 得出总页数
     *             *\/
     *             this.setTotalPage(remainder == 0 ? (divisor == 0 ? 1 : divisor) : divisor + 1);
     *             if (remainder == 0) {
     *             // 余数为零页数小于1时表示当前所有数据不满足一页展示数量
     *             divisor = (Math.max(divisor, 1));
     *             } else {
     *             // 存在余数时表示当前页数不够展示所有数据
     *             divisor += 1;
     *             }
     *             this.setTotalPage(divisor);
     *             this.setTotalPage(remainder == 0 ? (Math.max(divisor, 1)) : divisor + 1);
     *             </p>
     */
    public void build(List<T> data) {
        this.setData(data);
        // 总记录数
        Integer count = this.getTotalCount();
        // 除数
        int divisor = count / this.getPageSize();
        // 余数
        int remainder = count % this.getPageSize();
        /*
         * 通过总记录数 / 每页大小 得出总页数
         */
        this.setTotalPage(remainder == Constants.ZERO_I ? (Math.max(divisor, Constants.ONE_I)) : divisor + Constants.ONE_I);
    }

    /**
     * 设置数据集
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageBean{ currentPage:" +
                currentPage + ", pageSize:" +
                pageSize + ", startRecord:" +
                startRecord + ", totalPage:" +
                totalPage + ", totalCount:" +
                totalCount + ", data:" +
                data + " }";
    }

}
