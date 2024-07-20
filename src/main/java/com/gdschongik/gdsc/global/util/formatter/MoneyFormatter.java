package com.gdschongik.gdsc.global.util.formatter;

import com.gdschongik.gdsc.domain.common.vo.Money;
import java.text.NumberFormat;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyFormatter {
    public static String format(@NonNull Money money) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        String formattedAmount = formatter.format(money.getAmount());
        return formattedAmount + "원";
    }
}
