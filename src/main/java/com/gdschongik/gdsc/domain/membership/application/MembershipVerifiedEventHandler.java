package com.gdschongik.gdsc.domain.membership.application;

import com.gdschongik.gdsc.domain.member.application.CommonMemberService;
import com.gdschongik.gdsc.domain.membership.domain.MembershipVerifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembershipVerifiedEventHandler {

    private final CommonMemberService commonMemberService;

    @EventListener
    public void handleMembershipVerifiedEvent(MembershipVerifiedEvent event) {
        log.info("[MembershipVerifiedEventHandler] 멤버십 인증 이벤트 수신: membershipId={}", event.membershipId());
        commonMemberService.advanceMemberToRegularByMembership(event.membershipId());
    }
}
