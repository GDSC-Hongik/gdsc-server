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
import com.gdschongik.gdsc.domain.order.dto.request.OrderCompleteRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCreateRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderQueryOption;
import com.gdschongik.gdsc.domain.order.dto.response.OrderAdminResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.feign.payment.client.PaymentClient;
import com.gdschongik.gdsc.infra.feign.payment.dto.request.PaymentConfirmRequest;
import com.gdschongik.gdsc.infra.feign.payment.dto.response.PaymentResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentClient paymentClient;
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

    @Transactional
    public void completeOrder(OrderCompleteRequest request) {
        Order order = orderRepository
                .findByNanoId(request.orderNanoId())
                .orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));

        Optional<IssuedCoupon> issuedCoupon =
                Optional.ofNullable(order.getIssuedCouponId()).map(this::getIssuedCoupon);

        Member currentMember = memberUtil.getCurrentMember();

        Money requestedAmount = Money.from(request.amount());

        orderValidator.validateCompleteOrder(order, issuedCoupon, currentMember, requestedAmount);

        var paymentRequest = new PaymentConfirmRequest(request.paymentKey(), order.getNanoId(), request.amount());
        PaymentResponse response = paymentClient.confirm(paymentRequest);

        order.complete(request.paymentKey(), response.approvedAt());
        issuedCoupon.ifPresent(IssuedCoupon::use);

        orderRepository.save(order);

        log.info("[OrderService] 주문 완료: orderId={}", order.getId());
    }

    @Transactional(readOnly = true)
    public Page<OrderAdminResponse> searchOrders(OrderQueryOption queryOption, Pageable pageable) {
        return orderRepository.searchOrders(queryOption, pageable);
    }
}
