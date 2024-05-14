package com.gdschongik.gdsc.global.common.constant;

import java.time.LocalDateTime;

public class RecruitmentConstant {
    public static final String RECRUITMENT_NAME = "20xx학년도 1학기";
    public static final LocalDateTime START_DATE = LocalDateTime.of(2024, 3, 02, 00, 0);
    public static final LocalDateTime WRONG_END_DATE = LocalDateTime.of(2024, 3, 02, 00, 0);
    public static final LocalDateTime END_DATE = LocalDateTime.of(2024, 3, 11, 00, 00);
    public static final LocalDateTime NOW = LocalDateTime.now();

    private RecruitmentConstant() {}
}
