package com.gdschongik.gdsc.global.common.constant;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

public class UrlConstant {

    private UrlConstant() {}

    public static final String PROD_CLIENT_URL = "https://onboarding.gdschongik.com";
    public static final String DEV_CLIENT_URL = "https://dev-onboarding.gdschongik.com";
    public static final String LOCAL_REACT_CLIENT_URL = "http://localhost:3000";
    public static final String LOCAL_REACT_CLIENT_SECURE_URL = "https://localhost:3000";
    public static final String LOCAL_VITE_CLIENT_URL = "http://localhost:5173";
    public static final String LOCAL_VITE_CLIENT_SECURE_URL = "https://localhost:5173";

    public static final String PROD_SERVER_URL = "https://api.gdschongik.com";
    public static final String DEV_SERVER_URL = "https://dev-api.gdschongik.com";
    public static final String LOCAL_SERVER_URL = "http://localhost:8080";

    public static final String SOCIAL_LOGIN_REDIRECT_URL = "%ssocial-login/redirect?%s=%s&%s=%s&%s=%s";
}
