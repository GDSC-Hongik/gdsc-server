package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberQueryOption;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> findAll(String keyword, MemberQueryOption queryOption, Pageable pageable) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(queryOption(keyword, queryOption))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(member.count()).from(member).where(queryOption(keyword, queryOption));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    private BooleanExpression queryOption(String keyword, MemberQueryOption queryOption) {
        if (keyword != null && queryOption != null) {
            return switch (queryOption) {
                case STUDENT_ID -> member.studentId.containsIgnoreCase(keyword);
                case NAME -> member.name.containsIgnoreCase(keyword);
                case PHONE -> member.phone.containsIgnoreCase(keyword);
                case DEPARTMENT -> member.department.containsIgnoreCase(keyword);
                case EMAIL -> member.email.containsIgnoreCase(keyword);
                case DISCORD_USERNAME -> member.discordUsername.containsIgnoreCase(keyword);
                case DISCORD_NICKNAME -> member.nickname.containsIgnoreCase(keyword);
            };
        }

        return null;
    }
}
