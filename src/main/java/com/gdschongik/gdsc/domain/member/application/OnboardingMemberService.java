package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.BasicMemberInfoRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberSignupRequest;
import com.gdschongik.gdsc.domain.member.dto.request.OnboardingMemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberInfoResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberUnivStatusResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingMemberService {

    private final MemberUtil memberUtil;
    private final MemberRepository memberRepository;

    @Transactional
    public void signupMember(MemberSignupRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.signup(
                request.studentId(), request.name(), request.phone(), request.department(), request.email());
    }

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
        Member currentMember = memberUtil.getCurrentMember();
        if (!currentMember.isApplied()) {
            throw new CustomException(MEMBER_NOT_APPLIED);
        }
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
}
