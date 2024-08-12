package com.gdschongik.gdsc.global.util.formatter;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyFormatter {

    public static String getPeriod(Period period) {
        return period.getStartDate().format(DateTimeFormatter.ofPattern("MM.dd")) + "-"
                + period.getEndDate().format(DateTimeFormatter.ofPattern("MM.dd"));
    }

    public static String getSchedule(DayOfWeek week, LocalTime startTime, LocalTime endTime) {
        return getKoreanDayOfWeek(week) + " " + startTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " - "
                + endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String getHalfSchedule(DayOfWeek dayOfWeek, LocalTime startTime) {
        return getKoreanDayOfWeek(dayOfWeek) + startTime.format(DateTimeFormatter.ofPattern("HH")) + "시";
    }

    public static String getKoreanDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
            default -> "";
        };
    }
}
