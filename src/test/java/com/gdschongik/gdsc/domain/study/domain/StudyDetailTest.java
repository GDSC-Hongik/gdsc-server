package com.gdschongik.gdsc.domain.study.domain;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyDetailTest {

    @Nested
    class 과제_휴강_처리시 {

        FixtureHelper fixtureHelper = new FixtureHelper();

        @Test
        void 과제_상태가_휴강이_된다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);
            LocalDateTime now = LocalDateTime.now();
            Study study = fixtureHelper.createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            StudyDetail studyDetail = fixtureHelper.createStudyDetail(study, now, now.plusDays(7));

            // when
            studyDetail.cancelAssignment();

            // then
            assertThat(studyDetail.getAssignment().getStatus()).isEqualTo(StudyStatus.CANCELLED);
        }
    }
}
