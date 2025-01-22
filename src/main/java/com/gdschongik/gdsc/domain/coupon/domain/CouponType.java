package com.gdschongik.gdsc.domain.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponType {
    ADMIN("어드민"),
    STUDY_COMPLETION("스터디 수료");

    private final String value;
}
