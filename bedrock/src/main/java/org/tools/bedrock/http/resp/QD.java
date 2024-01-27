package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/22 0:32
 * @description query data
 * @scope response
 */
@Slf4j
@Data
@ToString
public class QD<T> {

    /**
     * 总计数 select count(id) from ${table_name} where 1 = 1 ···
     */
    private int totalCount;
    /**
     * 记录 page_row
     */
    private int records;
    /**
     * 页面大小 totalCount / records < totalCount
     */
    private int pageSize;
    /**
     * 页码 < pageSize
     */
    private int pageNo;
//    /**
//     * 开始记录
//     */
//    private int startRecords;
//    /**
//     * 结束记录
//     */
//    private int endRecords;

    private T data;

    public void init() {
        if (this.pageNo < 1) {
            log.debug("pageNo < 1 error correct: pageNo = 1");
            this.pageNo = 1;
        }
        if (this.totalCount < 1) {
            this.totalCount = 0;
        }
        if (this.records < 5) {
            log.debug("records < 5 error correct: pageNo = 5");
            this.records = 5;
        }
        this.pageSize = BigDecimal.valueOf(this.totalCount).divide(BigDecimal.valueOf(this.records), 0, RoundingMode.UP).intValue();
        if (this.pageNo > this.pageSize) {
            log.debug("pageNo > pageSize error correct: pageNo = pageSize");
            this.pageNo = this.pageSize;
        }
//        this.startRecords = (this.pageNo - 1) * this.records;
//        if (this.startRecords < 0) {
//            log.debug("startRecords < 0 error correct: startRecords = 0");
//            this.startRecords = 0;
//        }
//        if (this.startRecords >= this.records) {
//            log.debug("startRecords > records not error correct: startRecords++");
//            this.startRecords++;
//        }
//        if (this.startRecords > this.totalCount) {
//            log.debug("startRecords > totalCount error correct: startRecords = totalCount");
//            this.startRecords = this.totalCount;
//        }
//        this.endRecords = this.pageNo * this.records;
//        if (this.endRecords < this.startRecords) {
//            log.debug("endRecords < startRecords error correct: endRecords = startRecords + records");
//            this.endRecords = this.startRecords + this.records;
//        }
//        if (this.endRecords > this.totalCount) {
//            log.debug("endRecords > totalCount error correct: endRecords = totalCount");
//            this.endRecords = this.totalCount;
//        }
    }

}
