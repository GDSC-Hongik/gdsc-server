package com.gdschongik.gdsc.global.common.constant;

import java.util.List;

public class UrlConstant {

    private UrlConstant() {}

    // 클라이언트 URL
    public static final String PROD_CLIENT_ONBOARDING_URL = "https://onboarding.gdschongik.com";
    public static final String PROD_CLIENT_ADMIN_URL = "https://admin.gdschongik.com";
    public static final String PROD_CLIENT_STUDY_URL = "https://study.gdschongik.com";
    public static final String PROD_CLIENT_STUDY_MENTOR_URL = "https://mentor.study.gdschongik.com";
    public static final String DEV_CLIENT_ONBOARDING_URL = "https://dev-onboarding.gdschongik.com";
    public static final String DEV_CLIENT_ADMIN_URL = "https://dev-admin.gdschongik.com";
    public static final String DEV_CLIENT_STUDY_URL = "https://dev-study.gdschongik.com";
    public static final String DEV_CLIENT_STUDY_MENTOR_URL = "https://dev-mentor.study.gdschongik.com";
    public static final String LOCAL_REACT_CLIENT_URL = "http://localhost:3000";
    public static final String LOCAL_REACT_CLIENT_SECURE_URL = "https://localhost:3000";
    public static final String LOCAL_VITE_CLIENT_URL = "http://localhost:5173";
    public static final String LOCAL_VITE_CLIENT_SECURE_URL = "https://localhost:5173";
    public static final String LOCAL_PROXY_CLIENT_ONBOARDING_URL = "https://local-onboarding.gdschongik.com";

    public static final List<String> PROD_CLIENT_URLS = List.of(
            PROD_CLIENT_ONBOARDING_URL, PROD_CLIENT_ADMIN_URL, PROD_CLIENT_STUDY_URL, PROD_CLIENT_STUDY_MENTOR_URL);

    public static final List<String> DEV_CLIENT_URLS =
            List.of(DEV_CLIENT_ONBOARDING_URL, DEV_CLIENT_ADMIN_URL, DEV_CLIENT_STUDY_URL, DEV_CLIENT_STUDY_MENTOR_URL);

    // 서버 URL
    public static final String PROD_SERVER_URL = "https://api.gdschongik.com";
    public static final String DEV_SERVER_URL = "https://dev-api.gdschongik.com";
    public static final String LOCAL_SERVER_URL = "http://localhost:8080";

    // 기타
    public static final String ROOT_DOMAIN = "gdschongik.com";
}
