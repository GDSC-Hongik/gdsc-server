package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.domain.member.domain.Department.D022;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.ASSOCIATE;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.REGULAR;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.gdschongik.gdsc.domain.member.application.handler.MemberAssociateEventHandler;
import com.gdschongik.gdsc.domain.member.application.handler.MemberRegularEventHandler;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateEvent;
import com.gdschongik.gdsc.domain.member.domain.MemberRegularEvent;
import com.gdschongik.gdsc.integration.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MemberIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberAssociateEventHandler memberAssociateEventHandler;

    @Autowired
    private MemberRegularEventHandler memberRegularEventHandler;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 준회원_승급조건_만족됐으면_MemberRole은_ASSOCIATE이다() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();

        // when
        memberAssociateEventHandler.advanceToAssociate(new MemberAssociateEvent(member.getId()));
        member = memberRepository.save(member);

        // then
        assertThat(member.getRole()).isEqualTo(ASSOCIATE);
    }

    @Test
    void 정회원_승급조건_만족됐으면_MemberRole은_REGULAR이다() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();

        // when
        memberRegularEventHandler.advanceToRegular(new MemberRegularEvent(member.getId(), DISCORD_USERNAME));

        // then
        assertThat(member.getRole()).isEqualTo(REGULAR);
    }
}
