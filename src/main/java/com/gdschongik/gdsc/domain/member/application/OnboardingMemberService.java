package com.gdschongik.gdsc.domain.member.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberSignupRequest;
import com.gdschongik.gdsc.domain.member.dto.request.OnboardingMemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberUnivStatusResponse;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingMemberService {

    private final MemberUtil memberUtil;

    @Transactional
    public void signupMember(MemberSignupRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.signup(
                request.studentId(), request.name(), request.phone(), request.department(), request.email());
    }

    @Transactional
    public void updateMember(OnboardingMemberUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.updateDiscordInfo(request.discordUsername(), request.nickname());
    }

    public MemberInfoResponse getMemberInfo() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberInfoResponse.of(currentMember);
    }

    public MemberUnivStatusResponse checkUnivVerificationStatus() {
        Member currentMember = memberUtil.getCurrentMember();
        return new MemberUnivStatusResponse(currentMember.getUnivStatus());
    }
}
