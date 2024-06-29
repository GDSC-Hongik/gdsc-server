package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component // 추후 도메인 서비스로 교체
public class OrderValidator {

    public void validatePendingOrderCreate(
            Membership membership, @Nullable IssuedCoupon issuedCoupon, MoneyInfo moneyInfo, Member currentMember) {

        // 멤버 관련 검증

        // TODO: 어드민인 경우 리쿠르팅 지원 및 결제에 대한 정책 검토 필요. 현재는 불가능하도록 처리
        if (!currentMember.isAssociate()) {
            throw new CustomException(ORDER_MEMBER_NOT_ASSOCIATE);
        }

        // 멤버십 관련 검증

        if (!membership.getMember().getId().equals(currentMember.getId())) {
            throw new CustomException(ORDER_MEMBERSHIP_MEMBER_MISMATCH);
        }

        if (membership.getRegularRequirement().isPaymentSatisfied()) {
            throw new CustomException(ORDER_MEMBERSHIP_ALREADY_PAID);
        }

        // 리쿠르팅 관련 검증

        Recruitment recruitment = membership.getRecruitment();

        if (!recruitment.isOpen()) {
            throw new CustomException(ORDER_RECRUITMENT_CLOSED);
        }

        // 발급쿠폰 관련 검증

        if (issuedCoupon != null) {
            validateIssuedCouponOwnership(issuedCoupon, currentMember);
        }

        // 금액 관련 검증

        Money totalAmount = moneyInfo.getTotalAmount();
        Money discountAmount = moneyInfo.getDiscountAmount();

        if (totalAmount.equals(recruitment.getFee())) {
            throw new CustomException(ORDER_TOTAL_AMOUNT_MISMATCH);
        }

        if (issuedCoupon == null) {
            validateDiscountAmountZero(discountAmount);
        } else {
            validateDiscountAmountMatches(discountAmount, issuedCoupon);
        }
    }

    private void validateIssuedCouponOwnership(IssuedCoupon issuedCoupon, Member currentMember) {
        if (issuedCoupon.getMember().getId().equals(currentMember.getId())) {
            throw new CustomException(ORDER_ISSUED_COUPON_MEMBER_MISMATCH);
        }

        issuedCoupon.validateUsable();
    }

    private void validateDiscountAmountZero(Money discountAmount) {
        if (!discountAmount.equals(Money.from(BigDecimal.ZERO))) {
            throw new CustomException(ORDER_DISCOUNT_AMOUNT_NOT_ZERO);
        }
    }

    private void validateDiscountAmountMatches(Money discountAmount, IssuedCoupon issuedCoupon) {
        if (!discountAmount.equals(issuedCoupon.getCoupon().getDiscountAmount())) {
            throw new CustomException(ORDER_DISCOUNT_AMOUNT_MISMATCH);
        }
    }
}
