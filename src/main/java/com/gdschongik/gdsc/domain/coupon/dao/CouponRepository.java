package com.gdschongik.gdsc.domain.coupon.dao;

import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {}
