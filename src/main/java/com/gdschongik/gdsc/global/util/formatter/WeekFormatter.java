package com.gdschongik.gdsc.global.util.formatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekFormatter {
    public static String format(Long week) {
        return week + "주차";
    }
}
