package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.auth.dto.RefreshTokenDto;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberTokenByGithubHandleRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberTokenResponse;
import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import com.gdschongik.gdsc.global.annotation.ConditionalOnProfile;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.security.MemberAuthInfo;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProfile({LOCAL, DEV})
public class TestMemberService {

    private final MembershipService membershipService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final GithubClient githubClient;
    private final MemberUtil memberUtil;

    @Transactional
    public void createTestMember(String githubHandle) {
        String githubOauthId = githubClient.getOauthId(githubHandle);

        if (memberRepository.findByOauthId(githubOauthId).isPresent()) {
            throw new CustomException(INTERNAL_ERROR);
        }

        Member guestMember = Member.createGuest(githubOauthId);
        memberRepository.save(guestMember);
    }

    public MemberTokenResponse createTemporaryTokenByGithubHandle(MemberTokenByGithubHandleRequest request) {
        final String oauthId = githubClient.getOauthId(request.githubHandle());

        final Member member =
                memberRepository.findByOauthId(oauthId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        AccessTokenDto accessTokenDto = jwtService.createAccessToken(MemberAuthInfo.from(member));
        RefreshTokenDto refreshTokenDto = jwtService.createRefreshToken(member.getId());

        return new MemberTokenResponse(accessTokenDto.tokenValue(), refreshTokenDto.tokenValue());
    }

    @Transactional
    public void demoteToGuestAndRegularRequirementToUnsatisfied() {
        Member member = memberUtil.getCurrentMember();
        member.demoteToGuest();

        membershipService.deleteMembership(member);

        log.info("[TestMemberService] 게스트로 강등: demotedMemberId={}", member.getId());
    }
}
