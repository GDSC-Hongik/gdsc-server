package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentSubmissionStatus {
    NOT_SUBMITTED("미제출"),
    WORD_COUNT_INSUFFICIENT("글자수 부족"),
    LOCATION_UNIDENTIFIABLE("위치 확인불가"),
    SUBMISSION_SUCCESS("제출 성공");

    private final String value;
}
