package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.common.constant.StudyConstant.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyCreateRequest;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminStudyServiceV2Test extends IntegrationTest {

    @Autowired
    AdminStudyServiceV2 adminStudyService;

    @Autowired
    StudyV2Repository studyV2Repository;

    @Nested
    class 스터디_생성할때 {

        @Test
        void 스터디와_스터디회차가_모두_저장된다() {
            // given
            createRegularMember();
            int totalRound = 8;
            var request = new StudyCreateRequest(
                    1L,
                    StudyType.OFFLINE,
                    STUDY_TITLE,
                    STUDY_SEMESTER,
                    totalRound,
                    DAY_OF_WEEK,
                    STUDY_START_TIME,
                    STUDY_END_TIME,
                    STUDY_APPLICATION_PERIOD,
                    STUDY_DISCORD_CHANNEL_ID,
                    STUDY_DISCORD_ROLE_ID);

            // when
            adminStudyService.createStudy(request);

            // then
            Optional<StudyV2> optionalStudy = studyV2Repository.findFetchById(1L);
            assertThat(optionalStudy).isPresent();

            StudyV2 study = optionalStudy.get();
            assertThat(study.getStudySessions()).hasSize(totalRound);
        }

        @Test
        void 스터디회차에_출석번호가_생성되어_저장된다() {
            // given
            createRegularMember();
            var request = new StudyCreateRequest(
                    1L,
                    StudyType.OFFLINE,
                    STUDY_TITLE,
                    STUDY_SEMESTER,
                    TOTAL_ROUND,
                    DAY_OF_WEEK,
                    STUDY_START_TIME,
                    STUDY_END_TIME,
                    STUDY_APPLICATION_PERIOD,
                    STUDY_DISCORD_CHANNEL_ID,
                    STUDY_DISCORD_ROLE_ID);

            // when
            adminStudyService.createStudy(request);

            // then
            StudyV2 study = studyV2Repository.findFetchById(1L).orElseThrow();
            assertThat(study.getStudySessions())
                    .extracting(StudySessionV2::getLessonAttendanceNumber)
                    .doesNotContainNull();
        }

        @Test
        void 멘토가_멘토_역할로_변경된다() {
            // given
            createRegularMember();
            var request = new StudyCreateRequest(
                    1L,
                    StudyType.OFFLINE,
                    STUDY_TITLE,
                    STUDY_SEMESTER,
                    TOTAL_ROUND,
                    DAY_OF_WEEK,
                    STUDY_START_TIME,
                    STUDY_END_TIME,
                    STUDY_APPLICATION_PERIOD,
                    STUDY_DISCORD_CHANNEL_ID,
                    STUDY_DISCORD_ROLE_ID);

            // when
            adminStudyService.createStudy(request);

            // then
            Member mentor = memberRepository.findById(1L).orElseThrow();
            assertThat(mentor.isMentor()).isTrue();
        }
    }
}
