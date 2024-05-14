package com.gdschongik.gdsc.domain.membership.application;

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
        memberRepository.save(member);
    }

    private void setRecruitmentFixture() {
        Recruitment recruitment = Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE);
        recruitmentRepository.save(recruitment);
    }

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 열려있는_Recruitment가_없다면_실패한다() {
            // given
            setMemberFixture();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);

            // when & then
            assertThatThrownBy(() -> membershipService.applyMembership())
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_FOUND.getMessage());
        }

        @Test
        void 열려있는_Recruitment에_Membership을_생성한_적이_있다면_실패한다() {
            // given
            setMemberFixture();
            setRecruitmentFixture();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);


            // when & then
            assertThatThrownBy(() -> membershipService.applyMembership())
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_ALREADY_APPLIED.getMessage());
        }
    }
}
