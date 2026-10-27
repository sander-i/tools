package org.tools.bedrock.util.date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 日期区间对象：区间起止与区间类型封装。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {
    Date startDate;
    Date endDate;

    LocalDateTime startLocalDateTime;
    LocalDateTime endLocalDateTime;

    LocalDate startLocalDate;
    LocalDate endLocalDate;

    LocalTime startLocalTime;
    LocalTime endLocalTime;

    Timestamp startTimestamp;
    Timestamp endTimestamp;
}
