package com.gdschongik.gdsc.domain.common.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SemesterType {
    FIRST("1학기"),
    SECOND("2학기");

    private final String value;

    public static SemesterType from(LocalDateTime dateTime) {
        if (dateTime.getMonthValue() < 7) {
            return FIRST;
        }
        return SECOND;
    }
}
