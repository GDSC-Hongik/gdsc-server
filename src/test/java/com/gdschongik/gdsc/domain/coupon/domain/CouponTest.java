package com.gdschongik.gdsc.domain.coupon.domain;

import static com.gdschongik.gdsc.global.common.constant.CouponConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Nested
    class 쿠폰_생성할때 {

        @Test
        void 성공한다() {
            // when
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));

            // then
            assertThat(coupon).isNotNull();
        }

        @Test
        void 할인금액이_양수가_아니라면_실패한다() {
            // given
            Money discountAmount = Money.from(ZERO);

            // when & then
            assertThatThrownBy(() -> Coupon.createCoupon(COUPON_NAME, discountAmount))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_DISCOUNT_AMOUNT_NOT_POSITIVE.getMessage());
        }
    }

    @Nested
    class 쿠폰_수정할때 {

        @Test
        void 성공한다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));

            // when
            coupon.updateCoupon(COUPON_NAME, Money.from(TEN));

            // then
            assertThat(coupon.getDiscountAmount()).isEqualTo(Money.from(TEN));
        }

        @Test
        void 할인금액이_양수가_아니라면_실패한다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Money changedDiscountAmount = Money.from(ZERO);

            // when & then
            assertThatThrownBy(() -> coupon.updateCoupon(COUPON_NAME, changedDiscountAmount))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_DISCOUNT_AMOUNT_NOT_POSITIVE.getMessage());
        }
    }
}
