package com.gdschongik.gdsc.domain.study.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchievementType {
    FIRST_ROUND_OUTSTANDING_STUDENT("1차 우수 스터디원"),
    SECOND_ROUND_OUTSTANDING_STUDENT("2차 우수 스터디원");

    private final String value;
}
