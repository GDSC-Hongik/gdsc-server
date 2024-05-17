package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.integration.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminRecruitmentServiceTest extends IntegrationTest {

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Nested
    class 모집기간_생성시 {
        @Test
        void 기간이_중복되는_Recruitment가_있다면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE);
            recruitmentRepository.save(recruitment);

            // when & then
            assertThatThrownBy(() -> {
                        Recruitment duplicatedRecruitment =
                                Recruitment.createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE);
                        recruitmentRepository.save(duplicatedRecruitment);
                    })
                    .isInstanceOf(CustomException.class)
                    .hasMessage(PERIOD_DUPLICATE.getMessage());
        }
    }
}
