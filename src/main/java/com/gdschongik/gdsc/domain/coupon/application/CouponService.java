package com.gdschongik.gdsc.domain.coupon.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponIssueRequest;
import com.gdschongik.gdsc.domain.coupon.dto.response.CouponResponse;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final MemberUtil memberUtil;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    @Transactional
    public void createCoupon(CouponCreateRequest request) {
        Coupon coupon = Coupon.createCoupon(request.name(), Money.from(request.discountAmount()));
        couponRepository.save(coupon);
        log.info("[CouponService] 쿠폰 생성: name={}, discountAmount={}", request.name(), request.discountAmount());
    }

    public List<CouponResponse> findAllCoupons() {
        return couponRepository.findAll().stream().map(CouponResponse::from).toList();
    }

    @Transactional
    public void createIssuedCoupon(CouponIssueRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Coupon coupon =
                couponRepository.findById(request.couponId()).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, currentMember);
        issuedCouponRepository.save(issuedCoupon);
        log.info("[CouponService] 쿠폰 발급: couponId={}, memberId={}", request.couponId(), currentMember.getId());
    }
}
