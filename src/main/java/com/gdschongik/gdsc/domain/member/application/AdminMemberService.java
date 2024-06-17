package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.ExcelUtil;
import java.io.IOException;
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

    public byte[] createExcel() throws IOException {
        return excelUtil.createMemberExcel();
    }
}
