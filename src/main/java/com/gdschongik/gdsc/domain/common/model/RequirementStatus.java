package com.gdschongik.gdsc.domain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequirementStatus {
    PENDING("PENDING"),
    SATISFIED("SATISFIED");

    private final String value;
}
