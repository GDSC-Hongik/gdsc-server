package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.membership.domain.MembershipValidator;
import com.gdschongik.gdsc.domain.recruitment.application.OnboardingRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRoundRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final RecruitmentRoundRepository recruitmentRoundRepository;
    private final MemberUtil memberUtil;
    private final MembershipValidator membershipValidator;
    private final OnboardingRecruitmentService onboardingRecruitmentService;

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

        boolean isMembershipAlreadySubmitted =
                membershipRepository.existsByMemberAndRecruitment(currentMember, recruitmentRound.getRecruitment());

        membershipValidator.validateMembershipSubmit(currentMember, recruitmentRound, isMembershipAlreadySubmitted);

        Membership membership = Membership.createMembership(currentMember, recruitmentRound);
        membershipRepository.save(membership);

        log.info("[MembershipService] 멤버십 가입 신청 접수: membershipId = {}", membership.getId());
    }

    public Optional<Membership> findMyMembership(Member member, RecruitmentRound recruitmentRound) {
        return membershipRepository.findByMemberAndRecruitmentRound(member, recruitmentRound);
    }

    public void deleteMembership(Member member) {
        Optional<RecruitmentRound> currentRecruitmentRoundOpt =
                onboardingRecruitmentService.findCurrentRecruitmentRoundToDemote();

        if (!currentRecruitmentRoundOpt.isPresent()) {
            return;
        }

        RecruitmentRound currentRecruitmentRound = currentRecruitmentRoundOpt.get();
        Optional<Membership> myMembershipOpt = findMyMembership(member, currentRecruitmentRound);

        myMembershipOpt.ifPresent(membershipRepository::delete);
    }
}
