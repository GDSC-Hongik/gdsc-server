package com.gdschongik.gdsc.global.util.formatter;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneFormatter {
    public static String format(@Nullable String phone) {
        return phone == null
                ? null
                : new StringBuilder(12)
                        .append(phone, 0, 3)
                        .append('-')
                        .append(phone, 3, 7)
                        .append('-')
                        .append(phone, 7, 11)
                        .toString();
    }
}
