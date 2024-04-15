package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.repository.RepositoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class MemberRepositoryTest extends RepositoryTest {

    private static final String TEST_OAUTH_ID = "testOauthId";
    private static final MemberQueryOption EMPTY_QUERY_OPTION =
            new MemberQueryOption(null, null, null, null, null, null, null);

    @Autowired
    private MemberRepository memberRepository;

    private Member getMember() {
        Member member = Member.createGuestMember(TEST_OAUTH_ID);
        return memberRepository.save(member);
    }

    @Nested
    class 승인_가능_멤버_조회 {

        @Test
        void 가입조건_모두_충족했다면_조회_성공한다() {
            // given
            Member member = getMember();
            member.getRequirement().updateUnivStatus(VERIFIED);
            member.getRequirement().verifyDiscord();
            member.getRequirement().updatePaymentStatus(VERIFIED);
            member.getRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).contains(member);
        }

        @Test
        void 재학생_인증_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getRequirement().verifyDiscord();
            member.getRequirement().updatePaymentStatus(VERIFIED);
            member.getRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void 디스코드_인증_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getRequirement().updateUnivStatus(VERIFIED);
            member.getRequirement().updatePaymentStatus(VERIFIED);
            member.getRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void 회비납부_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getRequirement().updateUnivStatus(VERIFIED);
            member.getRequirement().verifyDiscord();
            member.getRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void Bevy_연동_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getRequirement().updateUnivStatus(VERIFIED);
            member.getRequirement().verifyDiscord();
            member.getRequirement().updatePaymentStatus(VERIFIED);

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }
    }
}
