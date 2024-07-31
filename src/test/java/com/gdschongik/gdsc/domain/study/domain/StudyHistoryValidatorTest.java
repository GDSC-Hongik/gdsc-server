package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.helper.FixtureHelper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyHistoryValidatorTest {

    FixtureHelper fixtureHelper = new FixtureHelper();
    StudyHistoryValidator studyHistoryValidator = new StudyHistoryValidator();

    @Nested
    class 스터디_수강신청시 {

        @Test
        void 이미_해당_스터디를_신청했다면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(15));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            Member mentee = fixtureHelper.createGuestMember(2L);
            StudyHistory studyHistory = StudyHistory.create(mentee, study);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateApplyStudy(study, List.of(studyHistory)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_DUPLICATE.getMessage());
        }

        @Test
        void 해당_스터디의_신청기간이_아니라면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.plusDays(10), now.plusDays(15));
            Period applicationPeriod = Period.createPeriod(now.minusDays(10), now.minusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateApplyStudy(study, List.of()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_NOT_APPLICABLE.getMessage());
        }

        @Test
        void 이미_듣고_있는_스터디가_있다면_실패한다() {
            // given
            Member mentor = fixtureHelper.createAssociateMember(1L);

            LocalDateTime now = LocalDateTime.now();
            Period period = Period.createPeriod(now.minusDays(5), now.plusDays(15));
            Period applicationPeriod = Period.createPeriod(now.minusDays(15), now.plusDays(5));
            Study study = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            Study anotherStudy = fixtureHelper.createStudy(mentor, period, applicationPeriod);

            Member mentee = fixtureHelper.createGuestMember(2L);
            StudyHistory studyHistory = StudyHistory.create(mentee, anotherStudy);

            // when & then
            assertThatThrownBy(() -> studyHistoryValidator.validateApplyStudy(study, List.of(studyHistory)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(STUDY_HISTORY_ONGOING_ALREADY_EXISTS.getMessage());
        }
    }
}
