package com.gdschongik.gdsc.domain.integration;

import static com.gdschongik.gdsc.domain.integration.UnivMailVerificationConstant.VERIFICATION_EMAIL_SUBJECT;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnivEmailVerificationService {

    private final HongikUnivEmailValidator hongikUnivEmailValidator;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final VerificationLinkUtil verificationLinkUtil;
    private final VerificationMailContentWriter verificationMailContentWriter;

    private final MailSender mailSender;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    public static final Duration VERIFICATION_CODE_TIME_TO_LIVE = Duration.ofMinutes(10);

    public void sendEmailVerificationMail(String email) {
        hongikUnivEmailValidator.validate(email);

        String verificationCode = verificationCodeGenerator.generate();
        sendVerificationLink(email, verificationCode);

        saveVerificationCode(email, verificationCode);
    }

    private void sendVerificationLink(String email, String verificationCode) {
        String verificationLink = verificationLinkUtil.getLink(verificationCode);
        String mailContent = verificationMailContentWriter.write(verificationLink, VERIFICATION_CODE_TIME_TO_LIVE);

        mailSender.send(email, VERIFICATION_EMAIL_SUBJECT, mailContent);
    }

    private void saveVerificationCode(String email, String verificationCode) {
        EmailVerificationCode emailVerificationCode =
                new EmailVerificationCode(email, verificationCode, VERIFICATION_CODE_TIME_TO_LIVE.toSeconds());

        emailVerificationCodeRepository.save(emailVerificationCode);
    }

    public void validateCodeMatch(String email, String userInputCode) {
        String verificationCode = getVerificationCodeByEmail(email);

        if (!Objects.equals(verificationCode, userInputCode)) {
            throw new CustomException(ErrorCode.UNIV_EMAIL_VERIFICATION_CODE_NOT_MATCH);
        }
    }

    private String getVerificationCodeByEmail(String email) {
        return emailVerificationCodeRepository
                .findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND))
                .getVerificationCode();
    }
}
