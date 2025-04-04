package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.email.application.UnivEmailVerificationService;
import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import com.gdschongik.gdsc.domain.email.domain.service.EmailVerificationStatusService;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.UnivVerificationStatus;
import com.gdschongik.gdsc.domain.member.dto.request.BasicMemberInfoRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberTokenRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberBasicInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberDashboardResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberTokenResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberUnivStatusResponse;
import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.application.OnboardingRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.security.MemberAuthInfo;
import com.gdschongik.gdsc.global.util.EnvironmentUtil;
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
    private final UnivEmailVerificationService univEmailVerificationService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final EnvironmentUtil environmentUtil;
    private final EmailVerificationStatusService emailVerificationStatusService;

    public MemberUnivStatusResponse checkUnivVerificationStatus() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberUnivStatusResponse.from(currentMember);
    }

    @Transactional
    public void updateBasicMemberInfo(BasicMemberInfoRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.updateBasicMemberInfo(
                request.studentId(), request.name(), request.phone(), request.department(), request.email());
        memberRepository.save(currentMember);
    }

    public MemberBasicInfoResponse getMemberBasicInfo() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberBasicInfoResponse.from(currentMember);
    }

    public MemberDashboardResponse getDashboard() {
        final Member member = memberUtil.getCurrentMember();
        final Optional<UnivEmailVerification> univEmailVerification =
                univEmailVerificationService.getUnivEmailVerificationFromRedis(member.getId());
        UnivVerificationStatus univVerificationStatus =
                emailVerificationStatusService.determineStatus(member, univEmailVerification);
        Optional<RecruitmentRound> currentRecruitmentRound = onboardingRecruitmentService.findCurrentRecruitmentRound();
        Optional<Membership> myMembership = currentRecruitmentRound.flatMap(
                recruitmentRound -> membershipService.findMyMembership(member, recruitmentRound));

        return MemberDashboardResponse.of(
                member, univVerificationStatus, currentRecruitmentRound.orElse(null), myMembership.orElse(null));
    }

    public MemberTokenResponse createTemporaryToken(MemberTokenRequest request) {
        validateProfile();

        final Member member = memberRepository
                .findByOauthId(request.oauthId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        AccessTokenDto accessTokenDto = jwtService.createAccessToken(MemberAuthInfo.from(member));
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(member.getId());

        return new MemberTokenResponse(accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());
    }

    private void validateProfile() {
        if (!environmentUtil.isDevAndLocalProfile()) {
            throw new CustomException(FORBIDDEN);
        }
    }
}
