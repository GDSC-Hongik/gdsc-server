package com.gdschongik.gdsc.domain.member.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberUpdateRequest(
        @NotBlank @Pattern(regexp = STUDENT_ID, message = "학번은 " + STUDENT_ID + " 형식이어야 합니다.") String studentId,
        @NotBlank String name,
        @NotBlank @Pattern(regexp = PHONE, message = "전화번호는 " + PHONE + " 형식이어야 합니다.") String phone,
        @NotBlank String department,
        @NotBlank @Email String email,
        @NotBlank String discordUsername,
        @NotBlank @Pattern(regexp = NICKNAME, message = "닉네임은 " + NICKNAME + " 형식이어야 합니다.") String nickname) {}
