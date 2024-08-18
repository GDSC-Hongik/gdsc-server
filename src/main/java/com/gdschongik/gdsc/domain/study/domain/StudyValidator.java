package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import jakarta.annotation.Nullable;
import java.util.Optional;

@DomainService
public class StudyValidator {
    public void validateStudyMentor(Member currentMember, Study study) {
        // 어드민인 경우 검증 통과
        if (currentMember.isAdmin()) {
            return;
        }

        // 멘토인지 검증
        if (!currentMember.isMentor()) {
            throw new CustomException(STUDY_ACCESS_NOT_ALLOWED);
        }

        // 해당 스터디의 담당 멘토인지 검증
        if (!currentMember.getId().equals(study.getMentor().getId())) {
            throw new CustomException(STUDY_MENTOR_INVALID);
        }
    }

    public void validateStudyMentorOrStudent(Member currentMember, Study study, Optional<StudyHistory> studyHistory) {
        // 어드민인 경우 검증 통과
        if (currentMember.isAdmin()) {
            return;
        }

        // 해당 스터디의 수강생인지 검증
        if (currentMember.isStudent() && studyHistory.isEmpty()) {
            throw new CustomException(STUDY_ACCESS_NOT_ALLOWED);
        }

        // 해당 스터디의 담당 멘토인지 검증
        if (!currentMember.getId().equals(study.getMentor().getId())) {
            throw new CustomException(STUDY_MENTOR_INVALID);
        }
    }
}
