package com.gdschongik.gdsc.domain.study.domain;

public record StudyHistoryCompletionWithdrawnEvent(long studyHistoryId) {
    // TODO: 이벤트 내부 필드의 식별자 값은 항상 not null이므로, primitive 타입으로 사용하도록 변경
}
