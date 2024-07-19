package com.gdschongik.gdsc.global.util.email;

import static com.gdschongik.gdsc.global.common.constant.EmailConstant.HONGIK_UNIV_MAIL_DOMAIN;
import static com.gdschongik.gdsc.global.common.constant.RegexConstant.HONGIK_EMAIL;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.Optional;

@DomainService
public class HongikUnivEmailValidator {

    public void validate(String email, Optional<Member> optionalMember) {
        if (!email.contains(HONGIK_UNIV_MAIL_DOMAIN)) {
            throw new CustomException(UNIV_EMAIL_DOMAIN_MISMATCH);
        }

        if (!email.matches(HONGIK_EMAIL)) {
            throw new CustomException(UNIV_EMAIL_FORMAT_MISMATCH);
        }

        if (optionalMember.isPresent()) {
            throw new CustomException(UNIV_EMAIL_ALREADY_SATISFIED);
        }
    }
}
