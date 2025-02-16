package com.gdschongik.gdsc.domain.studyv2.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.helper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MentorStudyServiceV2Test extends IntegrationTest {

    @Autowired
    MentorStudyServiceV2 mentorStudyServiceV2;

    @Nested
    class 스터디_수정할때 {

        @Test
        void 스터디_스터디회차에_변경사항이_모두_반영된다() {
            // given
            Member mentor = createMentor();
            logoutAndReloginAs(1L, MemberRole.REGULAR);
            createStudy(StudyType.OFFLINE, mentor);

            var request = new StudyUpdateRequest(
                    "수정된 제목",
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of(new StudyUpdateRequest.StudySessionUpdateDto(
                            1L, "수정된 1회차 스터디 제목", null, null, null, null)));

            // when
            mentorStudyServiceV2.updateStudy(1L, request);

            // then
            Optional<StudyV2> optionalStudy = studyV2Repository.findFetchById(1L);
            assertThat(optionalStudy).isPresent();

            StudyV2 study = optionalStudy.get();
            assertThat(study.getTitle()).isEqualTo("수정된 제목");
            assertThat(study.getStudySessions().get(0).getTitle()).isEqualTo("수정된 1회차 스터디 제목");
        }
    }
}
