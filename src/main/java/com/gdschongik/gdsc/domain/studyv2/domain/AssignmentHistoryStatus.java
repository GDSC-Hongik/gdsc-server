package com.gdschongik.gdsc.domain.studyv2.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.ASSIGNMENT_HISTORY_NOT_WITHIN_PERIOD;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentHistoryStatus {
    BEFORE_SUBMISSION("제출 전"),
    FAILED("제출 실패"),
    SUCCEEDED("제출 성공"),
    ;

    private final String value;

    /**
     * 과제 제출 상태를 반환합니다. 제출기한 이내에 있는 제츌이력만 인자로 받습니다.
     * 제출기한에 포함되지 않는 제출이력은 제출기한 변경 전 제출이력이므로, 판정 대상에서 제외합니다.
     *
     * @throws CustomException 제출기한에 포함되지 않는 제출이력을 인자로 받았을 때
     */
    public static AssignmentHistoryStatus of(
            @Nullable AssignmentHistoryV2 assignmentHistory, StudySessionV2 studySession, LocalDateTime now)
            throws CustomException {
        Period assignmentPeriod = studySession.getAssignmentPeriod();

        if (now.isBefore(assignmentPeriod.getStartDate())) {
            return BEFORE_SUBMISSION;
        }

        // 과제 제출 이력이 없는 경우
        if (assignmentHistory == null) {
            return assignmentPeriod.isWithin(now) ? BEFORE_SUBMISSION : FAILED;
        }

        // 과제 제출 이력이 있는 경우
        validateCommittedAtWithinAssignmentPeriod(assignmentHistory, studySession);
        return assignmentHistory.isSucceeded() ? SUCCEEDED : FAILED;
    }

    private static void validateCommittedAtWithinAssignmentPeriod(
            AssignmentHistoryV2 assignmentHistory, StudySessionV2 studySession) throws CustomException {
        LocalDateTime committedAt = assignmentHistory.getCommittedAt();
        if (committedAt == null) {
            return;
        }

        Period assignmentPeriod = studySession.getAssignmentPeriod();

        if (!assignmentPeriod.isWithin(committedAt)) {
            throw new CustomException(ASSIGNMENT_HISTORY_NOT_WITHIN_PERIOD);
        }
    }
}
