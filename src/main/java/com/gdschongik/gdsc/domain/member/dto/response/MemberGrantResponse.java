package com.gdschongik.gdsc.domain.member.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MemberGrantResponse(
        @Schema(description = "승인에 성공한 멤버 이름 리스트") List<String> grantedMembers,
        @Schema(description = "승인에 실패한 멤버 이름 리스트") List<String> notGrantedMembers) {
    public static MemberGrantResponse of(List<Member> grantedMembers, List<Member> notGrantedMembers) {
        List<String> grantedMemberIdList =
                grantedMembers.stream().map(Member::getName).toList();
        List<String> notGrantedMemberIdList =
                notGrantedMembers.stream().map(Member::getName).toList();
        return new MemberGrantResponse(grantedMemberIdList, notGrantedMemberIdList);
    }
}
