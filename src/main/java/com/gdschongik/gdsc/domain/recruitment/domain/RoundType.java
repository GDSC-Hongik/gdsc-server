package com.gdschongik.gdsc.domain.recruitment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoundType {
    FIRST("1차"),
    SECOND("2차");

    private final String value;
}
