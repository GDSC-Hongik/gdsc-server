package com.gdschongik.gdsc.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnivVerificationStatus {
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    SATISFIED("SATISFIED");

    private final String value;
}
