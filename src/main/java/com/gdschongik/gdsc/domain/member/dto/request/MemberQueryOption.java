package com.gdschongik.gdsc.domain.member.dto.request;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MemberQueryOption(
        @Schema(description = "학번", pattern = STUDENT_ID) String studentId,
        @Schema(description = "이름") String name,
        @Schema(description = "전화번호", pattern = PHONE_WITHOUT_HYPHEN) String phone,
        @Schema(description = "학과") String department,
        @Schema(description = "이메일") String email,
        @Schema(description = "디스코드 유저네임") String discordUsername,
        @Schema(description = "커뮤니티 닉네임", pattern = NICKNAME) String nickname,
        @Schema(description = "멤버 권한") List<MemberRole> roles) {
    // TODO: 키워드 기반 검색 고려하여 pattern 제약조건 제거
}
