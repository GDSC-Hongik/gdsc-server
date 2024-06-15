package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateOrUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.integration.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminRecruitmentServiceTest extends IntegrationTest {

    @Autowired
    private AdminRecruitmentService adminRecruitmentService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    private Recruitment createRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            RoundType roundType,
            Money fee) {
        Recruitment recruitment =
                Recruitment.createRecruitment(name, startDate, endDate, academicYear, semesterType, roundType, fee);
        return recruitmentRepository.save(recruitment);
    }

    @Nested
    class 모집기간_생성시 {
        @Test
        void 기간이_중복되는_Recruitment가_있다면_실패한다() {
            // given
            createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_OVERLAP.getMessage());
        }

        @Test
        void 모집_시작일과_종료일의_연도가_입력된_학년도와_다르다면_실패한다() {
            // given
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME, START_DATE, END_DATE, 2025, SEMESTER_TYPE, ROUND_TYPE, FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR.getMessage());
        }

        @Test
        void 모집_시작일과_종료일의_학기가_입력된_학기와_다르다면_실패한다() {
            // given
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SemesterType.SECOND, ROUND_TYPE, FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE.getMessage());
        }

        @Test
        void 모집_시작일과_종료일이_학기_시작일로부터_2주_이내에_있지_않다면_실패한다() {
            // given
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME,
                    START_DATE,
                    LocalDateTime.of(2024, 4, 10, 0, 0),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS.getMessage());
        }

        @Test
        void 학년도_학기_차수가_모두_중복되는_리쿠르팅이라면_실패한다() {
            // given
            createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME,
                    LocalDateTime.of(2024, 3, 12, 0, 0),
                    LocalDateTime.of(2024, 3, 13, 0, 0),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_TYPE_OVERLAP.getMessage());
        }
    }

    @Nested
    class 모집기간_수정시 {
        @Test
        void 모집_시작일이_지났다면_수정_실패한다() {
            // given
            Recruitment recruitment = createRecruitment(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME,
                    LocalDateTime.of(2024, 3, 12, 0, 0),
                    LocalDateTime.of(2024, 3, 13, 0, 0),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.updateRecruitment(recruitment.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_STARTDATE_ALREADY_PASSED.getMessage());
        }

        @Test
        void 기간이_중복되는_Recruitment가_있다면_실패한다() {
            // given
            Recruitment recruitmentRoundOne = createRecruitment(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            Recruitment recruitmentRoundTwo = createRecruitment(
                    ROUND_TWO_RECRUITMENT_NAME,
                    ROUND_TWO_START_DATE,
                    ROUND_TWO_END_DATE,
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    RoundType.SECOND,
                    FEE);
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.updateRecruitment(recruitmentRoundTwo.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_OVERLAP.getMessage());
        }

        @Test
        void 차수가_중복되는_Recruitment가_있다면_실패한다() {
            // given
            Recruitment recruitmentRoundOne = createRecruitment(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
            Recruitment recruitmentRoundTwo = createRecruitment(
                    ROUND_TWO_RECRUITMENT_NAME,
                    ROUND_TWO_START_DATE,
                    ROUND_TWO_END_DATE,
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    RoundType.SECOND,
                    FEE);
            RecruitmentCreateOrUpdateRequest request = new RecruitmentCreateOrUpdateRequest(
                    RECRUITMENT_NAME,
                    ROUND_TWO_START_DATE,
                    ROUND_TWO_END_DATE,
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    FEE_AMOUNT);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.updateRecruitment(recruitmentRoundTwo.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_ROUND_TYPE_OVERLAP.getMessage());
        }
    }
}
