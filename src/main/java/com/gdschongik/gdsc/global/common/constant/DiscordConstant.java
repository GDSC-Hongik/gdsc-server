package com.gdschongik.gdsc.global.common.constant;

public class DiscordConstant {

    private DiscordConstant() {}

    // 공통 상수
    public static final String DISCORD_BOT_STATUS_CONTENT = "정상영업";
    public static final String MEMBER_ROLE_NAME = "커뮤니티-멤버";
    public static final String ADMIN_ROLE_NAME = "운영진";

    // 인증코드 발급 커맨드
    public static final String COMMAND_NAME_ISSUING_CODE = "인증코드";
    public static final String COMMAND_DESCRIPTION_ISSUING_CODE = "디스코드 연동을 위한 인증코드를 발급받습니다.";
    public static final String DEFER_MESSAGE_ISSUING_CODE = "인증코드를 발급받는 중입니다...";
    public static final String REPLY_MESSAGE_ISSUING_CODE = "인증코드는 %d 입니다. 인증코드는 %d분 동안 유효합니다.";

    // 가입하기 커맨드
    public static final String COMMAND_NAME_JOIN = "가입하기";
    public static final String COMMAND_DESCRIPTION_JOIN = "가입 신청이 승인된 멤버에게 역할을 부여합니다.";
    public static final String DEFER_MESSAGE_JOIN = "가입 신청을 처리하는 중입니다...";
    public static final String REPLY_MESSAGE_JOIN = "가입 신청이 승인되었습니다. GDSC Hongik에 합류하신 것을 환영합니다!";

    // 디스코드 ID 저장 커맨드
    public static final String COMMAND_NAME_BATCH_DISCORD_ID = "디스코드id-배치실행";
    public static final String COMMAND_DESCRIPTION_BATCH_DISCORD_ID = "디스코드 ID 배치를 진행합니다.";
    public static final String DEFER_MESSAGE_BATCH_DISCORD_ID = "디스코드 ID 배치를 진행하는 중입니다...";
    public static final String REPLY_MESSAGE_BATCH_DISCORD_ID = "디스코드 ID 배치가 완료되었습니다.";
}
