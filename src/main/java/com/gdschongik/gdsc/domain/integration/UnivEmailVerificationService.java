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
    private final VerificationCodeAndEmailRepository verificationCodeAndEmailRepository;

    public static final Duration VERIFICATION_CODE_TIME_TO_LIVE = Duration.ofMinutes(10);

    public void sendEmailVerificationMail(String email) {
        hongikUnivEmailValidator.validate(email);

        String verificationCode = verificationCodeGenerator.generate();
        sendVerificationLink(email, verificationCode);

        saveVerificationCodeAndEmail(email, verificationCode);
    }

    private void sendVerificationLink(String email, String verificationCode) {
        String verificationLink = verificationLinkUtil.createLink(verificationCode);
        String mailContent = verificationMailContentWriter.write(verificationLink, VERIFICATION_CODE_TIME_TO_LIVE);

        mailSender.send(email, VERIFICATION_EMAIL_SUBJECT, mailContent);
    }

    private void saveVerificationCodeAndEmail(String email, String verificationCode) {
        VerificationCodeAndEmail verificationCodeAndEmail =
                new VerificationCodeAndEmail(verificationCode, email, VERIFICATION_CODE_TIME_TO_LIVE.toSeconds());

        verificationCodeAndEmailRepository.save(verificationCodeAndEmail);
    }

    public void validateCodeMatch(String email, String userInputCode) {
        String verificationCode = getVerificationCodeByEmail(email);

        if (!Objects.equals(verificationCode, userInputCode)) {
            throw new CustomException(ErrorCode.UNIV_EMAIL_VERIFICATION_CODE_NOT_MATCH);
        }
    }

    private String getVerificationCodeByEmail(String email) {
        return verificationCodeAndEmailRepository
                .findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND))
                .getVerificationCode();
    }
}
