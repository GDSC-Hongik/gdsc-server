package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
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

            StudyDetailUpdateRequest request =
                    new StudyDetailUpdateRequest(STUDY_NOTION_LINK, STUDY_INTRODUCTION, sessionCreateRequests);

            // when
            mentorStudyDetailService.updateStudyDetail(1L, request);

            // then
            Study savedStudy = studyRepository.findById(study.getId()).get();
            assertThat(savedStudy.getNotionLink()).isEqualTo(request.notionLink());
            assertThat(savedStudy.getIntroduction()).isEqualTo(request.introduction());

            List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(1L);
            for (int i = 0; i < studyDetails.size(); i++) {
                StudyDetail studyDetail = studyDetails.get(i);
                Long expectedId = studyDetail.getId();

                assertThat(studyDetail.getId()).isEqualTo(expectedId);
                assertThat(studyDetail.getSession().getTitle()).isEqualTo(SESSION_TITLE + expectedId);
                assertThat(studyDetail.getSession().getDescription()).isEqualTo(SESSION_DESCRIPTION + expectedId);
                assertThat(studyDetail.getSession().getDifficulty()).isEqualTo(Difficulty.HIGH);
                assertThat(studyDetail.getSession().getStatus()).isEqualTo(StudyStatus.OPEN);
            }
        }
    }
}
