package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.domain.member.domain.QMember.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
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
        return departmentCodes.isEmpty() ? null : member.department.in(departmentCodes);
    }

    protected BooleanExpression isStudentIdNotNull() {
        return member.studentId.isNotNull();
    }

    protected BooleanBuilder isGrantAvailable() {
        return new BooleanBuilder()
                .and(eqRequirementStatus(member.associateRequirement.discordStatus, VERIFIED))
                .and(eqRequirementStatus(member.associateRequirement.univStatus, VERIFIED))
                .and(eqRequirementStatus(member.associateRequirement.paymentStatus, VERIFIED))
                .and(eqRequirementStatus(member.associateRequirement.bevyStatus, VERIFIED));
    }

    protected BooleanBuilder isAssociateAvailable() {
        return new BooleanBuilder()
                .and(eqRequirementStatus(member.associateRequirement.discordStatus, VERIFIED))
                .and(eqRequirementStatus(member.associateRequirement.univStatus, VERIFIED))
                .and(eqRequirementStatus(member.associateRequirement.infoStatus, VERIFIED))
                .and(eqRequirementStatus(member.associateRequirement.bevyStatus, VERIFIED));
    }

    protected BooleanBuilder matchesQueryOption(MemberQueryOption queryOption) {
        return new BooleanBuilder()
                .and(eqStudentId(queryOption.studentId()))
                .and(eqName(queryOption.name()))
                .and(eqPhone(queryOption.phone()))
                .and(inDepartmentList(Department.searchDepartments(queryOption.department())))
                .and(eqEmail(queryOption.email()))
                .and(eqDiscordUsername(queryOption.discordUsername()))
                .and(eqNickname(queryOption.nickname()));
    }
}
