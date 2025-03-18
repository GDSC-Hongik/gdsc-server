package com.gdschongik.gdsc.domain.coupon.dao;

import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.CouponType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponTypeAndStudy(CouponType couponType, StudyV2 study);
}
