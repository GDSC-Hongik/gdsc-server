package com.gdschongik.gdsc.domain.common.model;

import java.time.MonthDay;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SemesterType {
    FIRST("1학기", MonthDay.of(3, 1)),
    SECOND("2학기", MonthDay.of(9, 1));

    private final String value;
    private final MonthDay startDate;
}
