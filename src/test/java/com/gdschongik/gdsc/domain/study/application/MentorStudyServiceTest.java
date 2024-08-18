package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.SESSION_DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Difficulty;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyStatus;
import com.gdschongik.gdsc.domain.study.dto.request.StudySessionCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

public class MentorStudyServiceTest extends IntegrationTest {

    @Autowired
    private MentorStudyService mentorStudyService;

    @Nested
    class 스터디_정보_작성시 {

        @Test
        void 성공한다() {
            // given
            LocalDateTime now = STUDY_START_DATETIME;
            Member mentor = createMentor();
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
                StudySessionCreateRequest sessionCreateRequest = new StudySessionCreateRequest(
                        id, SESSION_TITLE + i, SESSION_DESCRIPTION + i, Difficulty.HIGH, StudyStatus.OPEN);
                sessionCreateRequests.add(sessionCreateRequest);
            }

            StudyUpdateRequest request =
                    new StudyUpdateRequest(STUDY_NOTION_LINK, STUDY_INTRODUCTION, sessionCreateRequests);

            // when
            mentorStudyService.updateStudy(1L, request);

            // then
            Study savedStudy = studyRepository.findById(study.getId()).get();
            assertThat(savedStudy.getNotionLink()).isEqualTo(request.notionLink());

            List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(1L);
            for (int i = 0; i < studyDetails.size(); i++) {
                StudyDetail studyDetail = studyDetails.get(i);
                Long expectedId = studyDetail.getId();

                assertThat(studyDetail.getId()).isEqualTo(expectedId);
                assertThat(studyDetail.getSession().getTitle()).isEqualTo(SESSION_TITLE + expectedId);
                assertThat(studyDetail.getSession().getDescription()).isEqualTo(SESSION_DESCRIPTION + expectedId);
            }
        }
    }
}
