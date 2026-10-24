package org.tools.bedrock.http.mysql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.bedrock.http.Paging;

/**
 * MySQL 分页（LIMIT 起始行, 每页大小）：分页计算由 {@link Paging#initPaging} 提供。
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MysqlPaging<T> extends Paging<T> {
    private static final long serialVersionUID = -6457640146548312421L;

    /**
     * MySQL LIMIT 子句的第二个参数（LIMIT startRecord, endRecord），
     * 取 min(pageSize, totalCount)：最后一页不足时按剩余记录数截断。
     *
     * @return LIMIT 子句的每页大小
     */
    public Integer getEndRecord() {
        Integer totalCount = getTotalCount();
        Integer endRecord = getPageSize();
        endRecord = endRecord.compareTo(totalCount) > 0 ? totalCount : endRecord;
        return endRecord;
    }

}
