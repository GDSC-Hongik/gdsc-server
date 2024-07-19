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

    protected BooleanExpression hasUsed(Boolean hasUsed) {
        return hasUsed != null && hasUsed == true ? issuedCoupon.usedAt.isNotNull() : issuedCoupon.usedAt.isNull();
    }

    protected BooleanExpression hasRevoked(Boolean hasRevoked) {
        return hasRevoked != null ? issuedCoupon.hasRevoked.isTrue() : null;
    }

    protected BooleanBuilder matchesQueryOption(IssuedCouponQueryOption queryOption) {
        return new BooleanBuilder()
                .and(eqStudentId(queryOption.studentId()))
                .and(eqMemberName(queryOption.memberName()))
                .and(eqPhone(queryOption.phone()))
                .and(eqCouponName(queryOption.couponName()))
                .and(hasUsed(queryOption.hasUsed()))
                .and(hasRevoked(queryOption.hasRevoked()));
    }
}
