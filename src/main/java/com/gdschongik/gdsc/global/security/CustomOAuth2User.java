package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final Long memberId;
    private final MemberRole memberRole;

    public CustomOAuth2User(OAuth2User oAuth2User, Member member) {
        super(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), oAuth2User.getName());
        this.memberId = member.getId();
        this.memberRole = member.getRole();
    }

    public boolean isGuest() {
        return memberRole == MemberRole.GUEST;
    }
}
