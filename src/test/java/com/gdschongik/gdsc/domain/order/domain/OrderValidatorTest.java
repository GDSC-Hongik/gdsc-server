package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.Member.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.SemesterConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OrderValidatorTest {

    public static final Money MONEY_0_WON = Money.from(0L);
    public static final Money MONEY_5000_WON = Money.from(5000L);
    public static final Money MONEY_10000_WON = Money.from(10000L);
    public static final Money MONEY_15000_WON = Money.from(15000L);
    public static final Money MONEY_20000_WON = Money.from(20000L);

    OrderValidator orderValidator = new OrderValidator();

    private Member createAssociateMember(Long id) {
        Member member = createGuestMember(OAUTH_ID);
        member.updateBasicMemberInfo(STUDENT_ID, NAME, PHONE_NUMBER, D022, EMAIL);
        member.completeUnivEmailVerification(UNIV_EMAIL);
        member.verifyDiscord(DISCORD_USERNAME, NICKNAME);
        member.verifyBevy();
        member.advanceToAssociate();
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    private RecruitmentRound createRecruitmentRound(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            Money fee) {
        Recruitment recruitment = Recruitment.createRecruitment(
                academicYear, semesterType, fee, FEE_NAME, Period.createPeriod(SEMESTER_START_DATE, SEMESTER_END_DATE));

        return RecruitmentRound.create(RECRUITMENT_NAME, startDate, endDate, recruitment, RoundType.FIRST);
    }

    private Membership createMembership(Member member, RecruitmentRound recruitmentRound) {
        return Membership.createMembership(member, recruitmentRound);
    }

    private IssuedCoupon createAndIssue(Money money, Member member) {
        Coupon coupon = Coupon.createCoupon("테스트쿠폰", money);
        return IssuedCoupon.issue(coupon, member);
    }

    @Nested
    class 임시주문_생성_검증할때 {

        @Test
        void 멤버십_대상_멤버와_현재_로그인한_멤버_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Member anotherMember = createAssociateMember(2L);
            Membership membership = createMembership(anotherMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_MEMBERSHIP_MEMBER_MISMATCH.getMessage());
        }

        @Test
        void 멤버십_회비납부상태_이미_충족되었으면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);
            membership.verifyPaymentStatus();

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_MEMBERSHIP_ALREADY_PAID.getMessage());
        }

        @Test
        void 리크루팅_모집기간이_아니면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            LocalDateTime invalidStartDate = LocalDateTime.now().minusDays(2);
            LocalDateTime invalidEndDate = LocalDateTime.now().minusDays(1);
            RecruitmentRound recruitmentRound =
                    createRecruitmentRound(invalidStartDate, invalidEndDate, 2024, SemesterType.FIRST, MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_RECRUITMENT_PERIOD_INVALID.getMessage());
        }

        @Test
        void 쿠폰_발급대상_멤버와_현재_로그인한_멤버_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            Member anotherMember = createAssociateMember(2L);
            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, anotherMember);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_ISSUED_COUPON_MEMBER_MISMATCH.getMessage());
        }

        @Test
        void 회수된_발급쿠폰이면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);
            issuedCoupon.revoke();

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(COUPON_NOT_USABLE_REVOKED.getMessage());
        }

        @Test
        void 사용한_발급쿠폰이면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);
            issuedCoupon.use();

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(COUPON_NOT_USABLE_ALREADY_USED.getMessage());
        }

        @Test
        void 주문총액이_리크루팅_회비와_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_15000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_TOTAL_AMOUNT_MISMATCH.getMessage());
        }

        @Test
        void 쿠폰_미사용시_할인금액이_0이_아니면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);

            // when & then
            assertThatThrownBy(
                            () -> orderValidator.validatePendingOrderCreate(membership, null, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_DISCOUNT_AMOUNT_NOT_ZERO.getMessage());
        }

        @Test
        void 쿠폰_사용시_할인금액이_쿠폰의_할인금액과_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);

            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);

            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

            MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_10000_WON, MONEY_10000_WON);

            // when & then
            assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(
                            membership, issuedCoupon, moneyInfo, currentMember))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_DISCOUNT_AMOUNT_MISMATCH.getMessage());
        }
    }

    @Nested
    class 주문_완료_검증할때 {

        @Test
        void 이미_완료된_주문이면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            Order completedOrder = Order.createPending(
                    "nanoId", membership, null, MoneyInfo.of(MONEY_20000_WON, MONEY_0_WON, MONEY_20000_WON));
            completedOrder.complete("paymentKey");

            Optional<IssuedCoupon> emptyIssuedCoupon = Optional.empty();

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCompleteOrder(
                            completedOrder, emptyIssuedCoupon, currentMember, MONEY_20000_WON))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_ALREADY_COMPLETED.getMessage());
        }

        @Test
        void 발급쿠폰이_사용_불가능하면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);
            issuedCoupon.use(); // 쿠폰을 사용 불가능한 상태로 만듦

            Order order = Order.createPending(
                    "nanoId", membership, issuedCoupon, MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON));

            Optional<IssuedCoupon> optionalIssuedCoupon = Optional.of(issuedCoupon);

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCompleteOrder(
                            order, optionalIssuedCoupon, currentMember, MONEY_15000_WON))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(COUPON_NOT_USABLE_ALREADY_USED.getMessage());
        }

        @Test
        void 발급쿠폰_소유자와_현재_멤버가_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            Member anotherMember = createAssociateMember(2L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, anotherMember);

            Order order = Order.createPending(
                    "nanoId", membership, issuedCoupon, MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON));

            Optional<IssuedCoupon> optionalIssuedCoupon = Optional.of(issuedCoupon);

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCompleteOrder(
                            order, optionalIssuedCoupon, currentMember, MONEY_15000_WON))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_ISSUED_COUPON_MEMBER_MISMATCH.getMessage());
        }

        @Test
        void 주문_멤버십_멤버와_현재_멤버가_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            Member anotherMember = createAssociateMember(2L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);
            Membership membership = createMembership(anotherMember, recruitmentRound);

            Order order = Order.createPending(
                    "nanoId", membership, null, MoneyInfo.of(MONEY_20000_WON, MONEY_0_WON, MONEY_20000_WON));

            Optional<IssuedCoupon> emptyIssuedCoupon = Optional.empty();

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCompleteOrder(
                            order, emptyIssuedCoupon, currentMember, MONEY_20000_WON))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_MEMBERSHIP_MEMBER_MISMATCH.getMessage());
        }

        @Test
        void 요청된_금액이_주문의_최종_결제_금액과_다르면_실패한다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            Order order = Order.createPending(
                    "nanoId", membership, null, MoneyInfo.of(MONEY_20000_WON, MONEY_0_WON, MONEY_20000_WON));

            Optional<IssuedCoupon> emptyIssuedCoupon = Optional.empty();

            // when & then
            assertThatThrownBy(() -> orderValidator.validateCompleteOrder(
                            order, emptyIssuedCoupon, currentMember, MONEY_15000_WON))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ORDER_COMPLETE_AMOUNT_MISMATCH.getMessage());
        }

        @Test
        void 모든_검증을_통과하면_예외가_발생하지_않는다() {
            // given
            Member currentMember = createAssociateMember(1L);
            RecruitmentRound recruitmentRound = createRecruitmentRound(
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    2024,
                    SemesterType.FIRST,
                    MONEY_20000_WON);
            Membership membership = createMembership(currentMember, recruitmentRound);

            IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);
            Order order = Order.createPending(
                    "nanoId", membership, issuedCoupon, MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON));

            Optional<IssuedCoupon> optionalIssuedCoupon = Optional.of(issuedCoupon);

            // when & then
            assertThatCode(() -> orderValidator.validateCompleteOrder(
                            order, optionalIssuedCoupon, currentMember, MONEY_15000_WON))
                    .doesNotThrowAnyException();
        }
    }
}
