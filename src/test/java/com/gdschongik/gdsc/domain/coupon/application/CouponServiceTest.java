package com.gdschongik.gdsc.domain.coupon.application;

import static com.gdschongik.gdsc.global.common.constant.CouponConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponIssueRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CouponServiceTest extends IntegrationTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    IssuedCouponRepository issuedCouponRepository;

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

    @Nested
    class 쿠폰_발급할때 {

        @Test
        void 성공한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            createMember();

            CouponIssueRequest issueRequest = new CouponIssueRequest(1L, List.of(1L, 2L));

            // when
            couponService.createIssuedCoupon(issueRequest);

            // then
            assertThat(issuedCouponRepository.findAll()).hasSize(2);
        }

        @Test
        void 존재하지_않는_유저이면_제외하고_성공한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            createMember();

            CouponIssueRequest issueRequest = new CouponIssueRequest(1L, List.of(1L, 2L, 3L));

            // when
            couponService.createIssuedCoupon(issueRequest);

            // then
            assertThat(issuedCouponRepository.findAll()).hasSize(2);
        }

        @Test
        void 존재하지_않는_쿠폰이면_실패한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            createMember();

            CouponIssueRequest issueRequest = new CouponIssueRequest(2L, List.of(1L, 2L));

            // when & then
            assertThatThrownBy(() -> couponService.createIssuedCoupon(issueRequest))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_FOUND.getMessage());
        }
    }

    @Nested
    class 쿠폰_회수할때 {

        @Test
        void 성공한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            CouponIssueRequest issueRequest = new CouponIssueRequest(1L, List.of(1L));
            couponService.createIssuedCoupon(issueRequest);

            // when
            couponService.revokeIssuedCoupon(1L);

            // then
            assertThat(issuedCouponRepository.findAll()).hasSize(1).first().satisfies(issuedCoupon -> assertThat(
                            issuedCoupon.hasRevoked())
                    .isTrue());
        }

        @Test
        void 존재하지_않는_발급쿠폰이면_실패한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            CouponIssueRequest issueRequest = new CouponIssueRequest(1L, List.of(1L));
            couponService.createIssuedCoupon(issueRequest);

            // when & then
            assertThatThrownBy(() -> couponService.revokeIssuedCoupon(2L))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(ISSUED_COUPON_NOT_FOUND.getMessage());
        }

        @Test
        void 이미_회수한_발급쿠폰이면_실패한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            CouponIssueRequest issueRequest = new CouponIssueRequest(1L, List.of(1L));
            couponService.createIssuedCoupon(issueRequest);

            issuedCouponRepository.findById(1L).ifPresent(coupon -> {
                coupon.revoke();
                issuedCouponRepository.save(coupon);
            });

            // when & then
            assertThatThrownBy(() -> couponService.revokeIssuedCoupon(1L))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_REVOKABLE_ALREADY_REVOKED.getMessage());
        }

        @Test
        void 이미_사용한_발급쿠폰이면_실패한다() {
            // given
            CouponCreateRequest request = new CouponCreateRequest(COUPON_NAME, ONE);
            couponService.createCoupon(request);

            createMember();
            CouponIssueRequest issueRequest = new CouponIssueRequest(1L, List.of(1L));
            couponService.createIssuedCoupon(issueRequest);

            issuedCouponRepository.findById(1L).ifPresent(coupon -> {
                coupon.use();
                issuedCouponRepository.save(coupon);
            });

            // when & then
            assertThatThrownBy(() -> couponService.revokeIssuedCoupon(1L))
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_REVOKABLE_ALREADY_USED.getMessage());
        }
    }
}
