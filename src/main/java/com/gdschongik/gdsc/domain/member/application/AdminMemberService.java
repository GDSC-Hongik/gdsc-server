package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberGrantRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberPaymentRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberFindAllResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberGrantResponse;
import com.gdschongik.gdsc.domain.member.dto.response.MemberPendingFindAllResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
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

    public Page<MemberFindAllResponse> findAll(MemberQueryRequest queryRequest, Pageable pageable) {
        Page<Member> members = memberRepository.findAll(queryRequest, pageable);
        return members.map(MemberFindAllResponse::of);
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

    public Page<MemberPendingFindAllResponse> findAllPendingMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAllByRole(MemberRole.GUEST, pageable);
        return members.map(MemberPendingFindAllResponse::of);
    }

    @Transactional
    public MemberGrantResponse grantMember(MemberGrantRequest request) {
        List<Member> verifiedMembers = getVerifiedMembers(request);
        verifiedMembers.forEach(Member::grant);
        return MemberGrantResponse.of(verifiedMembers);
    }

    private List<Member> getVerifiedMembers(MemberGrantRequest request) {
        List<Long> memberIdList = request.memberIdList();
        return memberIdList.stream()
                .map(memberRepository::findVerifiedById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Page<MemberFindAllResponse> getGrantableMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAllGrantable(pageable);
        return members.map(MemberFindAllResponse::of);
    }

    public Page<MemberFindAllResponse> getMembersByPaymentStatus(RequirementStatus paymentStatus, Pageable pageable) {
        Page<Member> members = memberRepository.findAllByPaymentStatus(paymentStatus, pageable);
        return members.map(MemberFindAllResponse::of);
    }

    @Transactional
    public void verifyPayment(Long memberId, MemberPaymentRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        member.updatePaymentStatus(request.status());
    }
}
