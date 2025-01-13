package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.domain.study.domain.StudyHistoryStatus.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyHistoryTest {

    FixtureHelper fixtureHelper = new FixtureHelper();

    @Nested
    class 스터디_히스토리_생성시 {

        @Test
        void 수료상태는_NONE이다() {
            // given
            Member student = fixtureHelper.createRegularMember(1L);
            Member mentor = fixtureHelper.createRegularMember(2L);
            LocalDateTime now = LocalDateTime.now();
            Study study = fixtureHelper.createStudy(
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            // when
            StudyHistory studyHistory = StudyHistory.create(student, study);

            // then
            assertThat(studyHistory.getStudyHistoryStatus()).isEqualTo(NONE);
        }
    }

    @Nested
    class 스터디_수료시 {

        @Test
        void 수료상태는_COMPLETED이다() {
            // given
            Member student = fixtureHelper.createRegularMember(1L);
            Member mentor = fixtureHelper.createRegularMember(2L);
            LocalDateTime now = LocalDateTime.now();
            Study study = fixtureHelper.createStudy(
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyHistory studyHistory = StudyHistory.create(student, study);

            // when
            studyHistory.complete();

            // then
            assertThat(studyHistory.getStudyHistoryStatus()).isEqualTo(COMPLETED);
        }
    }

    @Nested
    class 스터디_수료_철회시 {

        @Test
        void 수료상태는_NONE이다() {
            // given
            Member student = fixtureHelper.createRegularMember(1L);
            Member mentor = fixtureHelper.createRegularMember(2L);
            LocalDateTime now = LocalDateTime.now();
            Study study = fixtureHelper.createStudy(
                    mentor, Period.of(now.plusDays(5), now.plusDays(10)), Period.of(now.minusDays(5), now));

            StudyHistory studyHistory = StudyHistory.create(student, study);
            fixtureHelper.setId(studyHistory, 1L); // TODO: 이벤트 ID 필드를 원시 타입으로 설정하는 것 vs setId를 테스트 사용 강제 간 trade-off 고민
            studyHistory.complete();

            // when
            studyHistory.withdrawCompletion();

            // then
            assertThat(studyHistory.getStudyHistoryStatus()).isEqualTo(NONE);
        }
    }
}
