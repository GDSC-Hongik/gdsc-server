package com.gdschongik.gdsc.domain.membership.application;

import com.gdschongik.gdsc.domain.member.application.CommonMemberService;
import com.gdschongik.gdsc.domain.membership.domain.MembershipPaymentRevokedEvent;
import com.gdschongik.gdsc.domain.membership.domain.MembershipVerifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembershipEventHandler {

    private final CommonMemberService commonMemberService;

    @EventListener
    public void handleMembershipVerifiedEvent(MembershipVerifiedEvent event) {
        log.info("[MembershipEventHandler] 멤버십 인증 이벤트 수신: membershipId={}", event.membershipId());
        commonMemberService.advanceMemberToRegularByMembership(event.membershipId());
    }

    @EventListener
    public void handleMembershipPaymentRevokedEvent(MembershipPaymentRevokedEvent event) {
        log.info("[MembershipEventHandler] 멤버십 회비납입 취소 이벤트 수신: membershipId={}", event.membershipId());
        commonMemberService.demoteMemberToAssociateByMembership(event.membershipId());
    }
}
