package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.gdschongik.gdsc.repository.RepositoryTest;
import java.util.List;
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
    class 승인_가능_멤버를_조회할때 {

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

    @Nested
    class 멤버_상태로_조회할때 {
        @Test
        void NORMAL이라면_조회_성공한다() {
            // given
            Member member = getMember();

            // when
            List<Member> members = memberRepository.findAll();

            // then
            assertThat(members).contains(member);
        }

        @Test
        void DELETED라면_조회되지_않는다() {
            // given
            Member member = getMember();
            member.withdraw();

            // when
            List<Member> members = memberRepository.findAll();

            // then
            assertThat(members).doesNotContain(member);
        }
    }

    @Nested
    class 역할로_조회할때 {
        private static final String UNIV_EMAIL = "test@g.hongik.ac.kr";
        private static final String DISCORD_USERNAME = "testDiscord";
        private static final String NICKNAME = "testNickname";
        private static final String NAME = "김홍익";
        private static final String STUDENT_ID = "C123456";
        private static final String PHONE_NUMBER = "01012345678";

        @Test
        void 승인전이라면_GUEST로_조회된다() {
            // given
            Member member = getMember();
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), GUEST);

            // then
            assertThat(members).contains(member);
        }

        @Test
        void 승인전이라면_USER로_조회되지_않는다() {
            // given
            Member member = getMember();
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), USER);

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void 승인후라면_USER로_조회된다() {
            // given
            Member member = getMember();
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);
            member.updatePaymentStatus(VERIFIED);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.grant();

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), USER);

            // then
            assertThat(members).contains(member);
        }

        @Test
        void 승인후라면_GUEST로_조회되지_않는다() {
            // given
            Member member = getMember();
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);
            member.updatePaymentStatus(VERIFIED);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.grant();

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), GUEST);

            // then
            assertThat(members).doesNotContain(member);
        }
    }
}
