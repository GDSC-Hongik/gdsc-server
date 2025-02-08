package com.gdschongik.gdsc.domain.coupon.application;

import static com.gdschongik.gdsc.global.common.constant.CouponConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Money;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.coupon.domain.Coupon;
import com.gdschongik.gdsc.domain.coupon.domain.CouponType;
import com.gdschongik.gdsc.domain.coupon.domain.IssuedCoupon;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoriesCompletedEvent;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryCompletionWithdrawnEvent;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CouponEventHandlerTest extends IntegrationTest {

    @Autowired
    private CouponEventHandler couponEventHandler;

    @Nested
    class 스터디_수료_이벤트_수신시 {

        @Test
        void 발급쿠폰을_생성한다() {
            // given
            Member student = createRegularMember();
            Member mentor = createMentor();
            LocalDateTime now = LocalDateTime.now();
            Study study =
                    createStudy(mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyHistory studyHistory = createStudyHistory(student, study);

            // when
            couponEventHandler.handleStudyHistoryCompletedEvent(
                    new StudyHistoriesCompletedEvent(List.of(student.getId())));

            // then
            IssuedCoupon issuedCoupon = issuedCouponRepository
                    .findFetchUnrevokedIssuedStudyCoupon(CouponType.STUDY_COMPLETION, student, study)
                    .orElseThrow();
            Coupon coupon = issuedCoupon.getCoupon();
            assertThat(coupon.getCouponType()).isEqualTo(CouponType.STUDY_COMPLETION);
            assertThat(coupon.getStudy().getId()).isEqualTo(study.getId());
            assertThat(issuedCoupon.getMember().getId()).isEqualTo(student.getId());
        }

        @Test
        void 수료_쿠폰이_이미_존재한다면_새로_생성하지_않는다() {
            // given
            Member student = createRegularMember();
            Member mentor = createMentor();
            LocalDateTime now = LocalDateTime.now();
            Study study =
                    createStudy(mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyHistory studyHistory = createStudyHistory(student, study);

            // when
            Coupon coupon = couponRepository.save(
                    Coupon.createAutomatic(COUPON_NAME, Money.FIVE_THOUSAND, CouponType.STUDY_COMPLETION, study));

            couponEventHandler.handleStudyHistoryCompletedEvent(
                    new StudyHistoriesCompletedEvent(List.of(student.getId())));

            // then
            IssuedCoupon issuedCoupon = issuedCouponRepository
                    .findFetchUnrevokedIssuedStudyCoupon(CouponType.STUDY_COMPLETION, student, study)
                    .orElseThrow();
            assertThat(issuedCoupon.getCoupon().getId()).isEqualTo(coupon.getId());
        }

        @Test
        void 수료_쿠폰이_없다면_새로_생성한다() {
            // given
            Member student = createRegularMember();
            Member mentor = createMentor();
            LocalDateTime now = LocalDateTime.now();
            Study study =
                    createStudy(mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyHistory studyHistory = createStudyHistory(student, study);

            // when
            couponEventHandler.handleStudyHistoryCompletedEvent(
                    new StudyHistoriesCompletedEvent(List.of(student.getId())));

            // then
            Optional<Coupon> coupon = couponRepository.findByCouponTypeAndStudy(CouponType.STUDY_COMPLETION, study);
            assertThat(coupon).isPresent();
        }
    }

    @Nested
    class 스터디_수료_철회_이벤트_수신시 {

        @Test
        void 발급쿠폰을_회수한다() {
            // given
            Member student = createRegularMember();
            Member mentor = createMentor();
            LocalDateTime now = LocalDateTime.now();
            Study study =
                    createStudy(mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyHistory studyHistory = createStudyHistory(student, study);

            couponEventHandler.handleStudyHistoryCompletedEvent(
                    new StudyHistoriesCompletedEvent(List.of(student.getId())));
            IssuedCoupon issuedCoupon = issuedCouponRepository
                    .findFetchUnrevokedIssuedStudyCoupon(CouponType.STUDY_COMPLETION, student, study)
                    .orElseThrow();

            // when
            couponEventHandler.handleStudyHistoryCompletionWithdrawnEvent(
                    new StudyHistoryCompletionWithdrawnEvent(studyHistory.getId()));

            // then
            issuedCoupon = issuedCouponRepository.findById(issuedCoupon.getId()).orElseThrow();
            assertThat(issuedCoupon.getHasRevoked()).isTrue();
        }
    }
}
