package com.gdschongik.gdsc.domain.recruitment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoundType {
    FIRST("1차"),
    SECOND("2차");

    private final String value;

    public boolean isFirst() {
        return this == FIRST;
    }

    public boolean isSecond() {
        return this == SECOND;
    }
}
