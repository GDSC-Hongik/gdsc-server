package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
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

    private static final MemberQueryOption EMPTY_QUERY_OPTION =
            new MemberQueryOption(null, null, null, null, null, null, null);

    @Autowired
    private MemberRepository memberRepository;

    private Member getMember() {
        Member member = Member.createGuestMember(OAUTH_ID);
        return memberRepository.save(member);
    }

    private void flushAndClearBeforeExecute() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Nested
    class 준회원_승급가능_멤버를_조회할때 {

        @Test
        void 준회원_승급조건_모두_충족했다면_조회_성공한다() {
            // given
            Member member = getMember();
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.getAssociateRequirement().updateUnivStatus(VERIFIED);
            member.getAssociateRequirement().verifyDiscord();
            member.getAssociateRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).contains(member);
        }

        @Test
        void 재학생_인증_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getAssociateRequirement().verifyDiscord();
            member.getAssociateRequirement().updatePaymentStatus(VERIFIED);
            member.getAssociateRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void 디스코드_인증_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getAssociateRequirement().updateUnivStatus(VERIFIED);
            member.getAssociateRequirement().updatePaymentStatus(VERIFIED);
            member.getAssociateRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void 회비납부_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getAssociateRequirement().updateUnivStatus(VERIFIED);
            member.getAssociateRequirement().verifyDiscord();
            member.getAssociateRequirement().verifyBevy();

            // when
            Page<Member> members = memberRepository.findAllGrantable(EMPTY_QUERY_OPTION, PageRequest.of(0, 10));

            // then
            assertThat(members).doesNotContain(member);
        }

        @Test
        void Bevy_연동_미완료시_조회되지_않는다() {
            // given
            Member member = getMember();
            member.getAssociateRequirement().updateUnivStatus(VERIFIED);
            member.getAssociateRequirement().verifyDiscord();
            member.getAssociateRequirement().updatePaymentStatus(VERIFIED);

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

        @Test
        void 기본_회원정보_작성후_준회원_승급전_이라면_GUEST로_조회된다() {
            // given
            Member member = getMember();
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);

            flushAndClearBeforeExecute();

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), GUEST);

            // then
            Member guest = memberRepository.findById(1L).get();
            assertThat(members).contains(guest);
        }

        @Test
        void 기본_회원정보_작성후_준회원_승급후라면_ASSOCIATE로_조회된다() {
            // given
            Member member = getMember();
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.advanceToAssociate();

            flushAndClearBeforeExecute();

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), ASSOCIATE);

            // then
            Member user = memberRepository.findById(1L).get();
            assertThat(members).contains(user);
        }

        @Test
        void 기본_회원정보_작성후_준회원_승급후라면_GUEST로_조회되지_않는다() {
            // given
            Member member = getMember();
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.advanceToAssociate();

            flushAndClearBeforeExecute();

            // when
            Page<Member> members = memberRepository.findAllByRole(EMPTY_QUERY_OPTION, PageRequest.of(0, 10), GUEST);

            // then
            Member user = memberRepository.findById(1L).get();
            assertThat(members).doesNotContain(user);
        }
    }
}
