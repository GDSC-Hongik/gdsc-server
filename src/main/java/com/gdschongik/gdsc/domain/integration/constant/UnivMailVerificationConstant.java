package com.gdschongik.gdsc.domain.integration.constant;

public class UnivMailVerificationConstant {

    public static final String VERIFY_EMAIL_API_ENDPOINT = "/onboarding/verify-email?%s=";

    public static final String VERIFY_EMAIL_QUERY_STRING_KEY = "token";

    public static final String VERIFICATION_EMAIL_SUBJECT = "GDSC Hongik 이메일 인증 코드입니다.";

    private UnivMailVerificationConstant() {}
}
