package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.WRONG_DATE_ORDER;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
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
            Recruitment recruitment = Recruitment.createRecruitment(RECRUITMENT_NAME, period);

            // then
            assertThat(recruitment.getPeriod().getStartDate()).isEqualTo(START_DATE);
            assertThat(recruitment.getPeriod().getEndDate()).isEqualTo(END_DATE);
        }
    }

    @Nested
    class Period생성시 {
        @Test
        void 시작일이_종료일보다_앞서면_성공() {
            // given
            LocalDateTime startDate = START_DATE;
            LocalDateTime endDate = END_DATE;

            // when
            Period period = Period.createPeriod(startDate, endDate);

            // then
            assertThat(period.getStartDate()).isEqualTo(START_DATE);
            assertThat(period.getEndDate()).isEqualTo(END_DATE);
        }

        @Test
        void 종료일이_시작일보다_앞서면_실패() {
            // given
            LocalDateTime startDate = END_DATE;
            LocalDateTime endDate = START_DATE;

            // when
            assertThatThrownBy(() -> {
                        Period.createPeriod(startDate, endDate);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(WRONG_DATE_ORDER.getMessage());
        }
    }
}
