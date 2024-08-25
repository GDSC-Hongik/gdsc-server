package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentSubmissionStatus {
    FAILURE("제출 실패"),
    SUCCESS("제출 성공");

    private final String value;
}
