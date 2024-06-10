package com.gdschongik.gdsc.domain.membership.dao;

import static com.gdschongik.gdsc.domain.membership.domain.QMembership.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

public class MembershipQueryMethod {

    protected BooleanExpression eqMember(Member member) {
        return member != null ? membership.member.eq(member) : null;
    }

    protected BooleanExpression eqAcademicYear(Integer academicYear) {
        return academicYear != null ? membership.academicYear.eq(academicYear) : null;
    }

    protected BooleanExpression eqSemesterType(SemesterType semesterType) {
        return semesterType != null ? membership.semesterType.eq(semesterType) : null;
    }

    protected BooleanExpression isPaymentVerified() {
        return membership.paymentStatus.eq(RequirementStatus.VERIFIED);
    }
}
