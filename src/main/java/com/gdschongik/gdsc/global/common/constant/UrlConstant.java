package com.gdschongik.gdsc.global.common.constant;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import java.util.List;

public class UrlConstant {

    private UrlConstant() {}

    // 클라이언트 URL
    public static final String PROD_CLIENT_URL = "https://onboarding.gdschongik.com";
    public static final String DEV_CLIENT_URL = "https://dev-onboarding.gdschongik.com";
    public static final String LOCAL_REACT_CLIENT_URL = "http://localhost:3000";
    public static final String LOCAL_REACT_CLIENT_SECURE_URL = "https://localhost:3000";
    public static final String LOCAL_VITE_CLIENT_URL = "http://localhost:5173";
    public static final String LOCAL_VITE_CLIENT_SECURE_URL = "https://localhost:5173";
    public static final List<String> LOCAL_CLIENT_URLS = List.of(
            LOCAL_REACT_CLIENT_URL, LOCAL_REACT_CLIENT_SECURE_URL, LOCAL_VITE_CLIENT_URL, LOCAL_VITE_CLIENT_SECURE_URL);

    // 서버 URL
    public static final String PROD_SERVER_URL = "https://api.gdschongik.com";
    public static final String DEV_SERVER_URL = "https://dev-api.gdschongik.com";
    public static final String LOCAL_SERVER_URL = "http://localhost:8080";

    // 기타
    public static final String ROOT_DOMAIN = "gdschongik.com";
}
