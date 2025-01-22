package com.gdschongik.gdsc.domain.studyv2.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyStatusV2 {
    OPENED("개설"),
    CANCELED("휴강"),
    ;

    private final String value;
}
