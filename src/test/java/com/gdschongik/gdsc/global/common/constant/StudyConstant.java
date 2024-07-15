package com.gdschongik.gdsc.global.common.constant;

import com.gdschongik.gdsc.domain.study.domain.StudyType;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class StudyConstant {
    public static final Long TOTAL_WEEK = 8L;
    public static final StudyType STUDY_TYPE = StudyType.ONLINE;
    public static final DayOfWeek DAY_OF_WEEK = DayOfWeek.FRIDAY;
    public static final LocalTime TIME = LocalTime.of(19, 0, 0);
}
