package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RecruitmentRoundValidatorTest {

    RecruitmentRoundValidator recruitmentRoundValidator = new RecruitmentRoundValidator();

    @Nested
    class 모집회차_생성시 {

        @Test
        void 모집_시작일과_종료일의_연도가_입력된_학년도와_다르다면_실패한다() {
            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, END_DATE, 2025, SEMESTER_TYPE))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR.getMessage());
        }

        @Test
        void 학기_시작일과_종료일의_학기가_입력된_학기와_다르다면_실패한다() {
            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, END_DATE, ACADEMIC_YEAR, SemesterType.SECOND))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE.getMessage());
        }

        @Test
        void 모집_시작일과_종료일이_학기_시작일로부터_2주_이내에_있지_않다면_실패한다() {
            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, LocalDateTime.of(2024, 4, 10, 0, 0), ACADEMIC_YEAR, SEMESTER_TYPE))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS.getMessage());
        }
    }
}
