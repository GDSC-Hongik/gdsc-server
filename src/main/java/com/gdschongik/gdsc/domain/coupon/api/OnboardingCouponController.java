package com.gdschongik.gdsc.domain.coupon.api;

import com.gdschongik.gdsc.domain.coupon.application.CouponService;
import com.gdschongik.gdsc.domain.coupon.dto.response.IssuedCouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Coupon - Onboarding", description = "온보딩 쿠폰 API입니다.")
@RestController
@RequestMapping("/onboarding/coupons")
@RequiredArgsConstructor
public class OnboardingCouponController {

    private final CouponService couponService;

    @Operation(summary = "사용 가능한 내 발급쿠폰 조회", description = "나에게 발급된 쿠폰 중 사용 가능한 것만 조회합니다.")
    @GetMapping("/issued/me")
    public ResponseEntity<List<IssuedCouponResponse>> getMyUsableIssuedCoupons() {
        var response = couponService.findMyUsableIssuedCoupons();
        return ResponseEntity.ok().body(response);
    }
}
