package com.gdschongik.gdsc.domain.order.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;
import static com.gdschongik.gdsc.domain.order.domain.QOrder.*;
import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitment.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDateTime;

public interface OrderQueryMethod {

    // TODO: MemberQueryMethod가 interface로 변경된 경우 해당 메서드 제거 및 대체
    default BooleanExpression eqName(String name) {
        return name != null ? member.name.contains(name) : null;
    }

    default BooleanExpression eqAcademicYear(Integer academicYear) {
        return academicYear != null ? recruitment.academicYear.eq(academicYear) : null;
    }

    default BooleanExpression eqSemesterType(SemesterType semesterType) {
        return semesterType != null ? recruitment.semesterType.eq(semesterType) : null;
    }

    default BooleanExpression eqStudentId(String studentId) {
        return studentId != null ? member.studentId.containsIgnoreCase(studentId) : null;
    }

    default BooleanExpression eqNanoId(String nanoId) {
        return nanoId != null ? order.nanoId.contains(nanoId) : null;
    }

    default BooleanExpression eqPaymentKey(String paymentKey) {
        return paymentKey != null ? order.paymentKey.contains(paymentKey) : null;
    }

    default BooleanExpression eqApprovedAt(LocalDateTime approvedAt) {
        return approvedAt != null ? order.approvedAt.eq(approvedAt) : null;
    }
}
