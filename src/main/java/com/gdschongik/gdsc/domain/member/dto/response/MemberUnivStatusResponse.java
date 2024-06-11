package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUnivStatusResponse(@Schema(description = "재학생 인증 완료 여부") RequirementStatus univStatus) {
    public static MemberUnivStatusResponse from(Member member) {
        return new MemberUnivStatusResponse(member.getRequirement().getUnivStatus());
    }
}
