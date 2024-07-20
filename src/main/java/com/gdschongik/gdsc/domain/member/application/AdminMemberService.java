package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberDemoteRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberResponse;
import com.gdschongik.gdsc.domain.membership.application.MembershipService;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.application.AdminRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.application.OnboardingRecruitmentService;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.EnvironmentUtil;
import com.gdschongik.gdsc.global.util.ExcelUtil;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final ExcelUtil excelUtil;
    private final AdminRecruitmentService adminRecruitmentService;
    private final MemberUtil memberUtil;
    private final EnvironmentUtil environmentUtil;
    private final MembershipService membershipService;
    private final OnboardingRecruitmentService onboardingRecruitmentService;

    public Page<AdminMemberResponse> searchMembers(MemberQueryOption queryOption, Pageable pageable) {
        Page<Member> members = memberRepository.searchMembers(queryOption, pageable);
        return members.map(AdminMemberResponse::from);
    }

    @Transactional
    public void withdrawMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        member.withdraw();
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest request) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateMemberInfo(
                request.studentId(),
                request.name(),
                request.phone(),
                request.department(),
                request.email(),
                request.discordUsername(),
                request.nickname());
    }

    public byte[] createExcel() throws IOException {
        return excelUtil.createMemberExcel();
    }

    @Transactional
    public void demoteAllRegularMembersToAssociate(MemberDemoteRequest request) {
        adminRecruitmentService.validateRecruitmentNotStarted(request.academicYear(), request.semesterType());
        List<Member> regularMembers = memberRepository.findAllByRole(MemberRole.REGULAR);

        regularMembers.forEach(Member::demoteToAssociate);
        log.info(
                "[AdminMemberService] 정회원 일괄 강등: demotedMemberIds={}",
                regularMembers.stream().map(Member::getId).toList());
    }

    /**
     * 정회원 조건 PENDING으로 변경, 준회원 조건 PENDING으로 변경
     */
    @Transactional
    public void demoteToGuestAndRegularRequirementToPending() {
        validateProfile();
        Member member = memberUtil.getCurrentMember();
        RecruitmentRound currentRecruitmentRound = onboardingRecruitmentService.findCurrentRecruitmentRound();
        Optional<Membership> myMembership = membershipService.findMyMembership(member, currentRecruitmentRound);

        member.demoteToGuest();

        if (myMembership.isPresent()) {
            myMembership.get().demoteRegularRequirementToPending();
        }
        log.info("[AdminMemberService] 게스트로 강등: demotedMemberId={}", member.getId());
    }

    private void validateProfile() {
        if (!environmentUtil.isDevAndLocalProfile()) {
            throw new CustomException(FORBIDDEN);
        }
    }
}
