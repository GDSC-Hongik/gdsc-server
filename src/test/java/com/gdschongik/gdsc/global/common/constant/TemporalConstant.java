package com.gdschongik.gdsc.global.common.constant;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import java.time.LocalDateTime;

public class TemporalConstant {
    // academic year
    public static final Integer ACADEMIC_YEAR = 2024;

    // semester
    public static final SemesterType SEMESTER_TYPE = SemesterType.FIRST;
    public static final LocalDateTime SEMESTER_START_DATE = LocalDateTime.of(2024, 3, 2, 0, 0);
    public static final LocalDateTime SEMESTER_END_DATE = LocalDateTime.of(2024, 8, 31, 0, 0);

    private TemporalConstant() {}
}
