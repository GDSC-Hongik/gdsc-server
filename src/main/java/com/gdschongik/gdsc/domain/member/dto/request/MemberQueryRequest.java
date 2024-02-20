package com.gdschongik.gdsc.domain.member.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberQueryRequest(
        @Schema(description = "학번", pattern = STUDENT_ID) String studentId,
        @Schema(description = "이름") String name,
        @Schema(description = "전화번호", pattern = PHONE) String phone,
        @Schema(description = "학과") String department,
        @Schema(description = "이메일") String email,
        @Schema(description = "디스코드 유저네임") String discordUsername,
        @Schema(description = "커뮤니티 닉네임", pattern = NICKNAME) String nickname) {}
