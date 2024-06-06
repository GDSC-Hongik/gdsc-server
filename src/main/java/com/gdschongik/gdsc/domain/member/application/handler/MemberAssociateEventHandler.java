package com.gdschongik.gdsc.domain.member.application.handler;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAssociateEventHandler {
    public void handle(Object context) {
        MemberAssociateEvent event = (MemberAssociateEvent) context;
        if (validateAdvanceAvailable(event.member())) {
            advanceToAssociate(event.member());
        }
    }

    private void advanceToAssociate(Member member) {
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
