package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MembershipServiceTest extends IntegrationTest {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    private void setMemberFixture() {
        Member member = Member.createGuestMember(OAUTH_ID);

        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updatePaymentStatus(VERIFIED);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();

        member.grant();
        memberRepository.save(member);
    }

    private Recruitment createRecruitment() {
        Recruitment recruitment = Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE);
        return recruitmentRepository.save(recruitment);
    }

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void Recruitment가_없다면_실패한다() {
            // given
            setMemberFixture();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            Long recruitmentId = 1L;

            // when & then
            assertThatThrownBy(() -> membershipService.receiveMembership(recruitmentId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_FOUND.getMessage());
        }

        @Test
        void 열려있는_Recruitment에_Membership을_생성한_적이_있다면_실패한다() {
            // given
            setMemberFixture();
            Recruitment recruitment = createRecruitment();
            // todo: Member.grant() 작업 후 ASSOCIATE로 로그인하는 것으로 변경
            logoutAndReloginAs(1L, MemberRole.USER);
            membershipService.receiveMembership(recruitment.getId());

            // when & then
            assertThatThrownBy(() -> membershipService.receiveMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_APPLIED.getMessage());
        }
    }
}
