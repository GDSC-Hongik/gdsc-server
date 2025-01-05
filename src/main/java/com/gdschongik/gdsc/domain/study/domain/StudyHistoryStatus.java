package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyHistoryStatus {
    NONE("미수료"),
    COMPLETED("수료");

    private final String value;
}
