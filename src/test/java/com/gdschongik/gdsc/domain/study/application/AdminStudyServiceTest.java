package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.common.constant.TemporalConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.helper.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminStudyServiceTest extends IntegrationTest {

    @Autowired
    private AdminStudyService adminStudyService;

    @Nested
    class 스터디_개설시 {

        @Test
        void 멘토로_지정된_멤버의_MemberStudyRole은_MENTOR이다() {
            // given
            Member regularMember = createRegularMember();
            StudyCreateRequest request = new StudyCreateRequest(
                    regularMember.getId(),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    STUDY_TITLE,
                    STUDY_START_DATETIME.minusDays(10).toLocalDate(),
                    STUDY_START_DATETIME.minusDays(5).toLocalDate(),
                    TOTAL_WEEK,
                    STUDY_START_DATETIME.toLocalDate(),
                    DAY_OF_WEEK,
                    STUDY_START_TIME,
                    STUDY_END_TIME,
                    ONLINE_STUDY);

            // when
            adminStudyService.createStudyAndStudyDetail(request);

            // then
            Member mentor = memberRepository.findById(regularMember.getId()).get();
            assertThat(mentor.isMentor()).isTrue();
        }
    }
}
