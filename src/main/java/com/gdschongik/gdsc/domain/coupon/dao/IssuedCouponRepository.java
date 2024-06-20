package com.gdschongik.gdsc.domain.coupon.dao;

import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {}
