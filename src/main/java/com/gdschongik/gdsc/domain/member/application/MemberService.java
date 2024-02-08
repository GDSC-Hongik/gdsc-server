package com.gdschongik.gdsc.domain.member.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.request.MemberUpdateRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberFindAllResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<MemberFindAllResponse> findAll(MemberQueryRequest queryRequest, Pageable pageable) {
        Page<Member> members = memberRepository.findAll(queryRequest, pageable);
        return members.map(MemberFindAllResponse::of);
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest request) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateMemberInfo(request);
    }
}
