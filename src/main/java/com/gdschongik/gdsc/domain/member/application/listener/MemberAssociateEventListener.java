package com.gdschongik.gdsc.domain.member.application.listener;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAssociateEventListener {

    private final MemberUtil memberUtil;

    @TransactionalEventListener(MemberAssociateEvent.class)
    public void handleMemberAssociateEvent(MemberAssociateEvent event) {
        Member member = memberUtil.getCurrentMember();
        if(validateAdvanceAvailable(member)) {
            advanceToAssociate(member);
        }
    }


    public void advanceToAssociate(Member member) {
        member.advanceToAssociate();
    }

    private boolean validateAdvanceAvailable(Member member) {
        if (isAtLeastAssociate(member.getRole())) {
            return false;
        }

        if (member.isAllVerified()) {
            return true;
        }

        return false;
    }

    private boolean isAtLeastAssociate(MemberRole role) {
        return role.equals(ASSOCIATE) || role.equals(ADMIN) || role.equals(REGULAR);
    }


}
