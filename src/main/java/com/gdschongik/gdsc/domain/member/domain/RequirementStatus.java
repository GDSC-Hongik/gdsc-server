package com.gdschongik.gdsc.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequirementStatus {
    PENDING("PENDING"),
    VERIFIED("VERIFIED");

    private final String value;
}
