package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.Member.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MembershipTest {

    @Nested
    class 멤버십_가입신청시 {

        @Test
        void 성공한다() {
            // given
            Member member = createGuestMember(OAUTH_ID);
            member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
            member.completeUnivEmailVerification(UNIV_EMAIL);
            member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
            member.verifyBevy();
            member.advanceToAssociate();

            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, FEE_NAME, Period.of(SEMESTER_START_DATE, SEMESTER_END_DATE));
            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_ROUND_NAME, START_TO_END_PERIOD, recruitment, ROUND_TYPE);

            // when
            Membership membership = Membership.createMembership(member, recruitmentRound);

            // then
            assertThat(membership).isNotNull();
        }
    }
}
