package com.gdschongik.gdsc.domain.email.application;

import com.gdschongik.gdsc.domain.email.dao.UnivEmailVerificationRepository;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
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
        String univEmail = getUnivEmailByVerificationCode(verificationCode);
        Member member = getMemberByUnivMail(univEmail);
        member.updateUnivRequirementStatus(RequirementStatus.VERIFIED);
    }

    private String getUnivEmailByVerificationCode(String verificationCode) {
        return univEmailVerificationRepository
                .findById(verificationCode)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND))
                .getUnivEmail();
    }

    private Member getMemberByUnivMail(String univEmail) {
        return memberRepository
                .findByUnivEmail(univEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
