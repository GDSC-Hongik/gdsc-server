package com.gdschongik.gdsc.domain.common.model;

import java.time.MonthDay;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SemesterType {
    FIRST(1, MonthDay.of(3, 1)),
    SECOND(2, MonthDay.of(9, 1));

    private final Integer value;
    private final MonthDay startDate;
}
