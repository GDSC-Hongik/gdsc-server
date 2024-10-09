package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;

@DomainService
public class StudyHistoryValidator {

    public void validateApplyStudy(Study study, List<StudyHistory> currentMemberStudyHistories) {
        // 이미 해당 스터디에 수강신청한 경우
        boolean isStudyHistoryDuplicate = currentMemberStudyHistories.stream()
                .anyMatch(studyHistory -> studyHistory.getStudy().equals(study));

        if (isStudyHistoryDuplicate) {
            throw new CustomException(STUDY_HISTORY_DUPLICATE);
        }

        // 스터디 수강신청 기간이 아닌 경우
        if (!study.isApplicable()) {
            throw new CustomException(STUDY_NOT_APPLICABLE);
        }

        // 이미 듣고 있는 스터디가 있는 경우
        boolean hasAppliedStudy =
                currentMemberStudyHistories.stream().anyMatch(StudyHistory::isWithinApplicationAndCourse);

        if (hasAppliedStudy) {
            throw new CustomException(STUDY_HISTORY_ONGOING_ALREADY_EXISTS);
        }
    }

    public void validateCancelStudyApply(Study study) {
        // 스터디 수강신청 기간이 아닌 경우
        if (!study.isApplicable()) {
            throw new CustomException(STUDY_NOT_CANCELABLE_APPLICATION_PERIOD);
        }
    }

    public void validateUpdateRepository(
            boolean isAnyAssignmentSubmitted, String repositoryOwnerOauthId, String currentMemberOauthId) {
        // 이미 제출한 과제가 있는 경우
        if (isAnyAssignmentSubmitted) {
            throw new CustomException(STUDY_HISTORY_REPOSITORY_NOT_UPDATABLE_ASSIGNMENT_ALREADY_SUBMITTED);
        }

        // 레포지토리 소유자가 현 멤버가 아닌 경우
        if (!repositoryOwnerOauthId.equals(currentMemberOauthId)) {
            throw new CustomException(STUDY_HISTORY_REPOSITORY_NOT_UPDATABLE_OWNER_MISMATCH);
        }
    }

    public void validateAppliedToStudy(boolean isAllAppliedToStudy) {
        if (!isAllAppliedToStudy) {
            throw new CustomException(STUDY_HISTORY_NOT_APPLIED_STUDENT_EXISTS);
        }
    }
}
