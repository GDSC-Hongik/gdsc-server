package com.gdschongik.gdsc.domain.order.dao;

import static com.gdschongik.gdsc.domain.member.domain.QMember.*;
import static com.gdschongik.gdsc.domain.order.domain.QOrder.*;
import static com.gdschongik.gdsc.domain.recruitment.domain.QRecruitmentRound.*;

import com.gdschongik.gdsc.domain.order.dto.request.OrderQueryOption;
import com.gdschongik.gdsc.domain.order.dto.response.OrderAdminResponse;
import com.gdschongik.gdsc.domain.order.dto.response.QOrderAdminResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository, OrderQueryMethod {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderAdminResponse> searchOrders(OrderQueryOption queryOption, Pageable pageable) {

        List<Long> ids = getIdsByQueryOption(queryOption, null, order.createdAt.desc());

        List<OrderAdminResponse> fetch = queryFactory
                .select(getOrderAdminResponse())
                .from(order)
                .join(member)
                .on(eqMember())
                .join(recruitmentRound)
                .on(eqRecruitmentRound())
                .where(order.id.in(ids))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(fetch, pageable, ids::size);
    }

    private QOrderAdminResponse getOrderAdminResponse() {
        return new QOrderAdminResponse(
                order.id,
                recruitmentRound.recruitment.semester.academicYear,
                recruitmentRound.recruitment.semester.semesterType,
                member.name,
                order.status,
                member.studentId,
                order.nanoId,
                order.paymentKey,
                order.moneyInfo.totalAmount,
                order.moneyInfo.discountAmount,
                order.moneyInfo.finalPaymentAmount,
                order.approvedAt);
    }

    private List<Long> getIdsByQueryOption(
            OrderQueryOption queryOption,
            @Nullable Predicate predicate,
            @NonNull OrderSpecifier<?>... orderSpecifiers) {
        return queryFactory
                .select(order.id)
                .from(order)
                .innerJoin(recruitmentRound)
                .on(order.recruitmentRoundId.eq(recruitmentRound.id))
                .innerJoin(member)
                .on(order.memberId.eq(member.id))
                .where(matchesOrderQueryOption(queryOption), predicate)
                .orderBy(orderSpecifiers)
                .fetch();
    }
}
