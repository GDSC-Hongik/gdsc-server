package com.gdschongik.gdsc.domain.coupon.dao;

import static com.gdschongik.gdsc.domain.coupon.domain.QCoupon.coupon;

import com.gdschongik.gdsc.domain.coupon.dto.request.CouponQueryOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

public class CouponQueryMethod {
    protected BooleanExpression eqName(String name) {
        return name != null ? coupon.name.containsIgnoreCase(name) : null;
    }

    protected BooleanBuilder matchesQueryOption(CouponQueryOption queryOption) {
        return new BooleanBuilder().and(eqName(queryOption.couponName()));
    }
}
