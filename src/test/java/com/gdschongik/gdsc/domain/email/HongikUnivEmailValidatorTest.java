package com.gdschongik.gdsc.domain.email;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.email.HongikUnivEmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class HongikUnivEmailValidatorTest {

    @Autowired
    private HongikUnivEmailValidator hongikUnivEmailValidator;

    @Test
    @DisplayName("'g.hongik.ac.kr' 도메인을 가진 이메일을 검증할 수 있다.")
    void validateEmailDomainTest() {
        String hongikDomainEmail = "test@g.hongik.ac.kr";

        assertThatCode(() -> hongikUnivEmailValidator.validate(hongikDomainEmail))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@naver.com", "test@mail.hongik.ac.kr", "test@gmail.com", "test@gg.hongik.ac.kr"})
    @DisplayName("'g.hongik.ac.kr'가 아닌 도메인을 가진 이메일을 입력하면 예외를 발생시킨다.")
    void validateEmailDomainMismatchTest(String email) {
        assertThatThrownBy(() -> hongikUnivEmailValidator.validate(email))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_EMAIL_DOMAIN_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("Email의 '@' 앞 부분에는 연속되지 않은 점이 포함될 수 있다.")
    void validateEmailFormatWithDotsTest() {
        String email = "t.e.s.t@g.hongik.ac.kr";

        assertThatCode(() -> hongikUnivEmailValidator.validate(email)).doesNotThrowAnyException();
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
        assertThatThrownBy(() -> hongikUnivEmailValidator.validate(email))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_EMAIL_FORMAT_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("Email의 '@' 앞 부분에 '.'이 2개 연속 오는 경우 예외를 발생시킨다.")
    void validateEmailFormatMismatchWithDotsTest() {
        String email = "te..st@g.hongik.ac.kr";
        assertThatThrownBy(() -> hongikUnivEmailValidator.validate(email))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.UNIV_EMAIL_FORMAT_MISMATCH.getMessage());
    }
}
