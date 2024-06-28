package com.gdschongik.gdsc.domain.order.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.order.dao.OrderRepository;
import com.gdschongik.gdsc.domain.order.domain.Order;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createPendingOrder(OrderCreateRequest request) {

        Member member =
                memberRepository.findById(request.memberId()).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        IssuedCoupon issuedCoupon = issuedCouponRepository
                .findById(request.issuedCouponId())
                .orElseThrow(() -> new CustomException(ISSUED_COUPON_NOT_FOUND));

        Order order = Order.createPending(
                request.nanoId(),
                member,
                issuedCoupon,
                Money.from(request.totalAmount()),
                Money.from(request.discountAmount()),
                Money.from(request.finalPaymentAmount()));

        orderRepository.save(order);

        log.info("[OrderService] 임시 주문 생성: orderId={}", order.getId());
    }
}
