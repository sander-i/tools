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
 * @author sander
 * @version 1.0
 * @date 2024/03/03 17:03
 * @description 描述
 * @scope 作用域
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
