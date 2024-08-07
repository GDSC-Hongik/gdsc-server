package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.GITHUB_NAME_ATTR_KEY;

import com.gdschongik.gdsc.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final MemberAuthInfo memberAuthInfo;

    public CustomOAuth2User(OAuth2User oAuth2User, Member member) {
        super(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), GITHUB_NAME_ATTR_KEY);
        memberAuthInfo = MemberAuthInfo.from(member);
    }
}
