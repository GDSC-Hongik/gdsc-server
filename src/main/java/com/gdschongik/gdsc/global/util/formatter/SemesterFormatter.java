package com.gdschongik.gdsc.global.util.formatter;

import com.gdschongik.gdsc.domain.common.model.BaseSemesterEntity;
import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SemesterFormatter {
    public static String format(Semester semester) {
        return format(semester.getAcademicYear(), semester.getSemesterType());
    }

    public static String format(Integer academicYear, SemesterType semesterType) {
        return academicYear + "-" + semesterType.getValue();
    }

    public static String formatType(BaseSemesterEntity semesterEntity) {
        return semesterEntity.getSemesterType().getValue() + "학기";
    }
}
