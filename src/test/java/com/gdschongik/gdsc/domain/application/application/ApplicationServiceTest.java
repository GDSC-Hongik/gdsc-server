package com.gdschongik.gdsc.domain.application.application;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationServiceTest extends IntegrationTest {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private MemberRepository memberRepository;

    private void setFixture() {
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);
    }

    @Nested
    class 가입신청_접수시 {
        @Test
        void 승인되지_않은_멤버라면_가입신청서_접수에_실패한다() {
            // given
            setFixture();
            logoutAndReloginAs(1L, MemberRole.GUEST);

            // when & then
            assertThatThrownBy(() -> applicationService.receive())
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.MEMBER_NOT_GRANTED.getMessage());
        }
    }
}
