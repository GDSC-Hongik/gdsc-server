package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;

public enum LandingStatus {
    TO_ADMIN, // 어드민 페이지로 랜딩
    TO_STUDENT_AUTHENTICATION, // 재학생 인증 페이지로 랜딩
    TO_REGISTRATION, // 가입신청 페이지로 랜딩
    TO_DASHBOARD, // 대시보드로 랜딩
    ;

    public static LandingStatus of(Member member) {
        // 어드민이라면 어드민 페이지로 랜딩
        if (member.getRole().equals(MemberRole.ADMIN)) {
            return TO_ADMIN;
        }

        // 아직 재학생 인증을 하지 않았다면 재학생 인증 페이지로 랜딩
        if (!member.getRequirement().isUnivVerified()) {
            return TO_STUDENT_AUTHENTICATION;
        }

        // 재학생 인증은 했지만 가입신청을 하지 않았다면 가입신청 페이지로 랜딩
        // 가입신청 여부는 학번 존재여부로 판단
        if (member.getStudentId() == null) {
            return TO_REGISTRATION;
        }

        // 재학생 인증과 가입신청을 모두 완료했다면 대시보드로 랜딩
        return TO_DASHBOARD;
    }
}
