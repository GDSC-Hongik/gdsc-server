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
        boolean isInOngoingStudy = currentMemberStudyHistories.stream().anyMatch(StudyHistory::isStudyOngoing);

        if (isInOngoingStudy) {
            throw new CustomException(STUDY_HISTORY_ONGOING_ALREADY_EXISTS);
        }
    }
}
