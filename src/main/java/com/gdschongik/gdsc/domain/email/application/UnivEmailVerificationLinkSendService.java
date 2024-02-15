package com.gdschongik.gdsc.domain.email.application;

import com.gdschongik.gdsc.domain.email.constant.UnivMailVerificationConstant;
import com.gdschongik.gdsc.domain.email.dao.UnivEmailVerificationRepository;
import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import com.gdschongik.gdsc.domain.email.util.VerificationCodeGenerator;
import com.gdschongik.gdsc.domain.email.util.VerificationLinkUtil;
import com.gdschongik.gdsc.domain.email.util.VerificationMailContentWriter;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.global.util.email.HongikUnivEmailValidator;
import com.gdschongik.gdsc.global.util.email.MailSender;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivEmailVerificationLinkSendService {

    private final UnivEmailVerificationRepository univEmailVerificationRepository;

    private final MailSender mailSender;
    private final HongikUnivEmailValidator hongikUnivEmailValidator;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final VerificationLinkUtil verificationLinkUtil;
    private final VerificationMailContentWriter verificationMailContentWriter;
    private final MemberUtil memberUtil;
    public static final Duration VERIFICATION_CODE_TIME_TO_LIVE = Duration.ofMinutes(10);

    public void send(String email) {
        hongikUnivEmailValidator.validate(email);
        validateUnivEmailNotVerified(email);

        String verificationCode = verificationCodeGenerator.generate();
        sendVerificationLink(email, verificationCode);

        saveUnivEmailVerification(verificationCode, email);
    }

    private void validateUnivEmailNotVerified(String email) {
        univEmailVerificationRepository.findByUnivEmail(email).ifPresent(univEmailVerification -> {
            throw new CustomException(ErrorCode.UNIV_EMAIL_ALREADY_VERIFIED);
        });
    }

    private void sendVerificationLink(String verificationCode, String email) {
        String verificationLink = verificationLinkUtil.createLink(verificationCode);
        String mailContent = verificationMailContentWriter.write(verificationLink, VERIFICATION_CODE_TIME_TO_LIVE);

        mailSender.send(email, UnivMailVerificationConstant.VERIFICATION_EMAIL_SUBJECT, mailContent);
    }

    private void saveUnivEmailVerification(String email, String verificationCode) {
        Long currentMemberId = memberUtil.getCurrentMemberId();
        UnivEmailVerification univEmailVerification = new UnivEmailVerification(
                verificationCode, email, currentMemberId, VERIFICATION_CODE_TIME_TO_LIVE.toSeconds());

        univEmailVerificationRepository.save(univEmailVerification);
    }
}
