package com.gdschongik.gdsc.domain.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IssuanceMethodType {
    AUTOMATIC("자동 발급"),
    MANUAL("수동 발급");

    private final String value;
}
