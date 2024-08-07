package com.gdschongik.gdsc.domain.study.application;

import static org.assertj.core.api.Assertions.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.helper.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentStudyServiceTest extends IntegrationTest {

    @Autowired
    private StudentStudyService studentStudyService;

    @Nested
    class 스터디_수강신청시 {

        @Test
        void 존재하지_않는_스터디라면_실패한다() {
            // when & then
            assertThatThrownBy(() -> studentStudyService.applyStudy(1L))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.STUDY_NOT_FOUND.getMessage());
        }
    }
}
