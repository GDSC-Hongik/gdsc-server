package com.gdschongik.gdsc.domain.coupon.dao;

import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.IssuedCouponQueryOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssuedCouponCustomRepository {

    Page<IssuedCoupon> findAllIssuedCoupons(IssuedCouponQueryOption queryOption, Pageable pageable);
}
