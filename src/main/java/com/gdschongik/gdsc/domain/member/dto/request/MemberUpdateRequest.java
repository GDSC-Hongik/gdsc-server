package com.gdschongik.gdsc.domain.member.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MemberUpdateRequest(
        @NotNull @Pattern(regexp = STUDENT_ID) String studentId,
        @NotNull String name,
        @NotNull @Pattern(regexp = PHONE) String phone,
        @NotNull String department,
        @NotNull @Email String email,
        @NotNull String discordUsername,
        @NotNull @Pattern(regexp = NICKNAME) String nickname) {}
