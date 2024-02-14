package com.gdschongik.gdsc.domain.integration;

import static com.gdschongik.gdsc.domain.integration.UnivMailVerificationConstant.VERIFICATION_EMAIL_SUBJECT;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivEmailVerificationLinkSenderService {

    private final MemberRepository memberRepository;
    private final VerificationCodeAndEmailRepository verificationCodeAndEmailRepository;

    private final MemberUtil memberUtil;
    private final MailSender mailSender;
    private final HongikUnivEmailValidator hongikUnivEmailValidator;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final VerificationLinkUtil verificationLinkUtil;
    private final VerificationMailContentWriter verificationMailContentWriter;
    public static final Duration VERIFICATION_CODE_TIME_TO_LIVE = Duration.ofMinutes(10);

    public void send() {
        Long currentMemberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        sendVerificationLink(member.getUnivEmail());
    }

    private void sendVerificationLink(String email) {
        hongikUnivEmailValidator.validate(email);

        String verificationCode = verificationCodeGenerator.generate();
        sendVerificationLink(email, verificationCode);

        saveVerificationCodeAndEmail(verificationCode, email);
    }

    private void sendVerificationLink(String verificationCode, String email) {
        String verificationLink = verificationLinkUtil.createLink(verificationCode);
        String mailContent = verificationMailContentWriter.write(verificationLink,
            VERIFICATION_CODE_TIME_TO_LIVE);

        mailSender.send(email, VERIFICATION_EMAIL_SUBJECT, mailContent);
    }

    private void saveVerificationCodeAndEmail(String email, String verificationCode) {
        VerificationCodeAndEmail verificationCodeAndEmail =
            new VerificationCodeAndEmail(verificationCode, email,
                VERIFICATION_CODE_TIME_TO_LIVE.toSeconds());

        verificationCodeAndEmailRepository.save(verificationCodeAndEmail);
    }
}
