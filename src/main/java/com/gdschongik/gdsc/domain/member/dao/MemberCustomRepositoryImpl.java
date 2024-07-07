package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.common.model.RequirementStatus.*;
import static com.gdschongik.gdsc.domain.member.domain.QMember.*;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl extends MemberQueryMethod implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findAllByRole(MemberQueryOption queryOption, Pageable pageable, @Nullable MemberRole role) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(matchesQueryOption(queryOption), eqRole(role))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(member.count()).from(member).where(matchesQueryOption(queryOption), eqRole(role));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Member> findAllByRole(MemberRole role) {
        return queryFactory
                .selectFrom(member)
                .where(eqRole(role))
                .orderBy(member.studentId.asc(), member.name.asc())
                .fetch();
    }

    @Override
    public List<Member> findAllByDiscordStatus(RequirementStatus discordStatus) {
        return queryFactory
                .selectFrom(member)
                .where(eqRequirementStatus(member.associateRequirement.discordStatus, discordStatus))
                .fetch();
    }

    @Override
    public Page<Member> findAssociateOrRegularMembers(MemberQueryOption queryOption, Pageable pageable) {
        List<Long> ids = getIdsByQueryOption(queryOption, eqRole(MemberRole.ASSOCIATE).or(eqRole(MemberRole.REGULAR)), member.createdAt.desc());

        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(member.id.in(ids))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(fetch, pageable, ids::size);
    }

    private List<Long> getIdsByQueryOption(MemberQueryOption queryOption, Predicate predicate, OrderSpecifier<?>... orderSpecifiers) {
        return queryFactory.select(member.id)
                .from(member)
                .where(matchesQueryOption(queryOption), predicate)
                .orderBy(orderSpecifiers)
                .fetch();
    }
}
