package com.gdschongik.gdsc.domain.member.application;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.response.AdminMemberFindAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<AdminMemberFindAllResponse> findAll(MemberQueryRequest queryRequest, Pageable pageable) {
        Page<Member> members = memberRepository.findAll(queryRequest, pageable);
        return members.map(AdminMemberFindAllResponse::of);
    }
}
