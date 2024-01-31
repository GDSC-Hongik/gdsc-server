package com.gdschongik.gdsc.domain.member.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
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
    public Page<Member> findAll(String keyword, String type, Pageable pageable) {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .where(nameContains(keyword, type))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(member.count()).from(member).where(nameContains(keyword, type));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameContains(String keyword, String type) {
        if (keyword != null && type != null) {
            return switch (type) {
                case "student-id" -> member.studentId.containsIgnoreCase(keyword);
                case "name" -> member.name.containsIgnoreCase(keyword);
                case "phone" -> member.phone.containsIgnoreCase(keyword);
                case "department" -> member.department.containsIgnoreCase(keyword);
                case "email" -> member.email.containsIgnoreCase(keyword);
                case "discord-username" -> member.discordUsername.containsIgnoreCase(keyword);
                case "discord-nickname" -> member.nickname.containsIgnoreCase(keyword);
                default -> throw new CustomException(ErrorCode.INVALID_QUERY_PARAMETER);
            };
        }

        return null;
    }
}
