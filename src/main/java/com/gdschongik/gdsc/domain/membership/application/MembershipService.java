package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentRoundRepository recruitmentRoundRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public void verifyPaymentStatus(Long membershipId) {
        Membership currentMembership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new CustomException(MEMBERSHIP_NOT_FOUND));

        currentMembership.verifyPaymentStatus();
    }

    @Transactional
    public void submitMembership(Long recruitmentRoundId) {
        Member currentMember = memberUtil.getCurrentMember();

        RecruitmentRound recruitmentRound = recruitmentRoundRepository
                .findById(recruitmentRoundId)
                .orElseThrow(() -> new CustomException(RECRUITMENT_ROUND_NOT_FOUND));

        validateMembershipDuplicate(
                currentMember, recruitmentRound.getAcademicYear(), recruitmentRound.getSemesterType());
        validateRecruitmentRoundOpen(recruitmentRound);

        Membership membership = Membership.createMembership(currentMember, recruitmentRound);
        membershipRepository.save(membership);
    }

    private void validateRecruitmentRoundOpen(RecruitmentRound recruitmentRound) {
        if (!recruitmentRound.isOpen()) {
            throw new CustomException(RECRUITMENT_ROUND_NOT_OPEN);
        }
    }

    private void validateMembershipDuplicate(Member currentMember, Integer academicYear, SemesterType semesterType) {
        membershipRepository
                .findByMemberAndAcademicYearAndSemesterType(currentMember, academicYear, semesterType)
                .ifPresent(membership -> {
                    if (membership.isRegularRequirementAllSatisfied()) {
                        throw new CustomException(MEMBERSHIP_ALREADY_SATISFIED);
                    }
                    throw new CustomException(MEMBERSHIP_ALREADY_APPLIED);
                });
    }

    public Optional<Membership> findMyMembership(Member member, RecruitmentRound recruitmentRound) {
        return membershipRepository.findByMemberAndRecruitmentRound(member, recruitmentRound);
    }
}
