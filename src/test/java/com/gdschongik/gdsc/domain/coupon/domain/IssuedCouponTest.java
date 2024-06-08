package com.gdschongik.gdsc.domain.coupon.domain;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.domain.Member;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class IssuedCouponTest {

    @Test
    void 발급쿠폰_사용하면_사용여부는_true이다() {
        // given
        Coupon coupon = Coupon.createCoupon("쿠폰이름", Money.from(BigDecimal.ONE));
        Member member = Member.createGuestMember(OAUTH_ID);
        IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, member);

        // when
        issuedCoupon.use();

        // then
        assertThat(issuedCoupon.isUsed()).isTrue();
    }
}
