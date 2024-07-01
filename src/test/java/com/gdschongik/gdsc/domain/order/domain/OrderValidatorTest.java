package com.gdschongik.gdsc.domain.order.domain;

import static com.gdschongik.gdsc.domain.member.domain.Department.*;
import static com.gdschongik.gdsc.domain.member.domain.Member.*;
import static com.gdschongik.gdsc.global.common.constant.MemberConstant.*;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.membership.domain.Membership;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.recruitment.domain.RoundType;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OrderValidatorTest {

    public static final Money MONEY_5000_WON = Money.from(BigDecimal.valueOf(5000));
    public static final Money MONEY_10000_WON = Money.from(BigDecimal.valueOf(10000));
    public static final Money MONEY_15000_WON = Money.from(BigDecimal.valueOf(15000));
    public static final Money MONEY_20000_WON = Money.from(BigDecimal.valueOf(20000));

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

    private Recruitment createRecruitment(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer academicYear,
            SemesterType semesterType,
            Money fee) {
        return Recruitment.createRecruitment(
                RECRUITMENT_NAME, startDate, endDate, academicYear, semesterType, RoundType.FIRST, fee);
    }

    private Membership createMembership(Member member, Recruitment recruitment) {
        return Membership.createMembership(member, recruitment);
    }

    private IssuedCoupon createAndIssue(Money money, Member member) {
        Coupon coupon = Coupon.createCoupon("테스트쿠폰", money);
        return IssuedCoupon.issue(coupon, member);
    }

    @Test
    void 멤버십_대상_멤버와_현재_로그인한_멤버_다르면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Member anotherMember = createAssociateMember(2L);
        Membership membership = createMembership(anotherMember, recruitment);

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_MEMBERSHIP_MEMBER_MISMATCH.getMessage());
    }

    @Test
    void 멤버십_회비납부상태_이미_충족되었으면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);
        membership.verifyPaymentStatus();

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_MEMBERSHIP_ALREADY_PAID.getMessage());
    }

    @Test
    void 리크루팅_모집기간이_아니면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        LocalDateTime invalidStartDate = LocalDateTime.now().minusDays(2);
        LocalDateTime invalidEndDate = LocalDateTime.now().minusDays(1);
        Recruitment recruitment =
                createRecruitment(invalidStartDate, invalidEndDate, 2024, SemesterType.FIRST, MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_RECRUITMENT_PERIOD_INVALID.getMessage());
    }

    @Test
    void 쿠폰_발급대상_멤버와_현재_로그인한_멤버_다르면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        Member anotherMember = createAssociateMember(2L);
        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, anotherMember);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_ISSUED_COUPON_MEMBER_MISMATCH.getMessage());
    }

    @Test
    void 회수된_발급쿠폰이면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);
        issuedCoupon.revoke();

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(COUPON_NOT_USABLE_REVOKED.getMessage());
    }

    @Test
    void 사용한_발급쿠폰이면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);
        issuedCoupon.use();

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(COUPON_NOT_USABLE_ALREADY_USED.getMessage());
    }

    @Test
    void 주문총액이_리크루팅_회비와_다르면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_15000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_TOTAL_AMOUNT_MISMATCH.getMessage());
    }

    @Test
    void 쿠폰_미사용시_할인금액이_0이_아니면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_5000_WON, MONEY_15000_WON);
        assertThatThrownBy(() -> orderValidator.validatePendingOrderCreate(membership, null, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_DISCOUNT_AMOUNT_NOT_ZERO.getMessage());
    }

    @Test
    void 쿠폰_사용시_할인금액이_쿠폰의_할인금액과_다르면_주문생성에_실패한다() {
        // given
        Member currentMember = createAssociateMember(1L);

        Recruitment recruitment = createRecruitment(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                2024,
                SemesterType.FIRST,
                MONEY_20000_WON);

        Membership membership = createMembership(currentMember, recruitment);

        IssuedCoupon issuedCoupon = createAndIssue(MONEY_5000_WON, currentMember);

        // when & then
        MoneyInfo moneyInfo = MoneyInfo.of(MONEY_20000_WON, MONEY_10000_WON, MONEY_10000_WON);
        assertThatThrownBy(() ->
                        orderValidator.validatePendingOrderCreate(membership, issuedCoupon, moneyInfo, currentMember))
                .isInstanceOf(CustomException.class)
                .hasMessage(ORDER_DISCOUNT_AMOUNT_MISMATCH.getMessage());
    }
}
