package com.gdschongik.gdsc.domain.coupon.domain;

import static com.gdschongik.gdsc.global.common.constant.CouponConstant.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IssuedCouponTest {

    @Nested
    class 발급쿠폰_사용할때 {

        @Test
        void 성공하면_사용여부는_true이다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Member member = Member.createGuestMember(OAUTH_ID);
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);

            // when
            issuedCoupon.use();

            // then
            assertThat(issuedCoupon.isUsed()).isTrue();
        }

        @Test
        void 이미_사용한_쿠폰이면_실패한다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Member member = Member.createGuestMember(OAUTH_ID);
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);
            issuedCoupon.use();

            // when & then
            assertThatThrownBy(issuedCoupon::use)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_USABLE_ALREADY_USED.getMessage());
        }

        @Test
        void 이미_회수한_쿠폰이면_실패한다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Member member = Member.createGuestMember(OAUTH_ID);
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);
            issuedCoupon.revoke();

            // when & then
            assertThatThrownBy(issuedCoupon::use)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_USABLE_REVOKED.getMessage());
        }
    }

    @Nested
    class 발급쿠폰_회수할때 {

        @Test
        void 성공하면_회수여부는_true이다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Member member = Member.createGuestMember(OAUTH_ID);
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);

            // when
            issuedCoupon.revoke();

            // then
            assertThat(issuedCoupon.isRevoked()).isTrue();
        }

        @Test
        void 이미_회수한_발급쿠폰이면_실패한다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Member member = Member.createGuestMember(OAUTH_ID);
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);
            issuedCoupon.revoke();

            // when & then
            assertThatThrownBy(issuedCoupon::revoke)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_REVOKABLE_ALREADY_REVOKED.getMessage());
        }

        @Test
        void 이미_사용한_발급쿠폰이면_실패한다() {
            // given
            Coupon coupon = Coupon.createCoupon(COUPON_NAME, Money.from(ONE));
            Member member = Member.createGuestMember(OAUTH_ID);
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);
            issuedCoupon.use();

            // when & then
            assertThatThrownBy(issuedCoupon::revoke)
                    .isInstanceOf(CustomException.class)
                    .hasMessageContaining(COUPON_NOT_REVOKABLE_ALREADY_USED.getMessage());
        }
    }
}
