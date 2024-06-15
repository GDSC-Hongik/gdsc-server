package com.gdschongik.gdsc.domain.coupon.application;

import static com.gdschongik.gdsc.global.common.constant.CouponConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CouponServiceTest extends IntegrationTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Nested
    class 쿠폰_생성할때 {

        @Test
        void 성공한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);

            // when
            couponService.createCoupon(request);

            // then
            assertThat(couponRepository.findById(1L)).isPresent();
        }

        @Test
        void 할인금액이_양수가_아니라면_실패한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ZERO);

            // when & then
            assertThatThrownBy(() -> couponService.createCoupon(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_DISCOUNT_AMOUNT_NOT_POSITIVE.getMessage());
        }
    }
}
