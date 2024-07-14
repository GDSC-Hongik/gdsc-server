package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MembershipValidator {

    public void validateMembershipSubmit(
            Member currentMember, RecruitmentRound recruitmentRound, boolean isMembershipAlreadySubmitted) {
        // 준회원인지 검증
        // TODO: 어드민인 경우 리쿠르팅 지원 및 결제에 대한 정책 검토 필요. 현재는 불가능하도록 설정
        if (!currentMember.isAssociate()) {
            throw new CustomException(MEMBERSHIP_NOT_APPLICABLE);
        }

        // 이미 접수한 멤버십이 있는지 검증
        if (isMembershipAlreadySubmitted) {
            throw new CustomException(MEMBERSHIP_ALREADY_SUBMITTED);
        }

        // 모집 회차가 열려있는지 검증
        if (!recruitmentRound.isOpen()) {
            throw new CustomException(MEMBERSHIP_RECRUITMENT_ROUND_NOT_OPEN);
        }
    }
}
