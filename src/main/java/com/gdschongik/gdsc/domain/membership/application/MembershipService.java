package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

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
    public void submitMembership(Long recruitmentId) {
        Member currentMember = memberUtil.getCurrentMember();
        Recruitment recruitment = recruitmentRepository
                .findById(recruitmentId)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));
        validateCurrentSemesterMembershipNotExists(currentMember, recruitment);
        validateRecruitmentOpen(recruitment);

        Membership membership = Membership.createMembership(
                currentMember, recruitment.getAcademicYear(), recruitment.getSemesterType());
        membershipRepository.save(membership);
    }

    private void validateRecruitmentOpen(Recruitment recruitment) {
        if (!recruitment.isOpen()) {
            throw new CustomException(RECRUITMENT_NOT_OPEN);
        }
    }

    private void validateCurrentSemesterMembershipNotExists(Member currentMember, Recruitment recruitment) {
        if (membershipRepository.existsByMemberAndAcademicYearAndSemesterType(
                currentMember, recruitment.getAcademicYear(), recruitment.getSemesterType())) {
            throw new CustomException(MEMBERSHIP_ALREADY_APPLIED);
        }
    }
}
