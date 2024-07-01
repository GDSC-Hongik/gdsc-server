package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentTest {

    @Nested
    class 학기생성시 {
        @Test
        void Period가_제대로_생성된다() {
            // given
            Period period = Period.createPeriod(START_DATE, END_DATE);

            // when
            Recruitment recruitment = Recruitment.createRecruitment(ACADEMIC_YEAR, SEMESTER_TYPE, FEE);

            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            // then
            assertThat(recruitmentRound.getPeriod().getStartDate()).isEqualTo(START_DATE);
            assertThat(recruitmentRound.getPeriod().getEndDate()).isEqualTo(END_DATE);
            assertThat(recruitmentRound.getPeriod().equals(period)).isTrue();
        }
    }
}
