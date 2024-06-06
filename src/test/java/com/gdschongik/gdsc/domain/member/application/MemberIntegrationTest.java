package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.domain.member.domain.Department.D022;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.ASSOCIATE;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.gdschongik.gdsc.domain.member.application.handler.MemberAssociateEventHandler;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import com.gdschongik.gdsc.integration.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MemberIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberAssociateEventHandler memberAssociateEventHandler;

    @Test
    void 준회원_승급조건_만족됐으면_MemberRole은_ASSOCIATE이다() {
        // when
        Member member = Member.createGuestMember(OAUTH_ID);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        memberAssociateEventHandler.handle(new MemberAssociateEvent(member));

        // then
        assertThat(member.getRole()).isEqualTo(ASSOCIATE);
    }
}
