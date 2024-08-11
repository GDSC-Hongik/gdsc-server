package com.gdschongik.gdsc.domain.member.application.handler;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAssociateEventHandler {

    private final MemberRepository memberRepository;

    public void advanceToAssociate(MemberAssociateEvent memberAssociateEvent) {
        Member member = memberRepository
                .findById(memberAssociateEvent.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.isAdvanceableToAssociate()) {
            member.advanceToAssociate();
            log.info("[MemberAssociateEventHandler] 준회원 승급 완료: memberId={}", member.getId());
        } else {
            log.debug("[MemberAssociateEventHandler] 준회원 승급 시도 실패: memberId={}", member.getId());
        }
    }
}
