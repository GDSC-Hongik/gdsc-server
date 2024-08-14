package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentSubmissionStatus {
    PENDING("제출 전"),
    FAILURE("제출 실패"),
    SUCCESS("제출 성공"),
    CANCELLED("과제 휴강");

    private final String value;
}
