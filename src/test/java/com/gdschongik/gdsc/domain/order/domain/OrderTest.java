package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    public static final Money MONEY_5000_WON = Money.from(5000L);
    public static final Money MONEY_10000_WON = Money.from(10000L);
    public static final Money MONEY_15000_WON = Money.from(15000L);
    public static final Money MONEY_20000_WON = Money.from(20000L);

    FixtureHelper fixtureHelper = new FixtureHelper();

    public Member createAssociateMember(Long id) {
        return fixtureHelper.createAssociateMember(id);
    }

    private RecruitmentRound createRecruitmentRound(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            Money fee) {
        return fixtureHelper.createRecruitmentRound(startDate, endDate, academicYear, semesterType, fee);
    }

    private Membership createMembership(Member member, RecruitmentRound recruitmentRound) {
        return fixtureHelper.createMembership(member, recruitmentRound);
    }

    @Nested
    class 무료주문_생성할때 {

        @Test
        void 주문상태는_완료이다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2021,
                    SemesterType.FIRST,
                    MONEY_10000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);
            MoneyInfo freeMoneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_20000_WON, Money.ZERO);

            // when
            Order order = Order.createFree("testNanoId", freeMoneyInfo, membership, null);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        }

        @Test
        void 최종결제금액이_0원이_아니면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2021,
                    SemesterType.FIRST,
                    MONEY_10000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);
            MoneyInfo freeMoneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_15000_WON, MONEY_5000_WON);

            // when & then
            assertThatThrownBy(() -> Order.createFree("testNanoId", freeMoneyInfo, membership, null))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_FREE_FINAL_PAYMENT_NOT_ZERO.getMessage());
        }
    }

    @Nested
    class 주문_취소할때 {

        @Test
        void 대기상태이면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2021,
                    SemesterType.FIRST,
                    MONEY_10000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            Order order = Order.createPending(
                    "testNanoId", MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON), membership, null);

            ZonedDateTime canceledAt = ZonedDateTime.now();

            // when
            assertThatThrownBy(() -> order.cancel(canceledAt))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_CANCEL_NOT_COMPLETED.getMessage());
        }

        @Test
        void 취소상태이면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2021,
                    SemesterType.FIRST,
                    MONEY_10000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            Order order = Order.createPending(
                    "testNanoId", MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON), membership, null);
            order.complete("testPaymentKey", ZonedDateTime.now());
            order.cancel(ZonedDateTime.now());

            ZonedDateTime canceledAt = ZonedDateTime.now();

            // when & then
            assertThatThrownBy(() -> order.cancel(canceledAt))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_CANCEL_NOT_COMPLETED.getMessage());
        }

        @Test
        void 무료주문이면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2021,
                    SemesterType.FIRST,
                    MONEY_10000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);
            MoneyInfo freeMoneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_20000_WON, Money.ZERO);

            Order order = Order.createFree("testNanoId", freeMoneyInfo, membership, null);

            ZonedDateTime canceledAt = ZonedDateTime.now();

            // when & then
            assertThatThrownBy(() -> order.cancel(canceledAt))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_CANCEL_FREE_ORDER.getMessage());
        }

        @Test
        void 완료상태이면_성공한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2021,
                    SemesterType.FIRST,
                    MONEY_10000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            Order order = Order.createPending(
                    "testNanoId", MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON), membership, null);
            order.complete("testPaymentKey", ZonedDateTime.now());

            ZonedDateTime canceledAt = ZonedDateTime.now();

            // when
            order.cancel(canceledAt);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
            assertThat(order.getCanceledAt()).isEqualTo(canceledAt);
        }
    }
}
