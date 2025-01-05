package com.gdschongik.gdsc.domain.study.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyStatus {
    NONE("생성"),
    OPEN("개설"),
    CANCELED("휴강");

    private final String value;
}
