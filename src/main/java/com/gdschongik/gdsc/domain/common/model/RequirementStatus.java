package com.gdschongik.gdsc.domain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequirementStatus {
    UNSATISFIED("UNSATISFIED"),
    SATISFIED("SATISFIED");

    private final String value;
}
