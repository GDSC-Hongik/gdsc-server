package com.gdschongik.gdsc.domain.study.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.START_DATE;
import static com.gdschongik.gdsc.global.exception.ErrorCode.ASSIGNMENT_CAN_NOT_BE_UPDATED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gdschongik.gdsc.domain.study.domain.vo.Assignment;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StudyDetailTest {
    @Nested
    class 과제_개설시 {

        @Test
        void 과제상태가_휴강이라면_과제를_수정할_수_없다() {
            // given
            Assignment assignment = Assignment.createEmptyAssignment();
            assignment.updateStatus(StudyStatus.CANCELLED);

            // when & then
            assertThatThrownBy(() -> assignment.update("HTTP 통신 코드 작성하기", START_DATE, "https://www.notion.com"))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ASSIGNMENT_CAN_NOT_BE_UPDATED.getMessage());
        }
    }
}
