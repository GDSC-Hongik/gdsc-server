package com.gdschongik.gdsc.domain.order.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.dao.IssuedCouponRepository;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.dao.MembershipRepository;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.order.dao.OrderRepository;
import com.gdschongik.gdsc.domain.order.domain.MoneyInfo;
import com.gdschongik.gdsc.domain.order.domain.Order;
import com.gdschongik.gdsc.domain.order.domain.OrderValidator;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberUtil memberUtil;
    private final OrderRepository orderRepository;
    private final MembershipRepository membershipRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final OrderValidator orderValidator;

    @Transactional
    public void createPendingOrder(OrderCreateRequest request) {
        Membership membership = membershipRepository
                .findById(request.membershipId())
                .orElseThrow(() -> new CustomException(MEMBERSHIP_NOT_FOUND));

        IssuedCoupon issuedCoupon = request.issuedCouponId() == null ? null : getIssuedCoupon(request.issuedCouponId());

        MoneyInfo moneyInfo = MoneyInfo.of(
                Money.from(request.totalAmount()),
                Money.from(request.discountAmount()),
                Money.from(request.finalPaymentAmount()));

        Member currentMember = memberUtil.getCurrentMember();

        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember);

        Order order = Order.createPending(request.orderNanoId(), membership, issuedCoupon, moneyInfo);

        orderRepository.save(order);

        log.info("[OrderService] 임시 주문 생성: orderId={}", order.getId());
    }

    private IssuedCoupon getIssuedCoupon(Long issuedCouponId) {
        return issuedCouponRepository
                .findById(issuedCouponId)
                .orElseThrow(() -> new CustomException(ISSUED_COUPON_NOT_FOUND));
    }
}