package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.MemberStatus;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findAll(MemberQueryRequest queryRequest, Pageable pageable) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(queryOption(queryRequest))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(member.count()).from(member).where(queryOption(queryRequest));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Member> findNormalByOauthId(String oauthId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(eqOauthId(oauthId), eqStatus(MemberStatus.NORMAL))
                .fetchOne());
    }

    @Override
    public Optional<Member> findVerifiedById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(eqId(id), requirementVerified())
                .fetchOne());
    }

    @Override
    public Page<Member> findAllGrantable(Pageable pageable) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(eqStatus(MemberStatus.NORMAL), eqRole(MemberRole.GUEST), requirementVerified())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(eqStatus(MemberStatus.NORMAL), eqRole(MemberRole.GUEST), requirementVerified());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqRole(MemberRole role) {
        return member.role.eq(role);
    }

    private BooleanBuilder requirementVerified() {
        return new BooleanBuilder().and(discordVerified()).and(univVerified()).and(paymentVerified());
    }

    private BooleanExpression discordVerified() {
        return member.requirement.discordStatus.eq(RequirementStatus.VERIFIED);
    }

    private BooleanExpression univVerified() {
        return member.requirement.univStatus.eq(RequirementStatus.VERIFIED);
    }

    private BooleanExpression paymentVerified() {
        return member.requirement.paymentStatus.eq(RequirementStatus.VERIFIED);
    }

    private BooleanExpression eqId(Long id) {
        return member.id.eq(id);
    }

    private BooleanExpression eqOauthId(String oauthId) {
        return member.oauthId.eq(oauthId);
    }

    private BooleanExpression eqStatus(MemberStatus status) {
        return member.status.eq(status);
    }

    private BooleanBuilder queryOption(MemberQueryRequest queryRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        return booleanBuilder
                .and(eqStudentId(queryRequest.studentId()))
                .and(eqName(queryRequest.name()))
                .and(eqPhone(queryRequest.phone()))
                .and(eqDepartment(queryRequest.department()))
                .and(eqEmail(queryRequest.email()))
                .and(eqDiscordUsername(queryRequest.discordUsername()))
                .and(eqNickname(queryRequest.nickname()));
    }

    private BooleanExpression eqStudentId(String studentId) {
        return studentId != null ? member.studentId.containsIgnoreCase(studentId) : null;
    }

    private BooleanExpression eqName(String name) {
        return name != null ? member.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression eqPhone(String phone) {
        return phone != null ? member.phone.number.contains(phone) : null;
    }

    private BooleanExpression eqDepartment(String department) {
        return department != null ? member.department.containsIgnoreCase(department) : null;
    }

    private BooleanExpression eqEmail(String email) {
        return email != null ? member.email.containsIgnoreCase(email) : null;
    }

    private BooleanExpression eqDiscordUsername(String discordUsername) {
        return discordUsername != null ? member.discordUsername.containsIgnoreCase(discordUsername) : null;
    }

    private BooleanExpression eqNickname(String nickname) {
        return nickname != null ? member.nickname.containsIgnoreCase(nickname) : null;
    }
}
