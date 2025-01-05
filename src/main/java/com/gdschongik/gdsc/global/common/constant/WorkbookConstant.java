package com.gdschongik.gdsc.global.common.constant;

public class WorkbookConstant {
    // Member
    public static final String ALL_MEMBER_SHEET_NAME = "전체 회원 목록";
    public static final String REGULAR_MEMBER_SHEET_NAME = "정회원 목록";
    public static final String[] MEMBER_SHEET_HEADER = {
        "가입 일시", "이름", "학번", "학과", "전화번호", "이메일", "디스코드 유저네임", "커뮤니티 닉네임"
    };

    // Study
    public static final String[] STUDY_SHEET_HEADER = {
        "이름", "학번", "디스코드 유저네임", "커뮤니티 닉네임", "깃허브 링크", "수료 상태", "1차 우수 스터디원 여부", "2차 우수 스터디원 여부", "출석률", "과제 수행률"
    };
    public static final String WEEKLY_ASSIGNMENT = "%d주차 과제";
    public static final String WEEKLY_ATTENDANCE = "%d주차 출석";

    private WorkbookConstant() {}
}
