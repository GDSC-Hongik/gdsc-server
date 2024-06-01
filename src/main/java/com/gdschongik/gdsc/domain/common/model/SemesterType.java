package com.gdschongik.gdsc.domain.common.model;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.time.Month;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SemesterType {
    FIRST("1학기"),
    SECOND("2학기");

    private final String value;

    public static SemesterType from(LocalDateTime dateTime) {
        return getSemesterType(dateTime);
    }

    private static SemesterType getSemesterType(LocalDateTime dateTime) {
        int year = dateTime.getYear();
        LocalDateTime firstSemesterStartDate = LocalDateTime.of(year, 3, 1, 0, 0, 0);
        LocalDateTime secondSemesterStartDate = LocalDateTime.of(year, 9, 1, 0, 0, 0);

        if (dateTime.isAfter(firstSemesterStartDate.minusWeeks(2)) && dateTime.getMonthValue() < 7) {
            return FIRST;
        }

        if (dateTime.isAfter(secondSemesterStartDate.minusWeeks(2))) {
            return SECOND;
        }
        throw new CustomException(ErrorCode.SEMESTER_TYPE_INVALID_FOR_DATE);
    }

    public static Month getStartMonth(SemesterType semesterType) {
        if (semesterType == FIRST) {
            return Month.MARCH;
        }
        return Month.SEPTEMBER;
    }
}
