package com.gdschongik.gdsc.domain.coupon.dao;

import static com.gdschongik.gdsc.domain.coupon.domain.QIssuedCoupon.issuedCoupon;

import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponQueryOption;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class IssuedCouponCustomRepositoryImpl extends IssuedCouponQueryMethod implements IssuedCouponCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<IssuedCoupon> findAllIssuedCoupons(CouponQueryOption queryOption, Pageable pageable) {
        List<IssuedCoupon> fetch = queryFactory
                .selectFrom(issuedCoupon)
                .where(matchesQueryOption(queryOption))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(issuedCoupon.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(issuedCoupon.count()).from(issuedCoupon).where(matchesQueryOption(queryOption));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }
}
