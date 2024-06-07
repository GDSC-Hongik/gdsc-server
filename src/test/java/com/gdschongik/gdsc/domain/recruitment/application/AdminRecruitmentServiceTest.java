package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentQueryOption;
import com.gdschongik.gdsc.domain.recruitment.dto.response.AdminRecruitmentResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.integration.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class AdminRecruitmentServiceTest extends IntegrationTest {

    private static final RecruitmentQueryOption queryOption = new RecruitmentQueryOption(null, null);

    @Autowired
    private AdminRecruitmentService adminRecruitmentService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    private Recruitment createRecruitment(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType) {
        Recruitment recruitment = Recruitment.createRecruitment(name, startDate, endDate, academicYear, semesterType);
        return recruitmentRepository.save(recruitment);
    }

    @Nested
    class 모집기간_생성시 {
        @Test
        void 기간이_중복되는_Recruitment가_있다면_실패한다() {
            // given
            createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);
            RecruitmentCreateRequest request =
                    new RecruitmentCreateRequest(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_OVERLAP.getMessage());
        }

        @Test
        void 모집_시작일과_종료일의_연도가_입력된_학년도와_다르다면_실패한다() {
            // given
            RecruitmentCreateRequest request =
                    new RecruitmentCreateRequest(RECRUITMENT_NAME, START_DATE, END_DATE, 2025, SEMESTER_TYPE);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_ACADEMIC_YEAR.getMessage());
        }

        @Test
        void 모집_시작일과_종료일의_학기가_입력된_학기와_다르다면_실패한다() {
            // given
            RecruitmentCreateRequest request = new RecruitmentCreateRequest(
                    RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SemesterType.SECOND);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_MISMATCH_SEMESTER_TYPE.getMessage());
        }

        @Test
        void 모집_시작일과_종료일이_학기_시작일로부터_2주_이내에_있지_않다면_실패한다() {
            // given
            RecruitmentCreateRequest request = new RecruitmentCreateRequest(
                    RECRUITMENT_NAME, START_DATE, LocalDateTime.of(2024, 4, 10, 00, 00), ACADEMIC_YEAR, SEMESTER_TYPE);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitment(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_PERIOD_NOT_WITHIN_TWO_WEEKS.getMessage());
        }
    }

    @Nested
    class 모집기간_조회시 {
        @Test
        void 학년도_내림차순으로_정렬된다() {
            // given
            Recruitment recruitment2024 =
                    createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);
            Recruitment recruitment2025 = createRecruitment(
                    "2025학년도 1학기 1차 모집",
                    LocalDateTime.of(2025, 3, 2, 0, 0),
                    LocalDateTime.of(2025, 3, 5, 0, 0),
                    2025,
                    SEMESTER_TYPE);

            // when
            Page<AdminRecruitmentResponse> allRecruitments =
                    adminRecruitmentService.getAllRecruitments(queryOption, PageRequest.of(0, 10));

            // then
            assertThat(allRecruitments)
                    .containsSequence(
                            AdminRecruitmentResponse.of(recruitment2025, FIRST_ROUND),
                            AdminRecruitmentResponse.of(recruitment2024, FIRST_ROUND));
        }

        @Test
        void 학년도가_같다면_학기_내림차순으로_정렬된다() {
            // given
            Recruitment firstSemesterRecruitment =
                    createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);
            Recruitment secondSemesterRecruitment = createRecruitment(
                    SECOND_SEMESTER_RECRUITMENT_NAME,
                    SECOND_SEMESTER_START_DATE,
                    SECOND_SEMESTER_END_DATE,
                    ACADEMIC_YEAR,
                    SECOND_SEMESTER_SEMESTER_TYPE);

            // when
            Page<AdminRecruitmentResponse> allRecruitments =
                    adminRecruitmentService.getAllRecruitments(queryOption, PageRequest.of(0, 10));

            // then
            assertThat(allRecruitments)
                    .containsSequence(
                            AdminRecruitmentResponse.of(secondSemesterRecruitment, FIRST_ROUND),
                            AdminRecruitmentResponse.of(firstSemesterRecruitment, FIRST_ROUND));
        }

        @Test
        void 학년도와_학기가_같다면_모집_시작일_오름차순으로_정렬된다() {
            // given
            Recruitment roundOneRecruitment =
                    createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);
            Recruitment roundTwoRecruitment = createRecruitment(
                    ROUND_TWO_RECRUITMENT_NAME, ROUND_TWO_START_DATE, ROUND_TWO_END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);

            // when
            Page<AdminRecruitmentResponse> allRecruitments =
                    adminRecruitmentService.getAllRecruitments(queryOption, PageRequest.of(0, 10));

            // then
            assertThat(allRecruitments)
                    .containsSequence(
                            AdminRecruitmentResponse.of(roundOneRecruitment, FIRST_ROUND),
                            AdminRecruitmentResponse.of(roundTwoRecruitment, SECOND_ROUND));
        }

        @Test
        void 학년도와_학기가_같은_두_리쿠르팅이_있다면_모집_시작일이_빠른_리쿠르팅이_1차이다() {
            // given
            Recruitment roundOneRecruitment =
                    createRecruitment(RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);
            createRecruitment(
                    ROUND_TWO_RECRUITMENT_NAME, ROUND_TWO_START_DATE, ROUND_TWO_END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE);

            // when
            Page<AdminRecruitmentResponse> allRecruitments =
                    adminRecruitmentService.getAllRecruitments(queryOption, PageRequest.of(0, 10));
            List<AdminRecruitmentResponse> recruitmentResponses = allRecruitments.getContent();
            AdminRecruitmentResponse recruitmentResponse = recruitmentResponses.stream()
                    .filter(response -> response.recruitmentId().equals(roundOneRecruitment.getId()))
                    .findFirst()
                    .orElse(null);

            // then
            assertThat(recruitmentResponse).isNotNull();
            assertThat(recruitmentResponse.round()).isEqualTo(FIRST_ROUND_NAME);
        }
    }
}
