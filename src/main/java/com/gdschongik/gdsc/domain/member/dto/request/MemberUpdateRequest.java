package com.gdschongik.gdsc.domain.member.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberUpdateRequest(
        @NotBlank
                @Pattern(regexp = STUDENT_ID, message = "학번은 " + STUDENT_ID + " 형식이어야 합니다.")
                @Schema(description = "학번", pattern = STUDENT_ID)
                String studentId,
        @NotBlank @Schema(description = "이름") String name,
        @NotBlank
                @Pattern(regexp = PHONE, message = "전화번호는 " + PHONE + " 형식이어야 합니다.")
                @Schema(description = "전화번호", pattern = PHONE)
                String phone,
        @NotBlank @Schema(description = "학과") String department,
        @NotBlank @Email @Schema(description = "이메일") String email,
        @NotBlank @Schema(description = "디스코드 유저네임") String discordUsername,
        @NotBlank
                @Pattern(regexp = NICKNAME, message = "닉네임은 " + NICKNAME + " 형식이어야 합니다.")
                @Schema(description = "커뮤니티 닉네임", pattern = NICKNAME)
                String nickname) {}
