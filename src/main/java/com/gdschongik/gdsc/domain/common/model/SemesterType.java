package com.gdschongik.gdsc.domain.common.model;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.Month;
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

    public static SemesterType from(LocalDateTime dateTime) {
        return getSemesterType(dateTime);
    }

    private static SemesterType getSemesterType(LocalDateTime dateTime) {
        int year = dateTime.getYear();

        LocalDateTime firstSemesterStartDate =
                LocalDateTime.of(year, FIRST.startDate.getMonth(), FIRST.startDate.getDayOfMonth(), 0, 0);
        LocalDateTime secondSemesterStartDate =
                LocalDateTime.of(year, SECOND.startDate.getMonth(), SECOND.startDate.getDayOfMonth(), 0, 0);

        if (dateTime.isAfter(firstSemesterStartDate.minusWeeks(2))
                && dateTime.getMonthValue() < Month.JULY.getValue()) {
            return FIRST;
        }

        if (dateTime.isAfter(secondSemesterStartDate.minusWeeks(2))) {
            return SECOND;
        }
        throw new CustomException(ErrorCode.SEMESTER_TYPE_INVALID_FOR_DATE);
    }
}
