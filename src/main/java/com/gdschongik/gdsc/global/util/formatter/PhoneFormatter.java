package com.gdschongik.gdsc.global.util.formatter;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneFormatter {
    public static String format(@Nullable String phone) {
        if (phone == null) return null;
        return phone.substring(0, 3) + '-' + phone.substring(3, 7) + '-' + phone.substring(7, 11);
    }
}
