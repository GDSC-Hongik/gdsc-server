package com.gdschongik.gdsc.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ASSOCIATE("ROLE_ASSOCIATE"),
    REGULAR("ROLE_REGULAR"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}
