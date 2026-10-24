package org.tools.bedrock.http.oracle;

import org.tools.bedrock.http.Paging;

/**
 * @author sander
 * @version 1.0
 */
public class OraclePaging<T> extends Paging<T> {

    /** Oracle 分页结束记录行号 */
    public Integer getEndRecord() {
        Integer totalCount = getTotalCount();
        Integer endRecord = (getStartRecord() + getPageSize());
        endRecord = endRecord.compareTo(totalCount) > 0 ? totalCount : endRecord;
        return endRecord;
    }

    /** 初始化 Oracle ROWNUM 分页参数 */
    @Override
    public void initPaging(Integer currentPage, Integer pageSize, Integer totalCount) {
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

    /** 演示 Oracle 分页参数计算的示例入口 */
    public static void main(String[] args) {
        OraclePaging oraclePaging = new OraclePaging();
        oraclePaging.initPaging(3, 33, 100);

        System.out.println(oraclePaging.getStartRecord());
        System.out.println(oraclePaging.getEndRecord());
        System.out.println("====");
        System.out.println(oraclePaging.getCurrentPage());
        System.out.println(oraclePaging.getPageSize());
        System.out.println(oraclePaging.getTotalPage());
        System.out.println(oraclePaging.getTotalCount());
    }

}
