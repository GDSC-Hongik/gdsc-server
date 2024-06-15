package com.gdschongik.gdsc.domain.coupon.application;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private CouponRepository couponRepository;

    @Transactional
    public void createCoupon(CouponCreateRequest request) {
        Coupon coupon = Coupon.createCoupon(request.name(), Money.from(request.discountAmount()));
        couponRepository.save(coupon);
    }
}
