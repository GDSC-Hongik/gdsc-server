package com.gdschongik.gdsc.global.security;

import com.gdschongik.gdsc.domain.auth.dto.AccessTokenDto;
import com.gdschongik.gdsc.domain.member.domain.MemberManageRole;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.MemberStudyRole;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final Long memberId;
    private final MemberRole role;
    private final MemberManageRole manageRole;
    private final MemberStudyRole studyRole;

    public static PrincipalDetails from(AccessTokenDto token) {
        MemberAuthInfo authInfo = token.authInfo();
        return new PrincipalDetails(authInfo.memberId(), authInfo.role(), authInfo.manageRole(), authInfo.studyRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.getValue()));
        authorities.add(new SimpleGrantedAuthority(manageRole.getValue()));
        authorities.add(new SimpleGrantedAuthority(studyRole.getValue()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
