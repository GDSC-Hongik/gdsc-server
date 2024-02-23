package com.gdschongik.gdsc.global.common.constant;

public class DiscordConstant {

    private DiscordConstant() {}

    // 인증코드 발급 커맨드
    public static final String COMMAND_NAME_ISSUING_CODE = "인증코드";
    public static final String COMMAND_DESCRIPTION_ISSUING_CODE = "디스코드 연동을 위한 인증코드를 발급받습니다.";
    public static final String DEFER_MESSAGE_ISSUING_CODE = "인증코드를 발급받는 중입니다...";
    public static final String REPLY_MESSAGE_ISSUING_CODE = "인증코드는 %d 입니다. 인증코드는 %d분 동안 유효합니다.";

    // 가입하기 커맨드
    public static final String COMMAND_NAME_JOIN = "가입하기";
    public static final String COMMAND_DESCRIPTION_JOIN = "가입 신청이 승인된 멤버에게 역할을 부여합니다.";
    public static final String DEFER_MESSAGE_JOIN = "가입 신청을 처리하는 중입니다...";
    public static final String REPLY_MESSAGE_JOIN = "가입 신청이 승인되었습니다. 환영합니다!";
}
