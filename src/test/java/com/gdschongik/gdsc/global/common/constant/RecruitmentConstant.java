package com.gdschongik.gdsc.global.common.constant;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.recruitment.domain.Round;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecruitmentConstant {
    public static final String RECRUITMENT_NAME = "20xx학년도 1학기";
    public static final LocalDateTime START_DATE = LocalDateTime.of(2024, 3, 02, 00, 0);
    public static final LocalDateTime WRONG_END_DATE = LocalDateTime.of(2024, 3, 02, 00, 0);
    public static final LocalDateTime END_DATE = LocalDateTime.of(2024, 3, 11, 00, 00);
    public static final Integer ACADEMIC_YEAR = 2024;
    public static final SemesterType SEMESTER_TYPE = SemesterType.FIRST;
    public static final Money FEE = Money.from(BigDecimal.valueOf(20000));
    public static final Round ROUND = Round.FIRST;

    private RecruitmentConstant() {}
}
