package com.gdschongik.gdsc.domain.email.application;

import com.gdschongik.gdsc.domain.email.dao.UnivEmailVerificationRepository;
import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UnivEmailVerificationService {

    private final MemberRepository memberRepository;
    private final UnivEmailVerificationRepository univEmailVerificationRepository;

    public void verifyMemberUnivEmail(String verificationCode) {
        UnivEmailVerification univEmailVerification = getUnivEmailVerification(verificationCode);
        Member member = getMemberById(univEmailVerification.getMemberId());
        member.completeUnivEmailVerification(univEmailVerification.getUnivEmail());
    }

    private UnivEmailVerification getUnivEmailVerification(String verificationCode) {
        return univEmailVerificationRepository
                .findById(verificationCode)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
