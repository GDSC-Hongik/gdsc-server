package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.domain.study.domain.AchievementType.*;
import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.domain.vo.Period;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.gdschongik.gdsc.domain.study.dto.request.OutstandingStudentRequest;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MentorStudyAchievementServiceTest extends IntegrationTest {

    @Autowired
    private MentorStudyAchievementService mentorStudyAchievementService;

    @Nested
    class 우수_스터디원_지정시 {

        @Test
        void 성공한다() {
            // given
            LocalDateTime now = LocalDateTime.now();
            Member mentor = createMentor();
            Study study = createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));

            Member student = createRegularMember();
            createStudyHistory(student, study);

            logoutAndReloginAs(mentor.getId(), mentor.getRole());
            OutstandingStudentRequest request =
                    new OutstandingStudentRequest(List.of(student.getId()), FIRST_ROUND_OUTSTANDING_STUDENT);

            // when
            mentorStudyAchievementService.designateOutstandingStudent(study.getId(), request);

            // then
            List<StudyAchievement> studyAchievements =
                    studyAchievementRepository.findByStudyIdAndMemberIds(study.getId(), request.studentIds());
            assertThat(studyAchievements).hasSize(request.studentIds().size());
        }
    }

    @Nested
    class 우수_스터디원_철회시 {

        @Test
        void 성공한다() {
            // given
            Member student = createRegularMember();
            LocalDateTime now = LocalDateTime.now();
            Member mentor = createMentor();
            Study study = createStudy(
                    mentor,
                    Period.createPeriod(now.plusDays(5), now.plusDays(10)),
                    Period.createPeriod(now.minusDays(5), now));
            createStudyHistory(student, study);
            createStudyAchievement(student, study, FIRST_ROUND_OUTSTANDING_STUDENT);

            logoutAndReloginAs(mentor.getId(), mentor.getRole());
            OutstandingStudentRequest request =
                    new OutstandingStudentRequest(List.of(student.getId()), FIRST_ROUND_OUTSTANDING_STUDENT);

            // when
            mentorStudyAchievementService.withdrawOutstandingStudent(study.getId(), request);

            // then
            List<StudyAchievement> studyAchievements =
                    studyAchievementRepository.findByStudyIdAndMemberIds(study.getId(), request.studentIds());
            assertThat(studyAchievements).isEmpty();
        }
    }
}
