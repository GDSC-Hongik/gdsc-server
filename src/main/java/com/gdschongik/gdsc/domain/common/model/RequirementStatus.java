package com.gdschongik.gdsc.domain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequirementStatus {
    PENDING("PENDING"),
    VERIFIED("VERIFIED");

    private final String value;
}
