package com.gdschongik.gdsc.domain.membership.application;

import static com.gdschongik.gdsc.domain.member.domain.Department.D022;
import static com.gdschongik.gdsc.domain.member.domain.MemberRole.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
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
    private MembershipRepository membershipRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    public Member createMember() {
        Member member = Member.createGuestMember(OAUTH_ID);
        memberRepository.save(member);

        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();

        return memberRepository.save(member);
    }

    private Recruitment createRecruitment() {
        Recruitment recruitment =
                Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);
        return recruitmentRepository.save(recruitment);
    }

    private Membership createMembership(Member member, Recruitment recruitment) {
        Membership membership = Membership.createMembership(member, recruitment);
        return membershipRepository.save(membership);
    }

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void Recruitment가_없다면_실패한다() {
            // given
            createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Long recruitmentId = 1L;

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitmentId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_FOUND.getMessage());
        }

        @Test
        void 해당_학기에_이미_Membership을_발급받았다면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();
            Membership membership = createMembership(member, recruitment);

            // when
            membership.verifyPaymentStatus();
            membershipRepository.save(membership);

            // then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_VERIFIED.getMessage());
        }

        @Test
        void 해당_Recruitment에_대해_Membership을_생성한_적이_있다면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();
            createMembership(member, recruitment);

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_APPLIED.getMessage());
        }

        @Test
        void 해당_Recruitment의_모집기간이_아니라면_실패한다() {
            // given
            createMember();
            logoutAndReloginAs(1L, ASSOCIATE);
            Recruitment recruitment = createRecruitment();

            // when & then
            assertThatThrownBy(() -> membershipService.submitMembership(recruitment.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_OPEN.getMessage());
        }
    }
}
