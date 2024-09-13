package com.gdschongik.gdsc.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    NORMAL("NORMAL"),
    DELETED("DELETED"),
    FORBIDDEN("FORBIDDEN");

    private final String value;

    public boolean isDeleted() {
        return this == DELETED;
    }

    public boolean isForbidden() {
        return this == FORBIDDEN;
    }
}
