package com.gdschongik.gdsc.global.util.formatter;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SemesterFormatter {
    public static String format(BaseSemesterEntity semesterEntity) {
        return semesterEntity.getAcademicYear() + "-"
                + semesterEntity.getSemesterType().getValue();
    }

    public static String formatType(BaseSemesterEntity semesterEntity) {
        return semesterEntity.getSemesterType().getValue() + "학기";
    }
}
