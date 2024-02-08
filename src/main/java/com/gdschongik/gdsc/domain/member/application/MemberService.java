package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.auth.dao.RefreshTokenRepository;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberFindAllResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
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
    private final RefreshTokenRepository refreshTokenRepository;

    public Page<MemberFindAllResponse> findAll(MemberQueryRequest queryRequest, Pageable pageable) {
        Page<Member> members = memberRepository.findAll(queryRequest, pageable);
        return members.map(MemberFindAllResponse::of);
    }

    @Transactional
    public void withdrawMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        member.softDelete();
    }
}
