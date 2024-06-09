package com.gdschongik.gdsc.domain.member.application.handler;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAssociateEventHandler {
    private final MemberRepository memberRepository;

    public void advanceToAssociate(MemberAssociateEvent memberAssociateEvent) {
        MemberAssociateEvent event = memberAssociateEvent;
        Member member = memberRepository
                .findById(event.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (validateAdvanceAvailable(member)) {
            member.advanceToAssociate();
        }
    }

    private boolean validateAdvanceAvailable(Member member) {
        if (member.isAtLeastAssociate(member.getRole())) {
            return false;
        }

        if (member.getRequirement().isAllVerified()) {
            return true;
        }

        return false;
    }
}
