package com.gdschongik.gdsc.domain.recruitment.domain;

import static com.gdschongik.gdsc.global.common.constant.RecruitmentConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RecruitmentValidatorTest {

    private RecruitmentValidator recruitmentValidator;
    private RecruitmentRepository recruitmentRepository;

    @BeforeEach
    public void setUp() {
        recruitmentRepository = Mockito.mock(RecruitmentRepository.class);
        recruitmentValidator = new RecruitmentValidator(recruitmentRepository);
    }

    @Nested
    class 리쿠르팅_생성시 {

        @Test
        void 학년도_학기가_모두_중복되는_리쿠르팅이라면_실패한다() {
            // given
            when(recruitmentRepository.existsByAcademicYearAndSemesterType(ACADEMIC_YEAR, SEMESTER_TYPE))
                    .thenReturn(true);

            // when & then
            assertThatThrownBy(() -> recruitmentValidator.validateRecruitmentCreate(ACADEMIC_YEAR, SEMESTER_TYPE))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(RECRUITMENT_OVERLAP.getMessage());
        }
    }
}
