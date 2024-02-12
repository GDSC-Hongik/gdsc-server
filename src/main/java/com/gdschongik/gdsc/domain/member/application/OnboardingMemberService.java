package com.gdschongik.gdsc.domain.member.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberSignupRequest;
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
}
