package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.domain.member.domain.MemberRole.ASSOCIATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.gdschongik.gdsc.domain.member.application.handler.MemberAssociateRequirementUpdatedEventHandler;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberAssociateRequirementUpdatedEvent;
import com.gdschongik.gdsc.helper.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MemberIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberAssociateRequirementUpdatedEventHandler memberAssociateRequirementUpdatedEventHandler;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 준회원_승급조건_만족됐으면_MemberRole은_ASSOCIATE이다() {
        // given
        Member member = createMember();

        // when
        memberAssociateRequirementUpdatedEventHandler.advanceToAssociate(
                new MemberAssociateRequirementUpdatedEvent(member.getId()));
        member = memberRepository.save(member);

        // then
        assertThat(member.getRole()).isEqualTo(ASSOCIATE);
    }
}
