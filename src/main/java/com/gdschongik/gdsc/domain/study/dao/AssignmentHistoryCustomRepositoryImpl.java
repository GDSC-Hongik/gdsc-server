package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionStatus.*;
import static com.gdschongik.gdsc.domain.study.domain.QAssignmentHistory.*;
import static com.gdschongik.gdsc.domain.study.domain.QStudyDetail.studyDetail;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignmentHistoryCustomRepositoryImpl implements AssignmentHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsSubmittedAssignmentByMemberAndStudy(Member member, Study study) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(assignmentHistory)
                .where(eqMember(member), eqStudy(study), isSubmitted())
                .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqMember(Member member) {
        return member == null ? null : assignmentHistory.member.eq(member);
    }

    private BooleanExpression eqStudy(Study study) {
        return study == null ? null : assignmentHistory.studyDetail.study.eq(study);
    }

    private BooleanExpression isSubmitted() {
        return assignmentHistory.submissionStatus.in(FAILURE, SUCCESS);
    }

    @Override
    public List<AssignmentHistory> findAssignmentHistoriesByStudentAndStudyId(Member currentMember, Long studyId) {
        return queryFactory
                .selectFrom(assignmentHistory)
                .join(assignmentHistory.studyDetail, studyDetail)
                .fetchJoin()
                .where(eqStudyId(studyId).and(eqMember(currentMember)))
                .fetch();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        return studyId != null ? studyDetail.study.id.eq(studyId) : null;
    }

    @Override
    public void deleteByStudyHistory(StudyHistory studyHistory) {
        queryFactory
                .delete(assignmentHistory)
                .where(eqMember(studyHistory.getStudent()).and(eqStudy(studyHistory.getStudy())))
                .execute();
    }
}
