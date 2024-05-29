package com.gdschongik.gdsc.domain.member.application;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.BasicMemberInfoRequest;
import com.gdschongik.gdsc.domain.member.dto.response.MemberInfoResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OnboardingMemberServiceTest extends IntegrationTest {

    public static final BasicMemberInfoRequest BASIC_MEMBER_INFO_REQUEST =
            new BasicMemberInfoRequest(STUDENT_ID, NAME, PHONE_NUMBER, Department.D015, EMAIL);

    @Autowired
    private OnboardingMemberService onboardingMemberService;

    @Autowired
    private MemberRepository memberRepository;

    private void setFixture() {
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);
    }

    private void verifyEmail() {
        Member member = memberRepository.findById(1L).get();
        member.completeUnivEmailVerification(UNIV_EMAIL);
        memberRepository.save(member);
    }

    @Nested
    class 회원정보_조회시 {

        @Test
        void 기본회원정보_작성을_완료헀다면_성공한다() {
            // given
            setFixture();
            logoutAndReloginAs(1L, MemberRole.GUEST);
            verifyEmail();
            onboardingMemberService.updateBasicMemberInfo(BASIC_MEMBER_INFO_REQUEST);

            // when
            MemberInfoResponse response = onboardingMemberService.getMemberInfo();

            // then
            assertThat(response.memberId()).isEqualTo(1L);
        }

        @Test
        void 기본회원정보_작성을_완료하지_않았다면_실패한다() {
            // given
            setFixture();
            logoutAndReloginAs(1L, MemberRole.GUEST);

            // when & then
            assertThatThrownBy(() -> onboardingMemberService.getMemberInfo())
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.MEMBER_NOT_APPLIED.getMessage());
        }
    }
}
