package com.gdschongik.gdsc.global.util.formatter;

import com.gdschongik.gdsc.domain.common.vo.Money;
import java.text.NumberFormat;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyFormatter {

    public static final NumberFormat KOREA_NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.KOREA);

    public static String format(@NonNull Money money) {
        return KOREA_NUMBER_FORMAT.format(money.getAmount()) + "원";
    }
}
