package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Test;

class MoneyInfoTest {

    @Test
    void 최종결제금액은_주문총액에서_쿠폰할인금액을_뺀_금액이다() {
        // given
        Money totalAmount = Money.from(10000L);
        Money discountAmount = Money.from(3000L);
        Money finalPaymentAmount = Money.from(7000L);

        // when
        MoneyInfo moneyInfo = MoneyInfo.create(totalAmount, discountAmount, finalPaymentAmount);

        // then
        Money expectedFinalPaymentAmount = totalAmount.subtract(discountAmount);
        assertThat(moneyInfo.getFinalPaymentAmount()).isEqualTo(expectedFinalPaymentAmount);
    }

    @Test
    void 최종결제금액이_주문총액에서_쿠폰할인금액을_뺀_금액과_다르면_실패한다() {
        // given
        Money totalAmount = Money.from(10000L);
        Money discountAmount = Money.from(3000L);
        Money finalPaymentAmount = Money.from(8000L);

        // when & then
        assertThatThrownBy(() -> MoneyInfo.create(totalAmount, discountAmount, finalPaymentAmount))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_FINAL_PAYMENT_AMOUNT_MISMATCH.getMessage());
    }

    @Test
    void 모든_금액이_같으면_같은_객체이다() {
        // given
        Money totalAmount1 = Money.from(10000L);
        Money discountAmount1 = Money.from(3000L);
        Money finalPaymentAmount1 = Money.from(7000L);

        Money totalAmount2 = Money.from(10000L);
        Money discountAmount2 = Money.from(3000L);
        Money finalPaymentAmount2 = Money.from(7000L);

        // when
        MoneyInfo moneyInfo1 = MoneyInfo.create(totalAmount1, discountAmount1, finalPaymentAmount1);
        MoneyInfo moneyInfo2 = MoneyInfo.create(totalAmount2, discountAmount2, finalPaymentAmount2);

        // then
        assertThat(moneyInfo1).isEqualTo(moneyInfo2);
    }
}
