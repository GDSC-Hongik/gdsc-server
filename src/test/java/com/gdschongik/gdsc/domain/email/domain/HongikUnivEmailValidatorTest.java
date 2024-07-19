package com.gdschongik.gdsc.domain.email.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.common.constant.MemberConstant;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.email.HongikUnivEmailValidator;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HongikUnivEmailValidatorTest {

    HongikUnivEmailValidator hongikUnivEmailValidator = new HongikUnivEmailValidator();

    @Test
    @DisplayName("'g.hongik.ac.kr' 도메인을 가진 이메일을 검증할 수 있다.")
    void validateEmailDomainTest() {
        // given
        String hongikDomainEmail = "test@g.hongik.ac.kr";
        Optional<Member> optionalMember = Optional.empty();

        // when & then
        assertThatCode(() -> hongikUnivEmailValidator.validateSendUnivEmailVerificationLink(
                        hongikDomainEmail, optionalMember))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@naver.com", "test@mail.hongik.ac.kr", "test@gmail.com", "test@gg.hongik.ac.kr"})
    @DisplayName("'g.hongik.ac.kr'가 아닌 도메인을 가진 이메일을 입력하면 예외를 발생시킨다.")
    void validateEmailDomainMismatchTest(String email) {
        // given
        Optional<Member> optionalMember = Optional.empty();

        // when & then
        assertThatThrownBy(() -> hongikUnivEmailValidator.validateSendUnivEmailVerificationLink(email, optionalMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_EMAIL_DOMAIN_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("Email의 '@' 앞 부분에는 연속되지 않은 점이 포함될 수 있다.")
    void validateEmailFormatWithDotsTest() {
        // given
        String email = "t.e.s.t@g.hongik.ac.kr";
        Optional<Member> optionalMember = Optional.empty();

        assertThatCode(() -> hongikUnivEmailValidator.validateSendUnivEmailVerificationLink(email, optionalMember))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "te&st@g.hongik.ac.kr",
                "te=st@g.hongik.ac.kr",
                "te'st@g.hongik.ac.kr",
                "te-st@g.hongik.ac.kr",
                "te+st@g.hongik.ac.kr",
                "te,st@g.hongik.ac.kr",
                "te<st@g.hongik.ac.kr",
                "te>st@g.hongik.ac.kr"
            })
    @DisplayName("Email의 '@' 앞 부분에 '&', '=', ''', '-', '+', ',', '<', '>'가 포함되는 경우 예외를 발생시킨다.")
    void validateEmailFormatMismatchTest(String email) {
        // given
        Optional<Member> optionalMember = Optional.empty();

        // when & then
        assertThatThrownBy(() -> hongikUnivEmailValidator.validateSendUnivEmailVerificationLink(email, optionalMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_EMAIL_FORMAT_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("Email의 '@' 앞 부분에 '.'이 2개 연속 오는 경우 예외를 발생시킨다.")
    void validateEmailFormatMismatchWithDotsTest() {
        // given
        String email = "te..st@g.hongik.ac.kr";
        Optional<Member> optionalMember = Optional.empty();

        // when & then
        assertThatThrownBy(() -> hongikUnivEmailValidator.validateSendUnivEmailVerificationLink(email, optionalMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_EMAIL_FORMAT_MISMATCH.getMessage());
    }

    @Test
    void 이미_가입된_재학생_메일이라면_실패한다() {
        // given
        String hongikDomainEmail = "test@g.hongik.ac.kr";

        Member member = Member.createGuestMember(MemberConstant.OAUTH_ID);
        Optional<Member> optionalMember = Optional.of(member);

        // when & then
        assertThatThrownBy(() -> hongikUnivEmailValidator.validateSendUnivEmailVerificationLink(
                        hongikDomainEmail, optionalMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(UNIV_EMAIL_ALREADY_SATISFIED.getMessage());
    }
}
