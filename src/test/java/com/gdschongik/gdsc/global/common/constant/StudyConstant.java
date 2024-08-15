package com.gdschongik.gdsc.global.common.constant;

import com.gdschongik.gdsc.domain.study.domain.StudyType;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class StudyConstant {
    private StudyConstant() {}

    public static final Long TOTAL_WEEK = 8L;
    public static final Long CURRENT_WEEK = 1L;
    public static final StudyType ONLINE_STUDY = StudyType.ONLINE;
    public static final StudyType ASSIGNMENT_STUDY = StudyType.ASSIGNMENT;
    public static final DayOfWeek DAY_OF_WEEK = DayOfWeek.FRIDAY;
    public static final LocalTime STUDY_START_TIME = LocalTime.of(19, 0, 0);
    public static final LocalTime STUDY_END_TIME = LocalTime.of(20, 0, 0);

    // StudyDetail
    public static final String ATTENDANCE_NUMBER = "1234";

    // Assignment
    public static final String ASSIGNMENT_TITLE = "testTitle";
    public static final String DESCRIPTION_LINK = "www.link.com";
}
