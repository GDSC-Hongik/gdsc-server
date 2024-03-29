package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import java.time.LocalDate;
import java.time.LocalDateTime;

public enum LandingStatus {
    ONBOARDING_NOT_OPENED, // 대기 페이지로 랜딩
    ONBOARDING_CLOSED, // 모집 기간 마감
    TO_STUDENT_AUTHENTICATION, // 재학생 인증 페이지로 랜딩
    TO_REGISTRATION, // 가입신청 페이지로 랜딩
    TO_DASHBOARD, // 대시보드로 랜딩
    ;

    public static LandingStatus of(Member member) {
        // 1차 모집기간 종료 ~ 2차 모집기간 시작 사이 가입했고, 현재는 2차 모집기간이 아닐 때 대기 페이지로 랜딩
        if (member.getCreatedAt().isAfter(Constants.FIRST_RECRUITMENT_END_DATE.atStartOfDay())
                && member.getCreatedAt().isBefore(Constants.SECOND_RECRUITMENT_START_DATE.atStartOfDay())
                && LocalDateTime.now().isBefore(Constants.SECOND_RECRUITMENT_START_DATE.atStartOfDay())) {
            return ONBOARDING_NOT_OPENED;
        }

        // 2차 모집기간 종료일 12시 30분 이후, 신청서 미제출 상태면 마감 페이지로 랜딩
        if (LocalDateTime.now().isAfter(Constants.SECOND_RECRUITMENT_END_DATE.atTime(0, 30)) && !member.isApplied()) {
            return ONBOARDING_CLOSED;
        }

        // 2차 모집기간 종료일 1시 이후, Guest를 마감 페이지로 랜딩.
        if (LocalDateTime.now().isAfter(Constants.SECOND_RECRUITMENT_END_DATE.atTime(1, 0))
                && member.getRole().equals(MemberRole.GUEST)) {
            return ONBOARDING_CLOSED;
        }

        // 아직 재학생 인증을 하지 않았다면 재학생 인증 페이지로 랜딩
        if (!member.getRequirement().isUnivVerified()) {
            return TO_STUDENT_AUTHENTICATION;
        }

        // 재학생 인증은 했지만 가입신청을 하지 않았다면 가입신청 페이지로 랜딩
        // 가입신청 여부는 학번 존재여부로 판단
        if (!member.isApplied()) {
            return TO_REGISTRATION;
        }

        // 재학생 인증과 가입신청을 모두 완료했다면 대시보드로 랜딩
        return TO_DASHBOARD;
    }

    private static class Constants {
        private static final LocalDate FIRST_RECRUITMENT_END_DATE = LocalDate.of(2024, 3, 2);
        private static final LocalDate SECOND_RECRUITMENT_START_DATE = LocalDate.of(2024, 3, 4);
        private static final LocalDate SECOND_RECRUITMENT_END_DATE = LocalDate.of(2024, 3, 9);
    }
}
