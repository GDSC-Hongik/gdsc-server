package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.application.handler.MemberRegularEventHandler;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberRegularEventListener {
    private final MemberRegularEventHandler memberRegularEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MemberRegularEvent.class)
    public void handleMemberAssociateEvent(MemberRegularEvent memberRegularEvent) {
        memberRegularEventHandler.advanceToRegular(memberRegularEvent);
    }
}
