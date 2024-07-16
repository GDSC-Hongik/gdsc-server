package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RecruitmentRoundValidatorTest {

    RecruitmentRoundValidator recruitmentRoundValidator = new RecruitmentRoundValidator();

    @Nested
    class 모집회차_생성시 {

        @Test
        void 모집_시작일과_종료일의_연도가_입력된_학년도와_다르다면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    2025,
                    SEMESTER_TYPE,
                    FEE,
                    Period.createPeriod(LocalDateTime.of(2025, 3, 2, 0, 0), LocalDateTime.of(2025, 8, 31, 0, 0)));

            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, END_DATE, ROUND_TYPE, recruitment, List.of()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS.getMessage());
        }

        @Test
        void 학기_시작일과_종료일의_학기가_입력된_학기와_다르다면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR,
                    SemesterType.SECOND,
                    FEE,
                    Period.createPeriod(LocalDateTime.of(2024, 9, 1, 0, 0), LocalDateTime.of(2025, 2, 28, 0, 0)));

            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, END_DATE, ROUND_TYPE, recruitment, List.of()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS.getMessage());
        }

        @Test
        void 모집_시작일과_종료일이_학기_시작일로부터_2주_이내에_있지_않다면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(START_DATE, END_DATE));

            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, LocalDateTime.of(2024, 4, 10, 0, 0), ROUND_TYPE, recruitment, List.of()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS.getMessage());
        }

        @Test
        void 학년도_학기_차수가_모두_중복되면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(START_DATE, END_DATE));

            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            LocalDateTime.of(2024, 3, 8, 0, 0),
                            LocalDateTime.of(2024, 3, 10, 0, 0),
                            ROUND_TYPE,
                            recruitment,
                            List.of(recruitmentRound)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_TYPE_OVERLAP.getMessage());
        }

        @Test
        void RoundType_1차가_없을때_2차를_생성하려_하면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(START_DATE, END_DATE));

            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, END_DATE, RoundType.SECOND, recruitment, List.of()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ROUND_ONE_DOES_NOT_EXIST.getMessage());
        }

        @Test
        void 기간이_중복되는_모집회차가_있다면_실패한다() {
            // given
            Recruitment recruitment = Recruitment.createRecruitment(
                    ACADEMIC_YEAR, SEMESTER_TYPE, FEE, Period.createPeriod(START_DATE, END_DATE));

            RecruitmentRound recruitmentRound =
                    RecruitmentRound.create(RECRUITMENT_NAME, START_DATE, END_DATE, recruitment, ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> recruitmentRoundValidator.validateRecruitmentRoundCreate(
                            START_DATE, END_DATE, ROUND_TYPE, recruitment, List.of(recruitmentRound)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(PERIOD_OVERLAP.getMessage());
        }
    }
}
