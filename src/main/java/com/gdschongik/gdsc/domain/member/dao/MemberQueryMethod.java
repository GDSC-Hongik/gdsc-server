package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;
import static com.gdschongik.gdsc.domain.member.domain.RequirementStatus.*;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import java.util.List;

public class MemberQueryMethod {

    protected BooleanExpression eqRole(MemberRole role) {
        return role != null ? member.role.eq(role) : null;
    }

    protected BooleanExpression eqStudentId(String studentId) {
        return studentId != null ? member.studentId.containsIgnoreCase(studentId) : null;
    }

    protected BooleanExpression eqName(String name) {
        return name != null ? member.name.containsIgnoreCase(name) : null;
    }

    protected BooleanExpression eqPhone(String phone) {
        return phone != null ? member.phone.contains(phone.replaceAll("-", "")) : null;
    }

    protected BooleanExpression eqEmail(String email) {
        return email != null ? member.email.containsIgnoreCase(email) : null;
    }

    protected BooleanExpression eqDiscordUsername(String discordUsername) {
        return discordUsername != null ? member.discordUsername.containsIgnoreCase(discordUsername) : null;
    }

    protected BooleanExpression eqNickname(String nickname) {
        return nickname != null ? member.nickname.containsIgnoreCase(nickname) : null;
    }

    protected BooleanExpression eqOauthId(String oauthId) {
        return member.oauthId.eq(oauthId);
    }

    protected BooleanExpression eqRequirementStatus(
            EnumPath<RequirementStatus> requirement, RequirementStatus requirementStatus) {
        return requirementStatus != null ? requirement.eq(requirementStatus) : null;
    }

    protected BooleanExpression inDepartmentList(List<Department> departmentCodes) {
        return departmentCodes != null ? member.department.in(departmentCodes) : null;
    }

    protected BooleanExpression isStudentIdNotNull() {
        return member.studentId.isNotNull();
    }

    protected BooleanBuilder isGrantAvailable() {
        return new BooleanBuilder()
                .and(eqRequirementStatus(member.requirement.discordStatus, VERIFIED))
                .and(eqRequirementStatus(member.requirement.univStatus, VERIFIED))
                .and(eqRequirementStatus(member.requirement.paymentStatus, VERIFIED))
                .and(eqRequirementStatus(member.requirement.bevyStatus, VERIFIED));
    }

    protected BooleanBuilder matchesQueryOption(MemberQueryRequest queryRequest) {
        return new BooleanBuilder()
                .and(eqStudentId(queryRequest.studentId()))
                .and(eqName(queryRequest.name()))
                .and(eqPhone(queryRequest.phone()))
                .and(inDepartmentList(Department.searchDepartments(queryRequest.department())))
                .and(eqEmail(queryRequest.email()))
                .and(eqDiscordUsername(queryRequest.discordUsername()))
                .and(eqNickname(queryRequest.nickname()));
    }
}
