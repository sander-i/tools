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

    public Integer getEndRecord() {
        Integer totalCount = getTotalCount();
        Integer endRecord = getPageSize();
        endRecord = endRecord.compareTo(totalCount) > 0 ? totalCount : endRecord;
        return endRecord;
    }

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

    public static void main(String[] args) {
        MysqlPaging mysqlPaging = new MysqlPaging();
        mysqlPaging.initPaging(3, 33, 100);

        System.out.println(mysqlPaging.getStartRecord());
        System.out.println(mysqlPaging.getEndRecord());
        System.out.println("====");
        System.out.println(mysqlPaging.getCurrentPage());
        System.out.println(mysqlPaging.getPageSize());
        System.out.println(mysqlPaging.getTotalPage());
        System.out.println(mysqlPaging.getTotalCount());
    }

}
