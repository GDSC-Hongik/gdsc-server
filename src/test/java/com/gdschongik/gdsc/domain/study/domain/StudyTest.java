package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.MemberConstant.OAUTH_ID;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.ACADEMIC_YEAR;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.END_DATE;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.SEMESTER_TYPE;
import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.START_DATE;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.DAY_OF_WEEK;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.STUDY_TYPE;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.TOTAL_WEEK;
import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_MENTOR_IS_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyTest {

    @Nested
    class 스터디_개설시 {

        @Test
        void 게스트인_회원을_멘토로_지정하면_실패한다() {
            // given
            Member guestMember = Member.createGuestMember(OAUTH_ID);
            Period period = Period.createPeriod(START_DATE, END_DATE);
            Period applicationPeriod = Period.createPeriod(START_DATE.minusDays(10), START_DATE.minusDays(5));
            
            // when & then
            assertThatThrownBy(() -> Study.createStudy(
                            ACADEMIC_YEAR,
                            SEMESTER_TYPE,
                            guestMember,
                            period,
                            applicationPeriod,
                            TOTAL_WEEK,
                            STUDY_TYPE,
                            DAY_OF_WEEK))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_MENTOR_IS_UNAUTHORIZED.getMessage());
        }
    }
}
