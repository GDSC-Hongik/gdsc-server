package com.gdschongik.gdsc.domain.study.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Difficulty {
    HIGH("상"),
    MEDIUM("중"),
    LOW("하");

    private final String value;
}
