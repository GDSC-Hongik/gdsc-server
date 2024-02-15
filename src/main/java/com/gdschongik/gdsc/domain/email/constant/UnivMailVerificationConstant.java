package com.gdschongik.gdsc.domain.email.constant;

public class UnivMailVerificationConstant {

    public static final String VERIFY_EMAIL_API_ENDPOINT = "/onboarding/verify-email?%s=";

    public static final String VERIFY_EMAIL_REQUEST_PARAMETER_KEY = "token";

    public static final String VERIFICATION_EMAIL_SUBJECT = "GDSC Hongik 이메일 인증 코드입니다.";

    private UnivMailVerificationConstant() {}
}
