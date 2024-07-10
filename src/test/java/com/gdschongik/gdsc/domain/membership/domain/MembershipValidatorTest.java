package com.gdschongik.gdsc.domain.membership.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MembershipValidatorTest {

    MembershipValidator membershipValidator = new MembershipValidator();

    @Nested
    class 멤버십_가입신청시 {
        @Test
        void 해당_RecruitmentRound의_모집기간이_아니라면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));
            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> membershipValidator.validateMembershipSubmit(recruitmentRound))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_NOT_OPEN.getMessage());
        }
    }
}
