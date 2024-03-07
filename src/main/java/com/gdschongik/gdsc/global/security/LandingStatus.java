package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.member.domain.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;

public enum LandingStatus {
    ONBOARDING_NOT_OPENED, // 대기 페이지로 랜딩
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

        // 2차 모집기간 종료 이후 가입했다면 대기 페이지로 랜딩
        if (member.getCreatedAt().isAfter(Constants.SECOND_RECRUITMENT_END_DATE.atStartOfDay())) {
            return ONBOARDING_NOT_OPENED;
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

    private static class Constants {
        private static final LocalDate FIRST_RECRUITMENT_END_DATE = LocalDate.of(2024, 3, 2);
        private static final LocalDate SECOND_RECRUITMENT_START_DATE = LocalDate.of(2024, 3, 4);
        private static final LocalDate SECOND_RECRUITMENT_END_DATE = LocalDate.of(2024, 3, 9);
    }
}
