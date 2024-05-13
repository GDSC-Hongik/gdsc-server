package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
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
            Recruitment recruitment = Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE);

            // then
            assertThat(recruitment.getPeriod().getStartDate()).isEqualTo(START_DATE);
            assertThat(recruitment.getPeriod().getEndDate()).isEqualTo(END_DATE);
            assertThat(recruitment.getPeriod().equals(period)).isTrue();
        }

        @Test
        void 잘못된_Period_순서_입력되면_생성_실패한다() {
            // when & then
            assertThatThrownBy(() -> {
                        Recruitment.createRecruitment(RECRUITMENT_NAME, END_DATE, START_DATE);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(DATE_PRECEDENCE_INVALID.getMessage());
        }

        @Test
        void Period_같은날짜가_입력되면_생성_실패한다() {
            // when & then
            assertThatThrownBy(() -> {
                        Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, START_DATE);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(DATE_PRECEDENCE_INVALID.getMessage());
        }
    }
}
