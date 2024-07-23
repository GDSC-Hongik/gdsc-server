package com.gdschongik.gdsc.domain.order.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.order.dao.OrderRepository;
import com.gdschongik.gdsc.domain.order.domain.Order;
import com.gdschongik.gdsc.domain.order.domain.OrderStatus;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCompleteRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCreateRequest;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.helper.IntegrationTest;
import com.gdschongik.gdsc.infra.feign.payment.dto.request.PaymentConfirmRequest;
import com.gdschongik.gdsc.infra.feign.payment.dto.response.PaymentResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    public static final Money MONEY_20000_WON = Money.from(20000L);
    public static final Money MONEY_15000_WON = Money.from(15000L);
    public static final Money MONEY_10000_WON = Money.from(10000L);
    public static final Money MONEY_5000_WON = Money.from(5000L);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    class 임시주문_생성할때 {

        @Test
        void 성공한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_NAME,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    MONEY_20000_WON);

            Membership membership = createMembership(member, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, member);

            // when
            var request = new OrderCreateRequest(
                    "HnbMWoSZRq3qK1W3tPXCW",
                    membership.getId(),
                    issuedCoupon.getId(),
                    BigDecimal.valueOf(20000),
                    BigDecimal.valueOf(5000),
                    BigDecimal.valueOf(15000));
            orderService.createPendingOrder(request);

            // then
            assertThat(orderRepository.findAll()).hasSize(1);
        }
    }

    @Nested
    class 주문_완료할때 {

        @Test
        void 성공한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_NAME,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    MONEY_20000_WON);

            Membership membership = createMembership(member, recruitmentRound);
            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, member);

            String orderNanoId = "HnbMWoSZRq3qK1W3tPXCW";
            orderService.createPendingOrder(new OrderCreateRequest(
                    orderNanoId,
                    membership.getId(),
                    issuedCoupon.getId(),
                    BigDecimal.valueOf(20000),
                    BigDecimal.valueOf(5000),
                    BigDecimal.valueOf(15000)));

            String paymentKey = "testPaymentKey";

            ZonedDateTime approvedAt = ZonedDateTime.now();
            PaymentResponse mockPaymentResponse = mock(PaymentResponse.class);
            when(mockPaymentResponse.approvedAt()).thenReturn(approvedAt);
            when(paymentClient.confirm(any(PaymentConfirmRequest.class))).thenReturn(mockPaymentResponse);

            // when
            var request = new OrderCompleteRequest(paymentKey, orderNanoId, 15000L);
            orderService.completeOrder(request);

            // then
            Order completedOrder = orderRepository.findByNanoId(orderNanoId).orElseThrow();
            assertThat(completedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
            assertThat(completedOrder.getPaymentKey()).isEqualTo(paymentKey);

            IssuedCoupon usedCoupon =
                    issuedCouponRepository.findById(issuedCoupon.getId()).orElseThrow();
            assertThat(usedCoupon.hasUsed()).isTrue();

            verify(paymentClient).confirm(any(PaymentConfirmRequest.class));
        }
    }
}
