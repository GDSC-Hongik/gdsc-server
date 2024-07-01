package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyInfoTest {

    @Test
    void 최종결제금액은_주문총액에서_쿠폰할인금액을_뺀_금액이다() {
        // given
        Money totalAmount = Money.from(BigDecimal.valueOf(10000));
        Money discountAmount = Money.from(BigDecimal.valueOf(3000));
        Money finalPaymentAmount = Money.from(BigDecimal.valueOf(7000));

        // when
        MoneyInfo moneyInfo = MoneyInfo.of(totalAmount, discountAmount, finalPaymentAmount);

        // then
        Money expectedFinalPaymentAmount = totalAmount.subtract(discountAmount);
        assertThat(moneyInfo.getFinalPaymentAmount()).isEqualTo(expectedFinalPaymentAmount);
    }

    @Test
    void 최종결제금액이_주문총액에서_쿠폰할인금액을_뺀_금액과_다르면_실패한다() {
        // given
        Money totalAmount = Money.from(BigDecimal.valueOf(10000));
        Money discountAmount = Money.from(BigDecimal.valueOf(3000));
        Money finalPaymentAmount = Money.from(BigDecimal.valueOf(8000));

        // when & then
        assertThatThrownBy(() -> MoneyInfo.of(totalAmount, discountAmount, finalPaymentAmount))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_FINAL_PAYMENT_AMOUNT_MISMATCH.getMessage());
    }

    @Test
    void 모든_금액이_같으면_같은_객체이다() {
        // given
        Money totalAmount1 = Money.from(BigDecimal.valueOf(10000));
        Money discountAmount1 = Money.from(BigDecimal.valueOf(3000));
        Money finalPaymentAmount1 = Money.from(BigDecimal.valueOf(7000));

        Money totalAmount2 = Money.from(BigDecimal.valueOf(10000));
        Money discountAmount2 = Money.from(BigDecimal.valueOf(3000));
        Money finalPaymentAmount2 = Money.from(BigDecimal.valueOf(7000));

        // when
        MoneyInfo moneyInfo1 = MoneyInfo.of(totalAmount1, discountAmount1, finalPaymentAmount1);
        MoneyInfo moneyInfo2 = MoneyInfo.of(totalAmount2, discountAmount2, finalPaymentAmount2);

        // then
        assertThat(moneyInfo1).isEqualTo(moneyInfo2);
    }
}
