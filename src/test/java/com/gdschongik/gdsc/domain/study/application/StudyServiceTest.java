package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.ACADEMIC_YEAR;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.SEMESTER_TYPE;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.START_DATE;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.DAY_OF_WEEK;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.STUDY_TYPE;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.TOTAL_WEEK;
import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_APPLICATION_START_DATE_INVALID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StudyServiceTest extends IntegrationTest {

    @Autowired
    private StudyService studyService;

    @Nested
    class 스터디_개설시 {

        @Test
        void 신청기간_시작일이_스터디_시작일보다_늦으면_실패한다() {
            // given
            Member member = createMember();
            LocalDate startDate = START_DATE.toLocalDate();
            LocalDate applicationStartDate = startDate.plusDays(3);
            LocalDate applicationEndDate = applicationStartDate.plusDays(3);
            StudyCreateRequest request = new StudyCreateRequest(
                    member.getId(),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    applicationStartDate,
                    applicationEndDate,
                    TOTAL_WEEK,
                    startDate,
                    DAY_OF_WEEK,
                    STUDY_TYPE);
            // when & then
            assertThatThrownBy(() -> studyService.createStudyAndStudyDetail(request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_APPLICATION_START_DATE_INVALID.getMessage());
        }
    }
}
