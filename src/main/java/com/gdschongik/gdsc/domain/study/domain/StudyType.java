package com.gdschongik.gdsc.domain.study.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyType {
    ASSIGNMENT("과제 스터디"),
    ONLINE("온라인 스터디"),
    OFFLINE("오프라인 스터디");

    private final String value;
}
