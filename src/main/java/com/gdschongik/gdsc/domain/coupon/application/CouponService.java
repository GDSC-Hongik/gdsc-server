package com.gdschongik.gdsc.domain.coupon.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.dao.CouponRepository;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponIssueRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.IssuedCouponQueryOption;
import com.gdschongik.gdsc.domain.coupon.dto.response.CouponResponse;
import com.gdschongik.gdsc.domain.coupon.dto.response.IssuedCouponResponse;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final MemberRepository memberRepository;

    @Transactional
    public void createCoupon(CouponCreateRequest request) {
        Coupon coupon = Coupon.create(request.name(), Money.from(request.discountAmount()));
        couponRepository.save(coupon);
        log.info("[CouponService] 쿠폰 생성: name={}, discountAmount={}", request.name(), request.discountAmount());
    }

    public List<CouponResponse> findAllCoupons() {
        return couponRepository.findAll().stream().map(CouponResponse::from).toList();
    }

    public Page<IssuedCouponResponse> findAllIssuedCoupons(IssuedCouponQueryOption queryOption, Pageable pageable) {
        Page<IssuedCoupon> issuedCoupons = issuedCouponRepository.findAllIssuedCoupons(queryOption, pageable);
        return issuedCoupons.map(IssuedCouponResponse::from);
    }

    @Transactional
    public void createIssuedCoupon(CouponIssueRequest request) {
        Coupon coupon =
                couponRepository.findById(request.couponId()).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        List<Member> members = memberRepository.findAllById(request.memberIds());

        List<IssuedCoupon> issuedCoupons = members.stream()
                .map(member -> IssuedCoupon.create(coupon, member))
                .toList();

        issuedCouponRepository.saveAll(issuedCoupons);

        log.info(
                "[CouponService] 쿠폰 발급: issuedCouponIds={}",
                issuedCoupons.stream().map(IssuedCoupon::getId).toList());
    }

    @Transactional
    public void revokeIssuedCoupon(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponRepository
                .findById(issuedCouponId)
                .orElseThrow(() -> new CustomException(ISSUED_COUPON_NOT_FOUND));

        issuedCoupon.revoke();
        log.info("[CouponService] 쿠폰 회수: issuedCouponId={}", issuedCouponId);
    }

    public List<IssuedCouponResponse> findMyUsableIssuedCoupons() {
        Member currentMember = memberUtil.getCurrentMember();

        return issuedCouponRepository.findByMember(currentMember).stream()
                .filter(IssuedCoupon::isUsable)
                .map(IssuedCouponResponse::from)
                .toList();
    }
}
