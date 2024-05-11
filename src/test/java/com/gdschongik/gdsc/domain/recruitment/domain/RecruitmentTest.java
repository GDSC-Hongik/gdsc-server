package com.gdschongik.gdsc.domain.recruitment.domain;

import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;


class RecruitmentTest {

    @Nested
    class 학기생성시 {
        @Test
        void Period가_제대로_생성된다() {
            //given
            Period period = Period.createPeriod(START_DATE,END_DATE);

            //when
            Recruitment recruitment = Recruitment.createRecruitment(RECRUITMENT_NAME,period);

            //then
            assertThat(recruitment.getPeriod().getStartDate()).isEqualTo(START_DATE);
            assertThat(recruitment.getPeriod().getEndDate()).isEqualTo(END_DATE);
        }
    }

}