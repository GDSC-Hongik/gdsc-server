package com.gdschongik.gdsc.domain.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStudyRole {
    MENTOR("ROLE_MENTOR"),
    STUDENT("ROLE_STUDENT");

    private final String value;
}
