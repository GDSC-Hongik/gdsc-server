package com.gdschongik.gdsc.domain.common.vo;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

    private BigDecimal amount;

    @Builder(access = AccessLevel.PRIVATE)
    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money from(BigDecimal amount) {
        validateAmountNotNull(amount);

        return Money.builder().amount(amount).build();
    }

    private static void validateAmountNotNull(BigDecimal amount) {
        if (amount == null) {
            throw new CustomException(MONEY_AMOUNT_NOT_NULL);
        }
    }

    // 모든 로직은 BigDecimal에서 제공하는 메서드를 그대로 사용하여 구현

    // 금액 사칙연산 로직

    public Money add(@NonNull Money target) {
        return Money.from(this.amount.add(target.amount));
    }

    public Money subtract(@NonNull Money target) {
        return Money.from(this.amount.subtract(target.amount));
    }

    public Money multiply(@NonNull BigDecimal target) {
        return Money.builder().amount(this.amount.multiply(target)).build();
    }

    public Money divide(@NonNull BigDecimal target) {
        return Money.builder()
                .amount(this.amount.divide(target, RoundingMode.HALF_UP))
                .build();
    }

    // 금액 비교 로직

    public boolean isGreaterThan(@NonNull Money target) {
        return this.amount.compareTo(target.amount) > 0;
    }

    public boolean isGreaterThanOrEqual(@NonNull Money target) {
        return this.amount.compareTo(target.amount) >= 0;
    }

    public boolean isLessThan(@NonNull Money target) {
        return this.amount.compareTo(target.amount) < 0;
    }

    public boolean isLessThanOrEqual(@NonNull Money target) {
        return this.amount.compareTo(target.amount) <= 0;
    }
}
