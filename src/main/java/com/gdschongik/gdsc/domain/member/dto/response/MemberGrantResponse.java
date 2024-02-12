package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MemberGrantResponse(@Schema(description = "승인에 성공한 멤버 ID 리스트") List<Long> grantedMembers) {
    public static MemberGrantResponse of(List<Member> grantedMembers) {
        List<Long> grantedMemberIdList =
                grantedMembers.stream().map(Member::getId).toList();
        return new MemberGrantResponse(grantedMemberIdList);
    }
}
