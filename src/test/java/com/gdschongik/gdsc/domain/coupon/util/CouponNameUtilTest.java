package com.gdschongik.gdsc.domain.coupon.util;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.helper.FixtureHelper;
import org.junit.jupiter.api.Test;

class CouponNameUtilTest {

    CouponNameUtil couponNameUtil = new CouponNameUtil();
    FixtureHelper fixtureHelper = new FixtureHelper();

    @Test
    void 스터디_수료_쿠폰_이름이_생성된다() {
        // given
        Member mentor = fixtureHelper.createMentor(1L);
        StudyV2 study = StudyV2.createLive(
                ONLINE_STUDY,
                "기초 백엔드 스터디",
                STUDY_DESCRIPTION,
                STUDY_DESCRIPTION_NOTION_LINK,
                STUDY_SEMESTER,
                TOTAL_ROUND,
                DAY_OF_WEEK,
                STUDY_START_TIME,
                STUDY_END_TIME,
                STUDY_APPLICATION_PERIOD,
                STUDY_DISCORD_CHANNEL_ID,
                STUDY_DISCORD_ROLE_ID,
                mentor);

        // when
        String couponName = couponNameUtil.generateStudyCompletionCouponName(study);

        // then
        assertThat(couponName).isEqualTo("2025-1 기초 백엔드 스터디 수료 쿠폰");
    }
}
