package com.gdschongik.gdsc.domain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Semester {
    FIRST("1학기"),
    SECOND("2학기");

    private final String value;
}
