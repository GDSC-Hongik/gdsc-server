package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MembershipTest {

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 역할이_GUEST라면_멤버십_가입신청에_실패한다() {
            // given
            Member guestMember = Member.createGuestMember(OAUTH_ID);
            Recruitment recruitment = Recruitment.createRecruitment(ACADEMIC_YEAR, SEMESTER_TYPE, FEE);
            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> Membership.createMembership(guestMember, recruitmentRound))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(MEMBERSHIP_NOT_APPLICABLE.getMessage());
        }
    }
}
