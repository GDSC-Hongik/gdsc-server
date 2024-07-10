package com.gdschongik.gdsc.global.util.formatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SemesterFormatter {
    public static String format(Integer academicYear, String semester) {
        return String.format("%d-%s", academicYear, semester);
    }
}
