package com.gdschongik.gdsc.domain.member.dto;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record MemberDto(Long memberId, String studentId, String name, String email, String phone) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(), member.getStudentId(), member.getName(), member.getEmail(), member.getPhone());
    }
}
