package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public void advanceToVerified(Long membershipId) {
        Membership currentMembership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new CustomException(MEMBERSHIP_NOT_FOUND));

        currentMembership.validateAdvanceRequirement();
        currentMembership.verifyPaymentStatus();
    }

    @Transactional
    public void submitMembership(Long recruitmentId) {
        Member currentMember = memberUtil.getCurrentMember();
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));
        validateMembershipDuplicate(currentMember, recruitment.getAcademicYear(), recruitment.getSemesterType());
        validateRecruitmentOpen(recruitment);

        Membership membership = Membership.createMembership(currentMember, recruitment);
        membershipRepository.save(membership);
    }

    private void validateRecruitmentOpen(Recruitment recruitment) {
        if (!recruitment.isOpen()) {
            throw new CustomException(RECRUITMENT_NOT_OPEN);
        }
    }

    private void validateMembershipDuplicate(Member currentMember, Integer academicYear, SemesterType semesterType) {
        membershipRepository
                .findByMemberAndAcademicYearAndSemesterType(currentMember, academicYear, semesterType)
                .ifPresent(membership -> {
                    if (membership.isAdvanceRequirementAllSatisfied()) {
                        throw new CustomException(MEMBERSHIP_ALREADY_VERIFIED);
                    }
                    throw new CustomException(MEMBERSHIP_ALREADY_APPLIED);
                });
    }
}
