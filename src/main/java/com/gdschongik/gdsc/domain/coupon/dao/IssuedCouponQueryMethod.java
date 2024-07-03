package com.gdschongik.gdsc.domain.coupon.dao;

import static com.gdschongik.gdsc.domain.coupon.domain.QIssuedCoupon.issuedCoupon;

import com.gdschongik.gdsc.domain.coupon.dto.request.IssuedCouponQueryOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

public class IssuedCouponQueryMethod {

    protected BooleanExpression eqStudentId(String studentId) {
        return studentId != null ? issuedCoupon.member.studentId.containsIgnoreCase(studentId) : null;
    }

    protected BooleanExpression eqMemberName(String memberName) {
        return memberName != null ? issuedCoupon.coupon.name.containsIgnoreCase(memberName) : null;
    }

    protected BooleanExpression eqPhone(String phone) {
        return phone != null ? issuedCoupon.member.phone.contains(phone.replaceAll("-", "")) : null;
    }

    protected BooleanExpression eqCouponName(String couponName) {
        return couponName != null ? issuedCoupon.coupon.name.containsIgnoreCase(couponName) : null;
    }

    protected BooleanExpression isUsed(boolean isUsed) {
        return isUsed ? issuedCoupon.usedAt.isNotNull() : issuedCoupon.usedAt.isNull();
    }

    protected BooleanExpression isRevoked(boolean isRevoked) {
        return isRevoked ? issuedCoupon.isRevoked.isTrue() : issuedCoupon.isRevoked.isFalse();
    }

    protected BooleanBuilder matchesQueryOption(IssuedCouponQueryOption queryOption) {
        return new BooleanBuilder()
                .and(eqStudentId(queryOption.studentId()))
                .and(eqMemberName(queryOption.memberName()))
                .and(eqPhone(queryOption.phone()))
                .and(eqCouponName(queryOption.couponName()))
                .and(isUsed(queryOption.isUsed()))
                .and(isRevoked(queryOption.isRevoked()));
    }
}