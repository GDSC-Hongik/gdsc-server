package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionFetcher;
import com.gdschongik.gdsc.domain.studyv2.dao.AssignmentHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.domain.service.AssignmentHistoryGraderV2;
import com.gdschongik.gdsc.domain.studyv2.domain.service.AssignmentHistoryValidatorV2;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentAssignmentHistoryServiceV2 {

    private final MemberUtil memberUtil;
    private final GithubClient githubClient;
    private final StudyV2Repository studyV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;
    private final AssignmentHistoryV2Repository assignmentHistoryV2Repository;
    private final AssignmentHistoryValidatorV2 assignmentHistoryValidatorV2;
    private final AssignmentHistoryGraderV2 assignmentHistoryGraderV2;

    @Transactional
    public void submitMyAssignment(Long studySessionId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository
                .findFetchBySessionId(studySessionId)
                .orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        Optional<StudyHistoryV2> optionalStudyHistory =
                studyHistoryV2Repository.findByStudentAndStudy(currentMember, study);

        boolean isAppliedToStudy = optionalStudyHistory.isPresent();
        StudySessionV2 studySession = study.getStudySession(studySessionId);
        LocalDateTime now = LocalDateTime.now();

        assignmentHistoryValidatorV2.validateSubmitAvailable(isAppliedToStudy, studySession, now);

        String repositoryLink =
                optionalStudyHistory.map(StudyHistoryV2::getRepositoryLink).orElse(null);
        AssignmentSubmissionFetcher fetcher =
                githubClient.getLatestAssignmentSubmissionFetcher(repositoryLink, studySession.getPosition());
        AssignmentHistoryV2 assignmentHistory = findOrCreate(currentMember, studySession);

        assignmentHistoryGraderV2.judge(fetcher, assignmentHistory);

        assignmentHistoryV2Repository.save(assignmentHistory);

        log.info(
                "[StudentAssignmentHistoryServiceV2] 과제 제출: studySessionId={}, studentId={}, submissionStatus={}, submissionFailureType={}",
                studySessionId,
                currentMember.getId(),
                assignmentHistory.getSubmissionStatus(),
                assignmentHistory.getSubmissionFailureType());
    }

    private AssignmentHistoryV2 findOrCreate(Member student, StudySessionV2 studySession) {
        return assignmentHistoryV2Repository
                .findByMemberAndStudySession(student, studySession)
                .orElseGet(() -> AssignmentHistoryV2.create(studySession, student));
    }
}
