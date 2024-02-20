package com.gdschongik.gdsc.domain.email.application;

import com.gdschongik.gdsc.domain.email.constant.UnivEmailVerificationConstant;
import com.gdschongik.gdsc.domain.email.dao.UnivEmailVerificationRepository;
import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import com.gdschongik.gdsc.domain.email.util.VerificationCodeGenerator;
import com.gdschongik.gdsc.domain.email.util.VerificationLinkUtil;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.global.util.email.HongikUnivEmailValidator;
import com.gdschongik.gdsc.global.util.email.MailSender;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivEmailVerificationLinkSendService {

    private final MemberRepository memberRepository;
    private final UnivEmailVerificationRepository univEmailVerificationRepository;

    private final MailSender mailSender;
    private final HongikUnivEmailValidator hongikUnivEmailValidator;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final VerificationLinkUtil verificationLinkUtil;
    private final MemberUtil memberUtil;
    public static final Duration VERIFICATION_CODE_TIME_TO_LIVE = Duration.ofMinutes(10);

    private static final String NOTIFICATION_MESSAGE = "<div style='margin:20px;'>"
            + "<h1> 안녕하세요 GDSC Hongik 재학생 인증 메일입니다. </h1> <br>"
            + "<h3> 아래의 링크를 %d분 안에 클릭해주세요. </h3> <br>"
            + "<h3> 감사합니다. </h3> <br>"
            + "CODE : <strong>";

    public void send(String univEmail) {
        hongikUnivEmailValidator.validate(univEmail);
        validateUnivEmailNotVerified(univEmail);

        String verificationCode = verificationCodeGenerator.generate();
        String verificationLink = verificationLinkUtil.createLink(verificationCode);
        String mailContent = writeMailContentWithVerificationLink(verificationLink);
        mailSender.send(univEmail, UnivEmailVerificationConstant.VERIFICATION_EMAIL_SUBJECT, mailContent);

        saveUnivEmailVerification(univEmail, verificationCode);
    }

    private void validateUnivEmailNotVerified(String univEmail) {
        Optional<Member> member = memberRepository.findByUnivEmail(univEmail);
        if (member.isPresent()) {
            throw new CustomException(ErrorCode.UNIV_EMAIL_ALREADY_VERIFIED);
        }
    }

    private String writeMailContentWithVerificationLink(String verificationLink) {
        return String.format(NOTIFICATION_MESSAGE, VERIFICATION_CODE_TIME_TO_LIVE.toMinutes()) + verificationLink;
    }

    private void saveUnivEmailVerification(String univEmail, String verificationCode) {
        Long currentMemberId = memberUtil.getCurrentMemberId();
        UnivEmailVerification univEmailVerification = new UnivEmailVerification(
                verificationCode, univEmail, currentMemberId, VERIFICATION_CODE_TIME_TO_LIVE.toSeconds());

        univEmailVerificationRepository.save(univEmailVerification);
    }
}
