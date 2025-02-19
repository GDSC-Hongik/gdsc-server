package com.gdschongik.gdsc.global.common.constant;

import java.util.List;
import java.util.stream.Stream;

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

    public static final String LOCAL_CLIENT_ONBOARDING_URL = "https://local-onboarding.gdschongik.com";
    public static final String LOCAL_CLIENT_ADMIN_URL = "https://local-admin.gdschongik.com";
    public static final String LOCAL_CLIENT_STUDY_URL = "https://local-study.gdschongik.com";
    public static final String LOCAL_CLIENT_STUDY_MENTOR_URL = "https://local-mentor.study.gdschongik.com";

    public static final String LOCAL_REACT_CLIENT_URL = "http://localhost:3000";
    public static final String LOCAL_REACT_CLIENT_SECURE_URL = "https://localhost:3000";
    public static final String LOCAL_VITE_CLIENT_URL = "http://localhost:5173";
    public static final String LOCAL_VITE_CLIENT_SECURE_URL = "https://localhost:5173";

    public static final List<String> PROD_CLIENT_URLS = List.of(
            PROD_CLIENT_ONBOARDING_URL, PROD_CLIENT_ADMIN_URL, PROD_CLIENT_STUDY_URL, PROD_CLIENT_STUDY_MENTOR_URL);

    public static final List<String> DEV_CLIENT_URLS =
            List.of(DEV_CLIENT_ONBOARDING_URL, DEV_CLIENT_ADMIN_URL, DEV_CLIENT_STUDY_URL, DEV_CLIENT_STUDY_MENTOR_URL);

    public static final List<String> LOCAL_CLIENT_URLS = List.of(
            LOCAL_CLIENT_ONBOARDING_URL,
            LOCAL_CLIENT_ADMIN_URL,
            LOCAL_CLIENT_STUDY_URL,
            LOCAL_CLIENT_STUDY_MENTOR_URL,
            LOCAL_REACT_CLIENT_URL,
            LOCAL_REACT_CLIENT_SECURE_URL,
            LOCAL_VITE_CLIENT_URL,
            LOCAL_VITE_CLIENT_SECURE_URL);

    public static final List<String> DEV_AND_LOCAL_CLIENT_URLS =
            Stream.concat(DEV_CLIENT_URLS.stream(), LOCAL_CLIENT_URLS.stream()).toList();

    // 서버 URL
    public static final String PROD_SERVER_URL = "https://api.wawoo.dev";
    public static final String DEV_SERVER_URL = "https://dev-api.wawoo.dev";
    public static final String LOCAL_SERVER_URL = "http://localhost:8080";

    // 기타
    public static final String ROOT_DOMAIN = "gdschongik.com";
}
