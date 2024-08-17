package com.gdschongik.gdsc.domain.study.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.domain.study.dto.request.StudyDetailUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudySessionCreateRequest;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            Study study = createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
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

    @Nested
    class 스터디_상세정보_작성시 {

        @Test
        void 성공한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = createAssociateMember();
            Study study = createNewStudy(
                    mentor,
                    4L,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            for (int i = 1; i <= 4; i++) {
                Long week = (long) i;
                createNewStudyDetail(week, study, now, now.plusDays(7));
                now = now.plusDays(8);
            }
            logoutAndReloginAs(study.getMentor().getId(), MemberRole.ASSOCIATE);

            List<StudySessionCreateRequest> sessionCreateRequests = new ArrayList<>();
            for (int i = 1; i <= study.getTotalWeek(); i++) {
                Long id = (long) i;
                StudySessionCreateRequest request =
                        new StudySessionCreateRequest(id, "title", "설명", Difficulty.HIGH, StudyStatus.OPEN);
                sessionCreateRequests.add(request);
            }

            StudyDetailUpdateRequest request =
                    new StudyDetailUpdateRequest("notionLink", "introduction", sessionCreateRequests);

            // when
            mentorStudyDetailService.updateStudyDetail(1L, request);

            // then
            StudyDetail studyDetail = studyDetailRepository.findAllByStudyId(1L).get(1);
            assertThat(studyDetail.getSession().getTitle()).isEqualTo("title");
        }
    }
}
