package com.gdschongik.gdsc.domain.coupon.util;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.helper.FixtureHelper;
import org.junit.jupiter.api.Test;

class CouponNameUtilTest {

    CouponNameUtil couponNameUtil = new CouponNameUtil();
    FixtureHelper fixtureHelper = new FixtureHelper();

    @Test
    void 스터디_수료_쿠폰_이름이_생성된다() {
        // given
        Member mentor = fixtureHelper.createMentor(1L);
        Study study = Study.create(
                2025,
                SemesterType.FIRST,
                "기초 백엔드 스터디",
                mentor,
                STUDY_ONGOING_PERIOD,
                Period.of(START_DATE.minusDays(10), START_DATE.minusDays(5)),
                TOTAL_WEEK,
                ONLINE_STUDY,
                DAY_OF_WEEK,
                STUDY_START_TIME,
                STUDY_END_TIME);

        // when
        String couponName = couponNameUtil.generateStudyCompletionCouponName(study);

        // then
        assertThat(couponName).isEqualTo("2025-1 기초 백엔드 스터디 수료 쿠폰");
    }
}
