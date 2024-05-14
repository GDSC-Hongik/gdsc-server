package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecruitmentUtil {

    private final RecruitmentRepository recruitmentRepository;

    public Recruitment getOpenRecruitment() {
        return recruitmentRepository
                .findOpenRecruitment()
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));
    }
}
