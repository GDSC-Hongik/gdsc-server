package com.gdschongik.gdsc.domain.coupon.util;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.global.util.formatter.SemesterFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponNameUtil {

    public String generateStudyCompletionCouponName(StudyV2 study) {
        Semester semester = study.getSemester();
        String academicYearAndSemesterName =
                SemesterFormatter.format(semester.getAcademicYear(), semester.getSemesterType());
        return academicYearAndSemesterName + " " + study.getTitle() + " 수료 쿠폰";
    }
}
