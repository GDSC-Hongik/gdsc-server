package com.gdschongik.gdsc.domain.coupon.dao;

import static com.gdschongik.gdsc.domain.coupon.domain.QCoupon.coupon;

import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.dto.request.CouponQueryOption;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class CouponCustomRepositoryImpl extends CouponQueryMethod implements CouponCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Coupon> findAllCoupons(CouponQueryOption queryOption, Pageable pageable) {
        List<Coupon> fetch = queryFactory
                .selectFrom(coupon)
                .where(matchesQueryOption(queryOption))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(coupon.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory.select(coupon.count()).from(coupon).where(matchesQueryOption(queryOption));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }
}
