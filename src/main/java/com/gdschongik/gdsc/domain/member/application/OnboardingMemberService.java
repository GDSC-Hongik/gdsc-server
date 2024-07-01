package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.BasicMemberInfoRequest;
import com.gdschongik.gdsc.domain.member.dto.request.OnboardingMemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberBasicInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberDashboardResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberUnivStatusResponse;
import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.application.OnboardingRecruitmentService;
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
public class OnboardingMemberService {

    private final MemberUtil memberUtil;
    private final OnboardingRecruitmentService onboardingRecruitmentService;
    private final MembershipService membershipService;
    private final MemberRepository memberRepository;

    @Deprecated
    @Transactional
    public void updateMember(OnboardingMemberUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        validateDiscordUsernameDuplicate(currentMember);
        currentMember.verifyDiscord(request.discordUsername(), request.nickname());
    }

    private void validateDiscordUsernameDuplicate(Member member) {
        if (memberRepository.existsByDiscordUsername(member.getDiscordUsername())) {
            throw new CustomException(MEMBER_DISCORD_USERNAME_DUPLICATE);
        }
    }

    public MemberInfoResponse getMemberInfo() {
        // TODO: 대시보드 API로 통합
        Member currentMember = memberUtil.getCurrentMember();
        return MemberInfoResponse.of(currentMember);
    }

    public MemberUnivStatusResponse checkUnivVerificationStatus() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberUnivStatusResponse.from(currentMember);
    }

    @Transactional
    public void verifyBevyStatus() {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.verifyBevy();
    }

    @Transactional
    public void updateBasicMemberInfo(BasicMemberInfoRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.updateBasicMemberInfo(
                request.studentId(), request.name(), request.phone(), request.department(), request.email());
    }

    public MemberBasicInfoResponse getMemberBasicInfo() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberBasicInfoResponse.from(currentMember);
    }

    public MemberDashboardResponse getDashboard() {
        Member currentMember = memberUtil.getCurrentMember();
        RecruitmentRound currentRecruitmentRound = onboardingRecruitmentService.findCurrentRecruitment();
        Optional<Membership> myMembership = membershipService.findMyMembership(currentMember, currentRecruitmentRound);

        return MemberDashboardResponse.from(currentMember, currentRecruitmentRound, myMembership.orElse(null));
    }
}
