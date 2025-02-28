package com.gdschongik.gdsc.domain.studyv2.domain;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentHistoryStatus {
    BEFORE_SUBMISSION("제출 전"),
    FAILED("제출 실패"),
    SUCCEEDED("제출 성공"),
    ;

    private final String value;

    public static AssignmentHistoryStatus of(
            AssignmentHistory assignmentHistory, StudySessionV2 studySession, LocalDateTime now) {
        // todo
        return null;
    }
}
