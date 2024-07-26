package com.gdschongik.gdsc.domain.study.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyType {
    // todo: value를 과제 스터디, 온라인 세션, 오프라인 세션으로 변경 해야함.
    ASSIGNMENT("과제"),
    ONLINE("온라인"),
    OFFLINE("오프라인");

    private final String value;
}
