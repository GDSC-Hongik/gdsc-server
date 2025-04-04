package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.service.AssignmentHistoryGrader;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AssignmentHistoryGraderTest {

    FixtureHelper fixtureHelper = new FixtureHelper();
    AssignmentHistoryGrader grader = new AssignmentHistoryGrader();

    AssignmentSubmissionFetcher mockFetcher = mock(AssignmentSubmissionFetcher.class);

    // FixtureHelper 래핑 메서드
    private AssignmentHistory createAssignmentHistory() {
        Study study = createStudyWithMentor(1L);
        StudyDetail studyDetail = fixtureHelper.createStudyDetail(
                study, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        return AssignmentHistory.create(studyDetail, fixtureHelper.createAssociateMember(2L));
    }

    private Study createStudyWithMentor(Long mentorId) {
        Period applicationPeriod = Period.of(STUDY_START_DATETIME.minusDays(7), STUDY_START_DATETIME.minusDays(1));
        return fixtureHelper.createStudyWithMentor(mentorId, STUDY_ONGOING_PERIOD, applicationPeriod);
    }

    @Nested
    class 과제_채점시 {

        @Test
        void 과제내용이_최소길이_이상이면_성공_처리된다() {
            // given
            AssignmentHistory history = createAssignmentHistory();
            AssignmentSubmission validSubmission = new AssignmentSubmission("url", "hash", 500, LocalDateTime.now());
            when(mockFetcher.fetch()).thenReturn(validSubmission);

            // when
            grader.judge(mockFetcher, history);

            // then
            assertThat(history.getSubmissionStatus()).isEqualTo(AssignmentSubmissionStatus.SUCCESS);
            assertThat(history.getSubmissionLink()).isEqualTo("url");
            assertThat(history.getCommitHash()).isEqualTo("hash");
            assertThat(history.getContentLength()).isEqualTo(500);
        }

        @Test
        void 과제내용이_최소길이_미만이면_실패_처리된다() {
            // given
            AssignmentHistory history = createAssignmentHistory();
            AssignmentSubmission shortSubmission = new AssignmentSubmission("url", "hash", 200, LocalDateTime.now());
            when(mockFetcher.fetch()).thenReturn(shortSubmission);

            // when
            grader.judge(mockFetcher, history);

            // then
            assertThat(history.getSubmissionStatus()).isEqualTo(AssignmentSubmissionStatus.FAILURE);
            assertThat(history.getSubmissionFailureType()).isEqualTo(SubmissionFailureType.WORD_COUNT_INSUFFICIENT);
        }

        @Test
        void 해당_위치에_과제파일_미존재시_위치확인불가로_실패_처리된다() {
            // given
            AssignmentHistory history = createAssignmentHistory();
            when(mockFetcher.fetch()).thenThrow(new CustomException(ErrorCode.GITHUB_CONTENT_NOT_FOUND));

            // when
            grader.judge(mockFetcher, history);

            // then
            assertThat(history.getSubmissionStatus()).isEqualTo(AssignmentSubmissionStatus.FAILURE);
            assertThat(history.getSubmissionFailureType()).isEqualTo(SubmissionFailureType.LOCATION_UNIDENTIFIABLE);
        }

        @Test
        void 그외_Github_문제인경우_알수없는오류로_실패_처리된다() {
            // given
            AssignmentHistory history = createAssignmentHistory();
            when(mockFetcher.fetch()).thenThrow(new CustomException(ErrorCode.GITHUB_FILE_READ_FAILED));

            // when
            grader.judge(mockFetcher, history);

            // then
            assertThat(history.getSubmissionStatus()).isEqualTo(AssignmentSubmissionStatus.FAILURE);
            assertThat(history.getSubmissionFailureType()).isEqualTo(SubmissionFailureType.UNKNOWN);
        }
    }
}
