package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.WRONG_DATE_ORDER;
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
        void 잘못된_Period_순서_입력되면_생성_실패() {
            // when & then
            assertThatThrownBy(() -> {
                        Recruitment.createRecruitment(RECRUITMENT_NAME, END_DATE, START_DATE);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(WRONG_DATE_ORDER.getMessage());
        }

        @Test
        void Period_같은날짜가_입력되면_생성_실패() {
            // when & then
            assertThatThrownBy(() -> {
                        Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, START_DATE);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(WRONG_DATE_ORDER.getMessage());
        }
    }

    @Nested
    class Period생성시 {
        @Test
        void 시작일이_종료일보다_앞서면_성공() {
            // when
            Period period = Period.createPeriod(START_DATE, END_DATE);

            // then
            assertThat(period.getStartDate()).isEqualTo(START_DATE);
            assertThat(period.getEndDate()).isEqualTo(END_DATE);
        }

        @Test
        void 종료일이_시작일보다_앞서면_실패() {
            // when & then
            assertThatThrownBy(() -> {
                        Period.createPeriod(END_DATE, START_DATE);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(WRONG_DATE_ORDER.getMessage());
        }

        @Test
        void 종료일이_시작일과_같으면_실패() {
            // when & then
            assertThatThrownBy(() -> {
                        Period.createPeriod(START_DATE, WRONG_END_DATE);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(WRONG_DATE_ORDER.getMessage());
        }
    }
}
