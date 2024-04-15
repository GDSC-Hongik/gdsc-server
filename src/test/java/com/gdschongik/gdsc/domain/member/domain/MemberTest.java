package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.D022;
import static org.assertj.core.api.Assertions.*;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;

class MemberTest {
    @Test
    void 회원가입시_MemberRole은_GUEST이다() {
        // given
        Member member = Member.createGuestMember("testOauthId");

        // when
        MemberRole role = member.getRole();

        // then
        assertThat(role).isEqualTo(MemberRole.GUEST);
    }

    @Test
    void 회원가입시_MemberStatus는_NORMAL이다() {
        // given
        Member member = Member.createGuestMember("testOauthId");

        // when
        MemberStatus status = member.getStatus();

        // then
        assertThat(status).isEqualTo(MemberStatus.NORMAL);
    }

    @Test
    void 회원가입시_재학생인증_되어있으면_성공() {
        //given
        Member member = Member.createGuestMember("testOauthId");

        //when
        member.completeUnivEmailVerification("test@g.hongik.ac.kr");
        member.signup("C123456","김홍익","01012345678",D022,"test@g.hongik.ac.kr");

        //then
        assertThatNoException();
    }

    @Test
    void 회원가입시_재학생인증_안되어있으면_실패() {
        //given
        Member member = Member.createGuestMember("testOauthId");

        //then
        assertThatThrownBy(() -> {
            member.signup("C123456","김홍익","01012345678",D022,"test@g.hongik.ac.kr");
        }).isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_NOT_VERIFIED.getMessage());

    }

    @Test
    void grant할때_회비를_납부하지_않았으면_에러_반환() {
        //given
        Member guestMember = Member.createGuestMember("testOauthId");

        //when
        guestMember.verifyBevy();
        guestMember.verifyDiscord("testDiscord","testNickname");
        guestMember.completeUnivEmailVerification("test@g.hongik.ac.kr");
        guestMember.signup("C123456","김홍익","01012345678",D022,"test@g.hongik.ac.kr");

        //when & then
        assertThatThrownBy(() -> {
            guestMember.grant();
        }).isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PAYMENT_NOT_VERIFIED.getMessage());
    }

    @Test
    void grant할때_디스코드_인증하지_않았으면_에러_반환() {
        //given
        Member guestMember = Member.createGuestMember("testOauthId");

        //when
        guestMember.verifyBevy();
        guestMember.updatePaymentStatus(RequirementStatus.VERIFIED);
        guestMember.completeUnivEmailVerification("test@g.hongik.ac.kr");
        guestMember.signup("C123456","김홍익","01012345678",D022,"test@g.hongik.ac.kr");

        //when & then
        assertThatThrownBy(() -> {
            guestMember.grant();
        }).isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DISCORD_NOT_VERIFIED.getMessage());
    }

    @Test
    void grant할때_Bevy_인증하지_않았으면_에러_반환() {
        //given
        Member guestMember = Member.createGuestMember("testOauthId");

        //when
        guestMember.updatePaymentStatus(RequirementStatus.VERIFIED);
        guestMember.verifyDiscord("testDiscord","testNickname");
        guestMember.completeUnivEmailVerification("test@g.hongik.ac.kr");

        //when & then
        assertThatThrownBy(() -> {
            guestMember.grant();
        }).isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.BEVY_NOT_VERIFIED.getMessage());
    }

    @Test
    void grant할때_회비_납부_디스코드인증_Bevy인증_재학생인증하면_성공() {
        //given
        Member guestMember = Member.createGuestMember("testOauthId");


        //when
        guestMember.verifyBevy();
        guestMember.updatePaymentStatus(RequirementStatus.VERIFIED);
        guestMember.verifyDiscord("testDiscord","testNickname");
        guestMember.completeUnivEmailVerification("test@g.hongik.ac.kr");

        //then
        assertThatNoException();
    }

    @Test
    void 회원탈퇴시_이미_삭제된_유저면_실패(){
        //given
        Member member = Member.createGuestMember("testOauthId");

        //when
        member.withdraw();

        assertThatThrownBy(() -> {
            member.grant();
        }).isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_DELETED.getMessage());



    }

    @Test
    void 회원탈퇴시_존재하는_유저면_성공(){
        //given
        Member member = Member.createGuestMember("testOauthId");

        //when
        member.withdraw();

        //then
        assertThatNoException();
    }

    //TODO updateMemberInfo
    @Test
    void 회원정보_수정시_탈퇴한_유저면_실패() {
        //given
        Member member = Member.createGuestMember("testOauthId");

        //when
        member.updateMemberInfo("C123456","김홍익","01012345678",D022,"test@g.hongik.ac.kr","dis","cord");

        //then
        assertThatNoException();

    }

    //TODO verifyDiscord - 이 세개 해야하나? 의견 묻기
    @Test
    void 디스코드인증시_

    @Test
    void 디스코드인증시_탈퇴한_유저면_실패(){

    }


    //TODO updatePaymentStatus

    //TODO verifyBevy


}
