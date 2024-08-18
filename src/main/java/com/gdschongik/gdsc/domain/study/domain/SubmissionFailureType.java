package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmissionFailureType {
    NONE("실패 없음"), // 제출상태 성공 시 사용
    NOT_SUBMITTED("미제출"), // 기본값
    WORD_COUNT_INSUFFICIENT("글자수 부족"),
    LOCATION_UNIDENTIFIABLE("위치 확인불가");

    private final String value;
}
