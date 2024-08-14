package com.gdschongik.gdsc.domain.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    GUEST("ROLE_GUEST"),
    ASSOCIATE("ROLE_ASSOCIATE"),
    REGULAR("ROLE_REGULAR");

    private final String value;
}
