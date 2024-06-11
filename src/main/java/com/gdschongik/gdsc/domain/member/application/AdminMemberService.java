package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberGrantRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberPaymentRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberGrantResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.ExcelUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final ExcelUtil excelUtil;

    public Page<AdminMemberResponse> findAll(MemberQueryOption queryOption, Pageable pageable) {
        Page<Member> members = memberRepository.findAllByRole(queryOption, pageable, null);
        return members.map(AdminMemberResponse::from);
    }

    public Page<AdminMemberResponse> findAllByRole(
            MemberQueryOption queryOption, Pageable pageable, MemberRole memberRole) {
        Page<Member> members = memberRepository.findAllByRole(queryOption, pageable, memberRole);
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

    public Page<AdminMemberResponse> findAllPendingMembers(MemberQueryOption queryOption, Pageable pageable) {
        Page<Member> members = memberRepository.findAllByRole(queryOption, pageable, GUEST);
        return members.map(AdminMemberResponse::from);
    }

    @Transactional
    public MemberGrantResponse grantMember(MemberGrantRequest request) {
        Map<Boolean, List<Member>> classifiedMember = memberRepository.groupByVerified(request.memberIdList());
        List<Member> verifiedMembers = classifiedMember.get(true);
        verifiedMembers.forEach(Member::grant);
        memberRepository.saveAll(verifiedMembers); // explicitly save to publish event
        return MemberGrantResponse.from(classifiedMember);
    }

    public Page<AdminMemberResponse> getGrantableMembers(MemberQueryOption queryOption, Pageable pageable) {
        Page<Member> members = memberRepository.findAllGrantable(queryOption, pageable);
        return members.map(AdminMemberResponse::from);
    }

    public Page<AdminMemberResponse> getMembersByPaymentStatus(
            MemberQueryOption queryOption, RequirementStatus paymentStatus, Pageable pageable) {
        Page<Member> members = memberRepository.findAllByPaymentStatus(queryOption, paymentStatus, pageable);
        return members.map(AdminMemberResponse::from);
    }

    @Transactional
    public void updatePaymentStatus(Long memberId, MemberPaymentRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        member.updatePaymentStatus(request.status());
    }

    public Page<AdminMemberResponse> findAllGrantedMembers(MemberQueryOption queryOption, Pageable pageable) {
        Page<Member> members = memberRepository.findAllByRole(queryOption, pageable, USER);
        return members.map(AdminMemberResponse::from);
    }

    public byte[] createExcel() throws IOException {
        return excelUtil.createMemberExcel();
    }
}
