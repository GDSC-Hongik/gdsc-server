package com.gdschongik.gdsc.domain.integration.application;

import static com.gdschongik.gdsc.domain.integration.constant.UnivMailVerificationConstant.VERIFICATION_EMAIL_SUBJECT;

import com.gdschongik.gdsc.domain.integration.dao.VerificationCodeAndEmailRepository;
import com.gdschongik.gdsc.domain.integration.domain.VerificationCodeAndEmail;
import com.gdschongik.gdsc.domain.integration.util.VerificationCodeGenerator;
import com.gdschongik.gdsc.domain.integration.util.VerificationLinkUtil;
import com.gdschongik.gdsc.domain.integration.util.VerificationMailContentWriter;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.global.util.mail.HongikUnivEmailValidator;
import com.gdschongik.gdsc.global.util.mail.MailSender;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivEmailVerificationLinkSenderService {

    private final VerificationCodeAndEmailRepository verificationCodeAndEmailRepository;

    private final MemberUtil memberUtil;
    private final MailSender mailSender;
    private final HongikUnivEmailValidator hongikUnivEmailValidator;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final VerificationLinkUtil verificationLinkUtil;
    private final VerificationMailContentWriter verificationMailContentWriter;
    public static final Duration VERIFICATION_CODE_TIME_TO_LIVE = Duration.ofMinutes(10);

    public void send() {
        Member currnetMember = memberUtil.getCurrentMember();
        sendVerificationLink(currnetMember.getUnivEmail());
    }

    private void sendVerificationLink(String email) {
        hongikUnivEmailValidator.validate(email);

        String verificationCode = verificationCodeGenerator.generate();
        sendVerificationLink(email, verificationCode);

        saveVerificationCodeAndEmail(verificationCode, email);
    }

    private void sendVerificationLink(String verificationCode, String email) {
        String verificationLink = verificationLinkUtil.createLink(verificationCode);
        String mailContent = verificationMailContentWriter.write(verificationLink, VERIFICATION_CODE_TIME_TO_LIVE);

        mailSender.send(email, VERIFICATION_EMAIL_SUBJECT, mailContent);
    }

    private void saveVerificationCodeAndEmail(String email, String verificationCode) {
        VerificationCodeAndEmail verificationCodeAndEmail =
                new VerificationCodeAndEmail(verificationCode, email, VERIFICATION_CODE_TIME_TO_LIVE.toSeconds());

        verificationCodeAndEmailRepository.save(verificationCodeAndEmail);
    }
}
