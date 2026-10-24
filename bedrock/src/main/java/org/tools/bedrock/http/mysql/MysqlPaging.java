package org.tools.bedrock.http.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.tools.bedrock.http.Paging;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class MysqlPaging<T> extends Paging<T> {
    private static final long serialVersionUID = -6457640146548312421L;

    /** MySQL 分页结束记录行号 */
    public Integer getEndRecord() {
        Integer totalCount = getTotalCount();
        Integer endRecord = getPageSize();
        endRecord = endRecord.compareTo(totalCount) > 0 ? totalCount : endRecord;
        return endRecord;
    }

    /** 初始化 MySQL LIMIT 分页参数 */
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

}
