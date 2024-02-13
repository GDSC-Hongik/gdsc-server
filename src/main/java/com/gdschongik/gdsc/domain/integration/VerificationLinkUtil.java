package com.gdschongik.gdsc.domain.integration;

import static com.gdschongik.gdsc.domain.integration.UnivMailVerificationConstant.VERIFY_EMAIL_QUERY_STRING_KEY;
import static com.gdschongik.gdsc.domain.integration.UnivMailVerificationConstant.VERIFY_EMAIL_ENDPOINT;
import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.DEV;
import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.PROD;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.DEV_CLIENT_URL;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.LOCAL_VITE_CLIENT_SECURE_URL;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.PROD_CLIENT_URL;

import com.gdschongik.gdsc.global.util.EnvironmentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationLinkUtil {

    private final EnvironmentUtil environmentUtil;

    public String getLink(String verificationCode) {
        return getClientUrl()
            + VERIFY_EMAIL_ENDPOINT
            + VERIFY_EMAIL_QUERY_STRING_KEY
            + verificationCode;
    }

    private String getClientUrl() {
        return switch (environmentUtil.getCurrentProfile()) {
            case PROD -> PROD_CLIENT_URL;
            case DEV -> DEV_CLIENT_URL;
            default -> LOCAL_VITE_CLIENT_SECURE_URL;
        };
    }
}
