package com.gdschongik.gdsc.domain.email.application;

import com.gdschongik.gdsc.domain.email.dao.UnivEmailVerificationRepository;
import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import com.gdschongik.gdsc.domain.email.dto.request.EmailVerificationTokenDto;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.email.EmailVerificationTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnivEmailVerificationService {
    private final EmailVerificationTokenUtil emailVerificationTokenUtil;

    private final MemberRepository memberRepository;
    private final UnivEmailVerificationRepository univEmailVerificationRepository;

    @Transactional
    public void verifyMemberUnivEmail(String verificationToken) {
        EmailVerificationTokenDto emailVerificationToken = getEmailVerificationToken(verificationToken);
        Member member = getMemberById(emailVerificationToken.memberId());
        member.completeUnivEmailVerification(emailVerificationToken.email());
    }

    private EmailVerificationTokenDto getEmailVerificationToken(String verificationToken) {
        return emailVerificationTokenUtil.parseEmailVerificationTokenDto(verificationToken);
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
