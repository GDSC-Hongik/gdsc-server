package com.gdschongik.gdsc.domain.recruitment.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.dto.request.RecruitmentRoundCreateUpdateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminRecruitmentServiceTest extends IntegrationTest {

    @Autowired
    private AdminRecruitmentService adminRecruitmentService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Nested
    class 리쿠르팅_생성시 {

        @Test
        void 성공한다() {
            // given
            RecruitmentCreateRequest request = new RecruitmentCreateRequest(
                    SEMESTER_START_DATE, SEMESTER_END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, FEE_AMOUNT);

            // when
            adminRecruitmentService.createRecruitment(request);

            // then
            assertThat(recruitmentRepository.findAll()).hasSize(1);
        }
    }

    // todo: test 원복
    // @Nested
    // class 모집회차_수정시 {
    //     @Test
    //     void 모집_시작일이_지났다면_수정_실패한다() {
    //         // given
    //         RecruitmentRound recruitmentRound = createRecruitmentRound(
    //                 RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
    //         RecruitmentRoundUpdateRequest request = new RecruitmentRoundUpdateRequest(
    //                 RECRUITMENT_NAME,
    //                 LocalDateTime.of(2024, 3, 12, 0, 0),
    //                 LocalDateTime.of(2024, 3, 13, 0, 0),
    //                 ROUND_TYPE);
    //
    //         // when & then
    //         assertThatThrownBy(() -> adminRecruitmentService.updateRecruitmentRound(recruitmentRound.getId(),
    // request))
    //                 .isInstanceOf(CustomException.class)
    //                 .hasMessage(RECRUITMENT_ROUND_STARTDATE_ALREADY_PASSED.getMessage());
    //     }
    //
    //     @Test
    //     void 기간이_중복되는_RecruitmentRound가_있다면_실패한다() {
    //         // given
    //         RecruitmentRound recruitmentRoundOne = createRecruitmentRound(
    //                 RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
    //         RecruitmentRound recruitmentRoundTwo = createRecruitmentRound(
    //                 ROUND_TWO_RECRUITMENT_NAME,
    //                 ROUND_TWO_START_DATE,
    //                 ROUND_TWO_END_DATE,
    //                 ACADEMIC_YEAR,
    //                 SEMESTER_TYPE,
    //                 RoundType.SECOND,
    //                 FEE);
    //         RecruitmentRoundUpdateRequest request =
    //                 new RecruitmentRoundUpdateRequest(RECRUITMENT_NAME, START_DATE, END_DATE, ROUND_TYPE);
    //
    //         // when & then
    //         assertThatThrownBy(
    //                         () -> adminRecruitmentService.updateRecruitmentRound(recruitmentRoundTwo.getId(),
    // request))
    //                 .isInstanceOf(CustomException.class)
    //                 .hasMessage(PERIOD_OVERLAP.getMessage());
    //     }
    //
    //     @Test
    //     void 차수가_중복되는_RecruitmentRound가_있다면_실패한다() {
    //         // given
    //         RecruitmentRound recruitmentRoundOne = createRecruitmentRound(
    //                 RECRUITMENT_NAME, START_DATE, END_DATE, ACADEMIC_YEAR, SEMESTER_TYPE, ROUND_TYPE, FEE);
    //         RecruitmentRound recruitmentRoundTwo = createRecruitmentRound(
    //                 ROUND_TWO_RECRUITMENT_NAME,
    //                 ROUND_TWO_START_DATE,
    //                 ROUND_TWO_END_DATE,
    //                 ACADEMIC_YEAR,
    //                 SEMESTER_TYPE,
    //                 RoundType.SECOND,
    //                 FEE);
    //         RecruitmentRoundUpdateRequest request = new RecruitmentRoundUpdateRequest(
    //                 RECRUITMENT_NAME, ROUND_TWO_START_DATE, ROUND_TWO_END_DATE, ROUND_TYPE);
    //
    //         // when & then
    //         assertThatThrownBy(
    //                         () -> adminRecruitmentService.updateRecruitmentRound(recruitmentRoundTwo.getId(),
    // request))
    //                 .isInstanceOf(CustomException.class)
    //                 .hasMessage(RECRUITMENT_ROUND_TYPE_OVERLAP.getMessage());
    //     }
    // }

    @Nested
    class 모집회차_생성시 {

        @Test
        void 학년도와_학기가_일치하는_리쿠르팅이_존재하지_않는다면_실패한다() {
            // given
            RecruitmentRoundCreateUpdateRequest request = new RecruitmentRoundCreateUpdateRequest(
                    ACADEMIC_YEAR, SEMESTER_TYPE, RECRUITMENT_NAME, START_DATE, END_DATE, ROUND_TYPE);

            // when & then
            assertThatThrownBy(() -> adminRecruitmentService.createRecruitmentRound(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_NOT_FOUND.getMessage());
        }
    }
}
