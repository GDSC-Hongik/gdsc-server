package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
@RequiredArgsConstructor
public class CustomUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Member member = fetchOrCreate(oAuth2User);
        member.updateLastLoginAt();
        memberRepository.save(member);

        return new CustomOAuth2User(oAuth2User, member);
    }

    private Member fetchOrCreate(OAuth2User oAuth2User) {
        Member member =
                memberRepository.findNormalByOauthId(oAuth2User.getName()).orElseGet(() -> registerMember(oAuth2User));

        String githubHandle = oAuth2User.getAttribute("login");
        if (member.getGithubHandle().equals(githubHandle)) {
            return member;
        }

        member.updateGithubHandle(githubHandle);
        return member;
    }

    private Member registerMember(OAuth2User oAuth2User) {
        Member guest = Member.createGuestMember(oAuth2User.getName(), oAuth2User.getAttribute("login"));
        return memberRepository.save(guest);
    }
}
