package com.gdschongik.gdsc.domain.study.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StudyMentorServiceTest extends IntegrationTest {

    @Autowired
    private StudyMentorService studyMentorService;

    @Autowired
    private StudyDetailRepository studyDetailRepository;

    @Nested
    class 스터디_과제_휴강_처리시 {

        @Test
        void 성공한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            StudyDetail studyDetail = createStudyDetail(now, now.plusDays(7));

            // when
            studyMentorService.cancelStudyAssignment(studyDetail.getId());

            // then
            StudyDetail cancelledStudyDetail =
                    studyDetailRepository.findById(studyDetail.getId()).get();
            assertThat(cancelledStudyDetail.getAssignment().getStatus()).isEqualTo(StudyStatus.CANCELLED);
        }
    }
}
