package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;

@DomainService
public class StudyAchievementValidator {

    public void validateDesignateOutstandingStudent(long countStudyAchievementsAlreadyExist) {
        // 이미 우수 스터디원으로 지정된 스터디원이 있는 경우
        if (countStudyAchievementsAlreadyExist > 0) {
            throw new CustomException(STUDY_ACHIEVEMENT_ALREADY_EXISTS);
        }
    }

    public void validateWithdrawOutstandingStudent(long countStudyAchievements, long countRequestedStudent) {
        // 요청한 우수 스터디원 수와 실제 우수 스터디원 수가 다른 경우
        if (countStudyAchievements != countRequestedStudent) {
            throw new CustomException(STUDY_ACHIEVEMENT_COUNT_MISMATCH);
        }
    }
}
