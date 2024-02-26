package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

public record MemberGrantResponse(
        @Schema(description = "승인에 성공한 멤버 이름 리스트") List<String> grantedMembers,
        @Schema(description = "승인에 실패한 멤버 이름 리스트") List<String> notGrantedMembers) {
    public static MemberGrantResponse from(Map<Boolean, List<Member>> grantResult) {
        List<String> grantedMemberIdList =
                grantResult.get(true).stream().map(Member::getName).toList();
        List<String> notGrantedMemberIdList =
                grantResult.get(false).stream().map(Member::getName).toList();
        return new MemberGrantResponse(grantedMemberIdList, notGrantedMemberIdList);
    }
}
