package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmission;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionFetcher;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

class StudentStudyHistoryServiceTest extends IntegrationTest {

    @Autowired
    private StudentStudyHistoryService studentStudyHistoryService;

    @Autowired
    private StudyHistoryRepository studyHistoryRepository;

    @Autowired
    private AssignmentHistoryRepository assignmentHistoryRepository;

    private void setCurrentTime(LocalDateTime now) {
        try (MockedStatic<LocalDateTime> mock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDateTime::now).thenReturn(now);
        }
    }

    @Nested
    class 과제_제출할때 {

        @Test
        void 성공한다() {
            // given
            Member mentor = createAssociateMember();
            // TODO: LocalDateTime.now() 관련 테스트 정책 논의 필요
            LocalDateTime now = LocalDateTime.now(); // 통합 테스트에서는 LocalDateTime.now()를 사용해야 함
            Study study = createStudy(
                    mentor,
                    Period.of(now.minusWeeks(1), now.plusWeeks(7)), // 스터디 기간: 1주 전 ~ 7주 후
                    Period.of(now.minusWeeks(2), now.minusWeeks(1))); // 수강신청 기간: 2주 전 ~ 1주 전
            StudyDetail studyDetail =
                    createStudyDetail(study, now.minusDays(6), now.plusDays(1)); // 1주차 기간: 6일 전 ~ 1일 후
            publishAssignment(studyDetail);

            Member student = createRegularMember();
            logoutAndReloginAs(student.getId(), MemberRole.REGULAR);

            // 수강신청 valiadtion 로직이 LocalDateTime.now() 기준으로 동작하기 때문에 직접 수강신청 생성
            StudyHistory studyHistory = StudyHistory.create(student, study);
            studyHistory.updateRepositoryLink(REPOSITORY_LINK);
            studyHistoryRepository.save(studyHistory);

            // 제출정보 조회 fetcher stubbing
            AssignmentSubmissionFetcher mockFetcher = mock(AssignmentSubmissionFetcher.class);
            when(mockFetcher.fetch())
                    .thenReturn(new AssignmentSubmission(REPOSITORY_LINK, COMMIT_HASH, 500, COMMITTED_AT));
            when(githubClient.getLatestAssignmentSubmissionFetcher(anyString(), anyInt()))
                    .thenReturn(mockFetcher);

            // when
            studentStudyHistoryService.submitAssignment(studyDetail.getId());

            // then
            AssignmentHistory assignmentHistory =
                    assignmentHistoryRepository.findById(1L).orElseThrow();
            assertThat(assignmentHistory.getSubmissionStatus()).isEqualTo(AssignmentSubmissionStatus.SUCCESS);
            assertThat(assignmentHistory.getSubmissionLink()).isEqualTo(REPOSITORY_LINK);
            assertThat(assignmentHistory.getCommitHash()).isEqualTo(COMMIT_HASH);
            assertThat(assignmentHistory.getContentLength()).isEqualTo(500);
        }
    }
}
