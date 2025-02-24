package com.gdschongik.gdsc.domain.studyv2.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.annotation.DomainService;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.Objects;

@DomainService
public class StudyHistoryValidatorV2 {

    public void validateAppliedToStudy(long countStudyHistory, int studentCount) {
        if (countStudyHistory != studentCount) {
            throw new CustomException(STUDY_HISTORY_NOT_APPLIED_STUDENT_EXISTS);
        }
    }

    public void validateUpdateRepository(String repositoryOwnerGithubId, Member currentMember) {
        // 레포지토리 소유자가 현 멤버가 아닌 경우
        String currentMemberGithubId = currentMember.getOauthId();

        if (!Objects.equals(repositoryOwnerGithubId, currentMemberGithubId)) {
            throw new CustomException(STUDY_HISTORY_REPOSITORY_NOT_UPDATABLE_OWNER_MISMATCH);
        }
    }
}
