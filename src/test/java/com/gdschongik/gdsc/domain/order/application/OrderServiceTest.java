package com.gdschongik.gdsc.domain.order.application;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
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
import com.gdschongik.gdsc.domain.order.dto.request.OrderCancelRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCompleteRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderCreateRequest;
import com.gdschongik.gdsc.domain.order.dto.request.OrderQueryOption;
import com.gdschongik.gdsc.domain.order.dto.response.OrderAdminResponse;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.IntegrationTest;
import com.gdschongik.gdsc.infra.feign.payment.dto.request.PaymentCancelRequest;
import com.gdschongik.gdsc.infra.feign.payment.dto.request.PaymentConfirmRequest;
import com.gdschongik.gdsc.infra.feign.payment.dto.response.PaymentResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
                    RECRUITMENT_ROUND_NAME,
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
                    RECRUITMENT_ROUND_NAME,
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

        @Test
        void 멤버십의_회비납입상태가_SATISFIED로_변경된다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
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
            Membership verifiedMembership =
                    membershipRepository.findById(membership.getId()).orElseThrow();
            assertThat(verifiedMembership.getRegularRequirement().isPaymentSatisfied())
                    .isTrue();
        }

        @Test
        void 정회원으로_승급한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
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
            Member regularMember = memberRepository.findById(member.getId()).orElseThrow();
            assertThat(regularMember.isRegular()).isTrue();
        }
    }

    @Nested
    class 주문_취소할때 {

        @Test
        void 성공한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
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

            var completeRequest = new OrderCompleteRequest(paymentKey, orderNanoId, 15000L);
            orderService.completeOrder(completeRequest);

            Order completedOrder = orderRepository.findByNanoId(orderNanoId).orElseThrow();

            ZonedDateTime canceledAt = ZonedDateTime.now();
            PaymentResponse mockCancelResponse = mock(PaymentResponse.class);
            PaymentResponse.CancelDto mockCancelDto = mock(PaymentResponse.CancelDto.class);

            when(mockCancelResponse.cancels()).thenReturn(List.of(mockCancelDto));
            when(mockCancelDto.canceledAt()).thenReturn(canceledAt);
            when(paymentClient.cancelPayment(eq(paymentKey), any(PaymentCancelRequest.class)))
                    .thenReturn(mockCancelResponse);

            // when
            var cancelRequest = new OrderCancelRequest("테스트 취소 사유");
            orderService.cancelOrder(completedOrder.getId(), cancelRequest);

            // then
            Order canceledOrder =
                    orderRepository.findById(completedOrder.getId()).orElseThrow();
            assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCELED);
            assertThat(canceledOrder.getCanceledAt()).isNotNull();

            verify(paymentClient).cancelPayment(eq(paymentKey), any(PaymentCancelRequest.class));
        }

        @Test
        void 주문상태가_PENDING이면_실패한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    MONEY_20000_WON);

            Membership membership = createMembership(member, recruitmentRound);

            String orderNanoId = "HnbMWoSZRq3qK1W3tPXCW";
            orderService.createPendingOrder(new OrderCreateRequest(
                    orderNanoId,
                    membership.getId(),
                    null,
                    BigDecimal.valueOf(20000),
                    BigDecimal.valueOf(0),
                    BigDecimal.valueOf(20000)));

            Order pendingOrder = orderRepository.findByNanoId(orderNanoId).orElseThrow();
            Long id = pendingOrder.getId();

            OrderCancelRequest request = new OrderCancelRequest("테스트 취소 사유");

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(id, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_CANCEL_NOT_COMPLETED.getMessage());

            verify(paymentClient, never()).cancelPayment(any(), any());
        }
    }

    @Disabled // TODO: CI 환경에서만 실패하는 테스트, TZ 관련 설정 확인 필요
    @Nested
    class 일자기준으로_주문목록_조회시 {

        @Test
        void 조회된다() {
            // given
            Member member = createAssociateMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
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
            var request = new OrderCompleteRequest(paymentKey, orderNanoId, 15000L);
            orderService.completeOrder(request);

            LocalDate date = LocalDate.now();
            OrderQueryOption queryOption = new OrderQueryOption(null, null, null, null, null, null, null, date);

            // when
            Page<OrderAdminResponse> orderResponse = orderService.searchOrders(queryOption, PageRequest.of(0, 10));

            // then
            boolean orderExists = orderResponse.getContent().stream()
                    .anyMatch(order -> order.nanoId().equals(orderNanoId));

            assertThat(orderExists).isTrue();
        }
    }

    @Nested
    class 무료주문_생성할때 {

        @Test
        void 멤버십의_회비납입상태가_SATISFIED로_변경된다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    MONEY_20000_WON);

            Membership membership = createMembership(member, recruitmentRound);
            IssuedCoupon issuedCoupon = createAndIssue(MONEY_20000_WON, member);

            String orderNanoId = "HnbMWoSZRq3qK1W3tPXCW";

            var request = new OrderCreateRequest(
                    orderNanoId,
                    membership.getId(),
                    issuedCoupon.getId(),
                    BigDecimal.valueOf(20000),
                    BigDecimal.valueOf(20000),
                    BigDecimal.ZERO);

            // when
            orderService.createFreeOrder(request);

            // then
            Membership verifiedMembership =
                    membershipRepository.findById(membership.getId()).orElseThrow();
            assertThat(verifiedMembership.getRegularRequirement().isPaymentSatisfied())
                    .isTrue();
        }

        @Test
        void 정회원으로_승급한다() {
            // given
            Member member = createMember();
            logoutAndReloginAs(1L, MemberRole.ASSOCIATE);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    RECRUITMENT_ROUND_NAME,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    ACADEMIC_YEAR,
                    SEMESTER_TYPE,
                    ROUND_TYPE,
                    MONEY_20000_WON);

            Membership membership = createMembership(member, recruitmentRound);
            IssuedCoupon issuedCoupon = createAndIssue(MONEY_20000_WON, member);

            String orderNanoId = "HnbMWoSZRq3qK1W3tPXCW";

            var request = new OrderCreateRequest(
                    orderNanoId,
                    membership.getId(),
                    issuedCoupon.getId(),
                    BigDecimal.valueOf(20000),
                    BigDecimal.valueOf(20000),
                    BigDecimal.ZERO);

            // when
            orderService.createFreeOrder(request);

            // then
            Member regularMember = memberRepository.findById(member.getId()).orElseThrow();
            assertThat(regularMember.isRegular()).isTrue();
        }
    }
}
