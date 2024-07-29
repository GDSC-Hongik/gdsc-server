package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.application.handler.MemberRegularEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberRegularEventListener {
    private final MemberRegularEventHandler memberRegularEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MemberRegularEvent.class)
    public void handleMemberAssociateEvent(MemberRegularEvent memberRegularEvent) {
        log.info("[MemberRegularEventListener] 정회원 승급 시도 이벤트 수신: memberId={}", memberRegularEvent.memberId());
        memberRegularEventHandler.advanceToRegular(memberRegularEvent);
    }
}
