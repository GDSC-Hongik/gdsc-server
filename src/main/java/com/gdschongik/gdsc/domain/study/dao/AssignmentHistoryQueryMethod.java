package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.QAssignmentHistory.*;
import static com.gdschongik.gdsc.domain.study.domain.QStudyDetail.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface AssignmentHistoryQueryMethod {
    default BooleanExpression eqMember(Member member) {
        return member == null ? null : assignmentHistory.member.eq(member);
    }

    default BooleanExpression eqStudy(Study study) {
        return study == null ? null : assignmentHistory.studyDetail.study.eq(study);
    }

    default BooleanExpression isSubmitted() {
        return assignmentHistory.submissionStatus.in(FAILURE, SUCCESS);
    }

    default BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? studyDetail.study.id.eq(studyId) : null;
    }

    default BooleanExpression eqMemberId(Long memberId) {
        return memberId != null ? assignmentHistory.member.id.eq(memberId) : null;
    }
}
