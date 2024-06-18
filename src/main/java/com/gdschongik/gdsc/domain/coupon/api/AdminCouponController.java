package com.gdschongik.gdsc.domain.coupon.api;

import com.gdschongik.gdsc.domain.coupon.application.CouponService;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponIssueRequest;
import com.gdschongik.gdsc.domain.coupon.dto.response.CouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Coupon", description = "어드민 쿠폰 관리 API입니다.")
@RestController
@RequestMapping("/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 생성", description = "쿠폰을 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponCreateRequest request) {
        couponService.createCoupon(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "쿠폰 조회", description = "쿠폰을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CouponResponse>> getCoupons() {
        List<CouponResponse> response = couponService.findAllCoupons();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "쿠폰 발급", description = "쿠폰을 발급합니다.")
    @PostMapping("/issued")
    public ResponseEntity<Void> createIssuedCoupon(@Valid @RequestBody CouponIssueRequest request) {
        couponService.createIssuedCoupon(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "쿠폰 회수", description = "쿠폰을 회수합니다.")
    @DeleteMapping("/issued/{issuedCouponId}")
    public ResponseEntity<Void> deleteIssuedCoupon(@PathVariable Long issuedCouponId) {
        couponService.revokeIssuedCoupon(issuedCouponId);
        return ResponseEntity.ok().build();
    }
}
