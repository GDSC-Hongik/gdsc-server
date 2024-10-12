package com.gdschongik.gdsc.domain.study.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MentorStudyDetailServiceTest extends IntegrationTest {

    @Autowired
    private MentorStudyDetailService mentorStudyDetailService;

    @Autowired
    private StudyDetailRepository studyDetailRepository;

    @Nested
    class 스터디_과제_휴강_처리시 {

        @Test
        void 성공한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = createAssociateMember();
            Study study =
                    createStudy(mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));
            StudyDetail studyDetail = createStudyDetail(study, now, now.plusDays(7));
            logoutAndReloginAs(studyDetail.getStudy().getMentor().getId(), MemberRole.ASSOCIATE);

            // when
            mentorStudyDetailService.cancelStudyAssignment(studyDetail.getId());

            // then
            StudyDetail cancelledStudyDetail =
                    studyDetailRepository.findById(studyDetail.getId()).get();
            assertThat(cancelledStudyDetail.getAssignment().getStatus()).isEqualTo(StudyStatus.CANCELLED);
        }
    }
}
