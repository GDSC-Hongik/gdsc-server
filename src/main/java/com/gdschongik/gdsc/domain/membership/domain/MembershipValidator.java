package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MembershipValidator {

    private final MembershipRepository membershipRepository;

    public void validateMembershipSubmit(Member currentMember, RecruitmentRound recruitmentRound) {
        validateMembershipDuplicate(currentMember, recruitmentRound.getRecruitment());
        validateRecruitmentRoundOpen(recruitmentRound);
    }

    private void validateMembershipDuplicate(Member currentMember, Recruitment recruitment) {
        membershipRepository
                .findByMember(currentMember)
                .filter(membership ->
                        membership.getRecruitmentRound().getRecruitment().equals(recruitment))
                .ifPresent(membership -> {
                    if (membership.isRegularRequirementAllSatisfied()) {
                        throw new CustomException(MEMBERSHIP_ALREADY_SATISFIED);
                    }
                    throw new CustomException(MEMBERSHIP_ALREADY_SUBMITTED);
                });
    }

    private void validateRecruitmentRoundOpen(RecruitmentRound recruitmentRound) {
        if (!recruitmentRound.isOpen()) {
            throw new CustomException(RECRUITMENT_ROUND_NOT_OPEN);
        }
    }
}
