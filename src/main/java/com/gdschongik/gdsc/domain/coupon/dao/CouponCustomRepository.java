package com.gdschongik.gdsc.domain.coupon.dao;

import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponQueryOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponCustomRepository {
    Page<Coupon> findAllCoupons(CouponQueryOption queryOption, Pageable pageable);
}
