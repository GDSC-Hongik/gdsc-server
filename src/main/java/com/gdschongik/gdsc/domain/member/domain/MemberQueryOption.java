package com.gdschongik.gdsc.domain.member.domain;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberQueryOption {
    STUDENT_ID("student-id"),
    NAME("name"),
    PHONE("phone"),
    DEPARTMENT("department"),
    EMAIL("email"),
    DISCORD_USERNAME("discord-username"),
    DISCORD_NICKNAME("discord-nickname");

    private final String type;

    public static MemberQueryOption of(String type) {
        if (type != null) {
            return Arrays.stream(values())
                    .filter(value -> value.type.equals(type))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_QUERY_PARAMETER));
        }

        return null;
    }
}
