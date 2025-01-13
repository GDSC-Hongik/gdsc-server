package com.gdschongik.gdsc.domain.study.domain;

public record StudyHistoryCompletionWithdrawnEvent(long studyHistoryId) {
    // TODO: 이벤트 내부 필드의 식별자 값은 항상 not null이므로, primitive 타입으로 사용하도록 변경
    // TODO: 스터디 철회 시 기존에 발행된 쿠폰 회수하는 로직 구현
}
