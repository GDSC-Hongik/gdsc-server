package com.gdschongik.gdsc.domain.member.application.handler;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberRegularEventHandler {
    private final MemberRepository memberRepository;

    public void advanceToRegular(MemberRegularEvent memberRegularEvent) {
        Member currentMember = memberRepository
                .findById(memberRegularEvent.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        try {
            currentMember.advanceToRegular();
        } catch (CustomException e) {
            log.info("{}", e.getErrorCode());
        }
    }
}
