package com.gdschongik.gdsc.domain.integration;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.HONGIK_EMAIL_REGEX;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HongikUnivEmailValidator {

    private static final String HONGIK_UNIV_MAIL_DOMAIN = "@g.hongik.ac.kr";

    public void validate(String email) {
        if (!email.contains(HONGIK_UNIV_MAIL_DOMAIN)) {
            throw new CustomException(ErrorCode.UNIV_EMAIL_DOMAIN_MISMATCH);
        }

        if (!email.matches(HONGIK_EMAIL_REGEX)) {
            throw new CustomException(ErrorCode.UNIV_EMAIL_FORMAT_MISMATCH);
        }
    }
}
