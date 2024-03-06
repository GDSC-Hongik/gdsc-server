package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;
import static com.querydsl.core.group.GroupBy.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.domain.RequirementStatus;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl extends MemberQueryMethod implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findNormalByOauthId(String oauthId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(member).where(eqOauthId(oauthId)).fetchOne());
    }

    @Override
    public Page<Member> findAllGrantable(MemberQueryRequest queryRequest, Pageable pageable) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(matchesQueryOption(queryRequest), eqRole(MemberRole.GUEST), isGrantAvailable())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(matchesQueryOption(queryRequest), eqRole(MemberRole.GUEST), isGrantAvailable());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Member> findAllByRole(MemberQueryRequest queryRequest, Pageable pageable, @Nullable MemberRole role) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(matchesQueryOption(queryRequest), eqRole(role), isStudentIdNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(matchesQueryOption(queryRequest), eqRole(role), isStudentIdNotNull());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Member> findAllByPaymentStatus(
            MemberQueryRequest queryRequest, RequirementStatus paymentStatus, Pageable pageable) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(
                        matchesQueryOption(queryRequest),
                        eqRequirementStatus(member.requirement.paymentStatus, paymentStatus),
                        isStudentIdNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        matchesQueryOption(queryRequest),
                        eqRequirementStatus(member.requirement.paymentStatus, paymentStatus),
                        isStudentIdNotNull());

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public Map<Boolean, List<Member>> groupByVerified(List<Long> memberIdList) {
        Map<Boolean, List<Member>> groupByVerified = queryFactory
                .selectFrom(member)
                .where(member.id.in(memberIdList))
                .transform(groupBy(requirementVerified()).as(list(member)));

        return replaceNullByEmptyList(groupByVerified);
    }

    private Map<Boolean, List<Member>> replaceNullByEmptyList(Map<Boolean, List<Member>> groupByVerified) {
        Map<Boolean, List<Member>> classifiedMember = new HashMap<>();
        List<Member> emptyList = new ArrayList<>();
        classifiedMember.put(true, groupByVerified.getOrDefault(true, emptyList));
        classifiedMember.put(false, groupByVerified.getOrDefault(false, emptyList));
        return classifiedMember;
    }

    @Override
    public List<Member> findAllByRole(MemberRole role) {
        return queryFactory
                .selectFrom(member)
                .where(eqRole(role), isStudentIdNotNull())
                .orderBy(member.studentId.asc(), member.name.asc())
                .fetch();
    }
}
