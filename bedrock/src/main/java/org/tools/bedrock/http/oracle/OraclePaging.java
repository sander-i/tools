package org.tools.bedrock.http.oracle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.bedrock.http.Paging;

/**
 * Oracle 分页（ROWNUM 区间 [startRecord+1, endRecord]）：分页计算由 {@link Paging#initPaging} 提供。
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OraclePaging<T> extends Paging<T> {
    private static final long serialVersionUID = 1L;

    /**
     * Oracle ROWNUM 区间的结束行号（startRecord + pageSize，不超过总记录数）。
     *
     * @return ROWNUM 结束行号
     */
    public Integer getEndRecord() {
        Integer totalCount = getTotalCount();
        Integer endRecord = (getStartRecord() + getPageSize());
        endRecord = endRecord.compareTo(totalCount) > 0 ? totalCount : endRecord;
        return endRecord;
    }

}
