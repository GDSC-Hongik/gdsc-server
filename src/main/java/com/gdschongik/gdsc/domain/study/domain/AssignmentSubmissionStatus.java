package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentSubmissionStatus {
    // TODO: 클라이언트 응답에는 PENDING 상태 필요하므로, 추후 응답용 enum 클래스 생성 구현
    FAILURE("제출 실패"),
    SUCCESS("제출 성공");

    private final String value;
}
