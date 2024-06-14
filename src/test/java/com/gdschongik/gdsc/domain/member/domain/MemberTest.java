package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.ASSOCIATE;
import static com.gdschongik.gdsc.domain.member.domain.MemberStatus.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Nested
    class 게스트_회원가입시 {

        @Test
        void MemberRole은_GUEST이다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            MemberRole role = member.getRole();

            // then
            assertThat(role).isEqualTo(MemberRole.GUEST);
        }

        @Test
        void MemberStatus는_NORMAL이다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            MemberStatus status = member.getStatus();

            // then
            assertThat(status).isEqualTo(MemberStatus.NORMAL);
        }

        @Test
        void 모든_준회원_가입조건은_인증되지_않은_상태이다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            AssociateRequirement requirement = member.getAssociateRequirement();

            // then
            assertThat(requirement.getUnivStatus()).isEqualTo(PENDING);
            assertThat(requirement.getDiscordStatus()).isEqualTo(PENDING);
            assertThat(requirement.getBevyStatus()).isEqualTo(PENDING);
            assertThat(requirement.getInfoStatus()).isEqualTo(PENDING);
        }
    }

    @Nested
    class 준회원_가입조건_인증시도시 {

        @Test
        void 기본회원정보_작성시_준회원_가입조건중_기본정보_인증상태가_인증된다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);

            // then
            AssociateRequirement requirement = member.getAssociateRequirement();
            assertThat(requirement.getInfoStatus()).isEqualTo(VERIFIED);
        }

        @Test
        void 재학생이메일_인증시_준회원_가입조건중_재학생이메일_인증상태가_인증된다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            member.completeUnivEmailVerification(UNIV_EMAIL);

            // then
            AssociateRequirement requirement = member.getAssociateRequirement();
            assertThat(requirement.getUnivStatus()).isEqualTo(VERIFIED);
        }

        @Test
        void 디스코드_인증시_준회원_가입조건중_디스코드_인증상태가_인증된다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);

            // then
            AssociateRequirement requirement = member.getAssociateRequirement();
            assertThat(requirement.getDiscordStatus()).isEqualTo(VERIFIED);
        }

        @Test
        void Bevy_인증시_준회원_가입조건중_Bevy_인증상태가_인증된다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            member.verifyBevy();

            // then
            AssociateRequirement requirement = member.getAssociateRequirement();
            assertThat(requirement.getBevyStatus()).isEqualTo(VERIFIED);
        }
    }

    @Nested
    class 준회원으로_승급시 {

        @Test
        void 기본_회원정보_작성하지_않았으면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyBevy();

            // when & then
            assertThatThrownBy(member::advanceToAssociate)
                    .isInstanceOf(CustomException.class)
                    .hasMessage(BASIC_INFO_NOT_VERIFIED.getMessage());
        }

        @Test
        void 디스코드_인증하지_않았으면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyBevy();

            // when & then
            assertThatThrownBy(member::advanceToAssociate)
                    .isInstanceOf(CustomException.class)
                    .hasMessage(DISCORD_NOT_VERIFIED.getMessage());
        }

        @Test
        void Bevy_연동하지_않았으면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);

            // when & then
            assertThatThrownBy(member::advanceToAssociate)
                    .isInstanceOf(CustomException.class)
                    .hasMessage(BEVY_NOT_VERIFIED.getMessage());
        }

        @Test
        void 기본_회원정보_작성_디스코드인증_Bevy인증_재학생인증하면_성공한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();

            // when
            member.advanceToAssociate();

            // then
            assertThat(member.getRole()).isEqualTo(ASSOCIATE);
        }

        @Test
        void 이미_준회원으로_승급_돼있으면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.advanceToAssociate();

            // when & then
            assertThatThrownBy(member::advanceToAssociate)
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBER_ALREADY_ASSOCIATE.getMessage());
        }
    }

    @Nested
    class 회원탈퇴시 {

        @Test
        void 이미_탈퇴한_유저면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.withdraw();

            // when & then
            assertThatThrownBy(member::withdraw)
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBER_DELETED.getMessage());
        }

        @Test
        void 회원탈퇴시_이전에_탈퇴하지_않은_유저면_성공한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            member.withdraw();

            // then
            assertThat(member.getStatus()).isEqualTo(DELETED);
        }
    }

    @Nested
    class 회원수정시 {
        @Test
        void 탈퇴하지_않은_유저면_성공한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.updateMemberInfo(
                    MODIFIED_STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL, DISCORD_USERNAME, NICKNAME);

            // then
            assertThat(member.getStudentId()).isEqualTo(MODIFIED_STUDENT_ID);
        }

        @Test
        void 탈퇴한_유저면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.withdraw();

            // when & then
            assertThatThrownBy(() -> {
                        member.updateMemberInfo(
                                MODIFIED_STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL, DISCORD_USERNAME, NICKNAME);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBER_DELETED.getMessage());
        }
    }

    @Test
    void 디스코드인증시_탈퇴한_유저면_실패한다() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);

        member.withdraw();

        // when & then
        assertThatThrownBy(() -> {
                    member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
                })
                .isInstanceOf(CustomException.class)
                .hasMessage(MEMBER_DELETED.getMessage());
    }

    @Test
    void Bevy인증시_탈퇴한_유저면_실패한다() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);

        member.withdraw();

        // when & then
        assertThatThrownBy(member::verifyBevy)
                .isInstanceOf(CustomException.class)
                .hasMessage(MEMBER_DELETED.getMessage());
    }
}
