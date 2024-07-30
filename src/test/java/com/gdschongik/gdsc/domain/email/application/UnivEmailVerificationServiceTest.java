package com.gdschongik.gdsc.domain.email.application;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.OAUTH_ID;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.UNIV_EMAIL;
import static com.gdschongik.gdsc.global.exception.ErrorCode.EMAIL_NOT_SENT;
import static com.gdschongik.gdsc.global.exception.ErrorCode.EXPIRED_EMAIL_VERIFICATION_TOKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.config.TestRedisConfig;
import com.gdschongik.gdsc.domain.email.dto.request.UnivEmailVerificationRequest;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.email.EmailVerificationTokenUtil;
import com.gdschongik.gdsc.global.util.email.MailSender;
import com.gdschongik.gdsc.helper.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(TestRedisConfig.class)
public class UnivEmailVerificationServiceTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UnivEmailVerificationLinkSendService univEmailVerificationLinkSendService;

    @Autowired
    private UnivEmailVerificationService univEmailVerificationService;

    @Autowired
    private EmailVerificationTokenUtil emailVerificationTokenUtil;

    @MockBean
    private MailSender mailSender;

    @Nested
    class 재학생_메일_인증시 {

        @Test
        void 레디스에_이메일인증정보가_존재하지_않으면_실패한다() {
            // given
            Member member = Member.createGuestMember(OAUTH_ID);
            memberRepository.save(member);
            String verificationToken =
                    emailVerificationTokenUtil.generateEmailVerificationToken(member.getId(), UNIV_EMAIL);
            UnivEmailVerificationRequest request = new UnivEmailVerificationRequest(verificationToken);

            // when & then
            assertThatThrownBy(() -> univEmailVerificationService.verifyMemberUnivEmail(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(EMAIL_NOT_SENT.getMessage());
        }

        @Test
        void 인증토큰과_레디스에_존재하는_인증정보의_토큰이_다르면_실패한다() {
            // given
            // TODO: 아래 두줄 createGuestMember로 대체하기
            Member member = memberRepository.save(Member.createGuestMember(OAUTH_ID));
            logoutAndReloginAs(member.getId(), member.getRole());

            // when
            univEmailVerificationLinkSendService.send(UNIV_EMAIL);
            String oldVerificationToken = univEmailVerificationService
                    .getUnivEmailVerificationFromRedis(member.getId())
                    .get()
                    .getVerificationToken();
            UnivEmailVerificationRequest request = new UnivEmailVerificationRequest(oldVerificationToken);
            univEmailVerificationLinkSendService.send("b123456@g.hongik.ac.kr");

            // then
            assertThatThrownBy(() -> univEmailVerificationService.verifyMemberUnivEmail(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(EXPIRED_EMAIL_VERIFICATION_TOKEN.getMessage());
        }
    }
}
