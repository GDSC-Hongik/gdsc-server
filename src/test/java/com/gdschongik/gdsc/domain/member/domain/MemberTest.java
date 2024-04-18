package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.domain.member.domain.MemberStatus.*;
import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemberTest {
    private static final String OAUTH_ID = "testOauthId";
    private static final String UNIV_EMAIL = "test@g.hongik.ac.kr";
    private static final String DISCORD_USERNAME = "testDiscord";
    private static final String NICKNAME = "testNickname";
    private static final String NAME = "김홍익";
    private static final String STUDENT_ID = "C123456";
    private static final String PHONE_NUMBER = "01012345678";
    private static final String MODIFIED_STUDENT_ID = "C123458";

    @Nested
    class 회원가입시 {
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
    }

    @Nested
    class 가입신청시 {
        @Test
        void 재학생인증_되어있으면_성공() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);

            // then
            assertThat(member.getStudentId()).isEqualTo(STUDENT_ID);
        }

        @Test
        void 재학생인증_안되어있으면_실패() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            // when & then
            assertThatThrownBy(() -> {
                        member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(UNIV_NOT_VERIFIED.getMessage());
        }
    }

    @Nested
    class 가입승인시 {
        @Test
        void 회비를_납부하지_않았으면_실패() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();

            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);

            // when & then
            assertThatThrownBy(() -> {
                        member.grant();
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(PAYMENT_NOT_VERIFIED.getMessage());
        }

        @Test
        void 디스코드_인증하지_않았으면_실패() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.updatePaymentStatus(VERIFIED);
            member.verifyBevy();

            member.signup(STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL);

            // when & then
            assertThatThrownBy(() -> {
                        member.grant();
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(DISCORD_NOT_VERIFIED.getMessage());
        }

        @Test
        void Bevy_연동하지_않았으면_실패() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.updatePaymentStatus(VERIFIED);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);

            // when & then
            assertThatThrownBy(() -> {
                        member.grant();
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(BEVY_NOT_VERIFIED.getMessage());
        }

        @Test
        void 회비납부_디스코드인증_Bevy인증_재학생인증하면_성공() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.updatePaymentStatus(VERIFIED);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();

            member.grant();

            // then
            assertThat(member.getRole()).isEqualTo(USER);
        }

        @Test
        void 이미_승인되어있으면_실패() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.updatePaymentStatus(VERIFIED);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();

            member.grant();

            // when & then
            assertThatThrownBy(() -> {
                        member.grant();
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBER_ALREADY_GRANTED.getMessage());
        }
    }

    @Nested
    class 회원탈퇴시 {
        @Test
        void 이미_탈퇴한_유저면_실패() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.withdraw();

            // when & then
            assertThatThrownBy(() -> {
                        member.withdraw();
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBER_DELETED.getMessage());
        }

        @Test
        void 회원탈퇴시_이전에_탈퇴하지_않은_유저면_성공() {
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
        void 탈퇴하지_않은_유저면_성공() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);

            member.updateMemberInfo(
                    MODIFIED_STUDENT_ID, NAME, PHONE_NUMBER, D022, UNIV_EMAIL, DISCORD_USERNAME, NICKNAME);

            // then
            assertThat(member.getStudentId()).isEqualTo(MODIFIED_STUDENT_ID);
        }

        @Test
        void 탈퇴한_유저면_실패() {
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
    void 디스코드인증시_탈퇴한_유저면_실패() {
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
    void 회비납부시_탈퇴한_유저면_실패() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);

        member.withdraw();

        // when & then
        assertThatThrownBy(() -> {
                    member.updatePaymentStatus(VERIFIED);
                })
                .isInstanceOf(CustomException.class)
                .hasMessage(MEMBER_DELETED.getMessage());
    }

    @Test
    void Bevy인증시_탈퇴한_유저면_실패() {
        // given
        Member member = Member.createGuestMember(OAUTH_ID);

        member.withdraw();

        // when & then
        assertThatThrownBy(() -> {
                    member.verifyBevy();
                })
                .isInstanceOf(CustomException.class)
                .hasMessage(MEMBER_DELETED.getMessage());
    }
}
