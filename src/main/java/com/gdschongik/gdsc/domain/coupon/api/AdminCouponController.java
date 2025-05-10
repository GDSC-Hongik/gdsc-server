package com.gdschongik.gdsc.domain.coupon.api;

import com.gdschongik.gdsc.domain.coupon.application.CouponService;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponCreateRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponIssueRequest;
import com.gdschongik.gdsc.domain.coupon.dto.request.IssuedCouponQueryOption;
import com.gdschongik.gdsc.domain.coupon.dto.response.CouponResponse;
import com.gdschongik.gdsc.domain.coupon.dto.response.CouponTypeResponse;
import com.gdschongik.gdsc.domain.coupon.dto.response.IssuedCouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Coupon - Admin", description = "어드민 쿠폰 관리 API입니다.")
@RestController
@RequestMapping("/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 생성", description = "쿠폰을 생성합니다. 이름 및 할인금액을 가집니다.")
    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponCreateRequest request) {
        couponService.createCoupon(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "쿠폰 조회", description = "발급 가능한 모든 쿠폰을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CouponResponse>> getCoupons() {
        List<CouponResponse> response = couponService.findAllCoupons();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "쿠폰 종류 목록 조회", description = "발급 가능한 모든 쿠폰 종류 목록을 조회합니다.")
    @GetMapping("/types")
    public ResponseEntity<List<CouponTypeResponse>> getCouponTypes() {
        List<CouponTypeResponse> response = couponService.getCouponTypes();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "발급쿠폰 조회", description = "발급된 쿠폰을 조회합니다.")
    @GetMapping("/issued")
    public ResponseEntity<Page<IssuedCouponResponse>> getIssuedCoupons(
            IssuedCouponQueryOption queryOption, Pageable pageable) {
        Page<IssuedCouponResponse> response = couponService.findAllIssuedCoupons(queryOption, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "발급쿠폰 생성", description = "지정된 멤버들에게 쿠폰을 발급합니다. 존재하지 않는 멤버인 경우 무시됩니다.")
    @PostMapping("/issued")
    public ResponseEntity<Void> createIssuedCoupon(@Valid @RequestBody CouponIssueRequest request) {
        couponService.createIssuedCoupon(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "발급쿠폰 회수", description = "발급된 쿠폰을 회수합니다.")
    @DeleteMapping("/issued/{issuedCouponId}")
    public ResponseEntity<Void> revokeIssuedCoupon(@PathVariable Long issuedCouponId) {
        couponService.revokeIssuedCoupon(issuedCouponId);
        return ResponseEntity.ok().build();
    }
}
