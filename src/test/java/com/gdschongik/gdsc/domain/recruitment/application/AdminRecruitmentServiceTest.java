package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentRoundUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminRecruitmentServiceTest extends IntegrationTest {

    @Autowired
    private AdminRecruitmentService adminRecruitmentService;

    @Nested
    class 리쿠르팅_생성시 {
        @Test
        void 학기_시작일과_종료일의_연도가_입력된_학년도와_다르다면_실패한다() {
            // given
            RecruitmentCreateRequest request =
                    new RecruitmentCreateRequest(START_DATE, END_DATE, 2025, SEMESTER_TYPE, FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR.getMessage());
        }

        @Test
        void 학기_시작일과_종료일의_학기가_입력된_학기와_다르다면_실패한다() {
            // given
            RecruitmentCreateRequest request =
                    new RecruitmentCreateRequest(START_DATE, END_DATE, ACADEMIC_YEAR, SemesterType.SECOND, FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE.getMessage());
        }

        @Test
        void 학년도_학기가_모두_중복되는_리쿠르팅이라면_실패한다() {
            // given
            createRecruitment(ACADEMIC_YEAR, SEMESTER_TYPE, FEE);
            RecruitmentCreateRequest request = new RecruitmentCreateRequest(
                    LocalDateTime.of(2024, 3, 12, 0, 0),
                    LocalDateTime.of(2024, 3, 13, 0, 0),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_OVERLAP.getMessage());
        }
    }

    @Nested
    class 모집회차_수정시 {
        @Test
        void 모집_시작일이_지났다면_수정_실패한다() {
            // given
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            RecruitmentRoundUpdateRequest request = new RecruitmentRoundUpdateRequest(
                    RECRUITMENT_NAME,
                    LocalDateTime.of(2024, 3, 12, 0, 0),
                    LocalDateTime.of(2024, 3, 13, 0, 0),
                    ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.updateRecruitmentRound(recruitmentRound.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_STARTDATE_ALREADY_PASSED.getMessage());
        }

        @Test
        void 기간이_중복되는_RecruitmentRound가_있다면_실패한다() {
            // given
            RecruitmentRound recruitmentRoundOne = createRecruitmentRound(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            RecruitmentRound recruitmentRoundTwo = createRecruitmentRound(
                    ROUND_TWO_RECRUITMENT_NAME,
                    ROUND_TWO_START_DATE,
                    ROUND_TWO_END_DATE,
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    RoundType.SECOND,
                    FEE);
            RecruitmentRoundUpdateRequest request =
                    new RecruitmentRoundUpdateRequest(RECRUITMENT_NAME, START_DATE, END_DATE, ROUND_TYPE);

            // when & then
            assertThatThrownBy(
                            () -> adminRecruitmentService.updateRecruitmentRound(recruitmentRoundTwo.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(PERIOD_OVERLAP.getMessage());
        }

        @Test
        void 차수가_중복되는_RecruitmentRound가_있다면_실패한다() {
            // given
            RecruitmentRound recruitmentRoundOne = createRecruitmentRound(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            RecruitmentRound recruitmentRoundTwo = createRecruitmentRound(
                    ROUND_TWO_RECRUITMENT_NAME,
                    ROUND_TWO_START_DATE,
                    ROUND_TWO_END_DATE,
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    RoundType.SECOND,
                    FEE);
            RecruitmentRoundUpdateRequest request = new RecruitmentRoundUpdateRequest(
                    RECRUITMENT_NAME, ROUND_TWO_START_DATE, ROUND_TWO_END_DATE, ROUND_TYPE);

            // when & then
            assertThatThrownBy(
                            () -> adminRecruitmentService.updateRecruitmentRound(recruitmentRoundTwo.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_TYPE_OVERLAP.getMessage());
        }
    }
}
