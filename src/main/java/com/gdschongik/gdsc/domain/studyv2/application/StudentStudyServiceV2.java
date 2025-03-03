package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.RECRUITMENT_NOT_FOUND;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudentMyCurrentStudyResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentStudyServiceV2 {

    private final MemberUtil memberUtil;
    private final StudyHistoryV2Repository studyHistoryV2Repository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    public StudentMyCurrentStudyResponse getMyCurrentStudies() {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDateTime now = LocalDateTime.now();

        Recruitment recruitment = recruitmentRepository
                .findBySemesterPeriodContains(now)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));

        List<StudyHistoryV2> currentStudyHistories = studyHistoryV2Repository.findAllByStudent(currentMember).stream()
                .filter(studyHistory ->
                        studyHistory.isWithinSemester(recruitment.getAcademicYear(), recruitment.getSemesterType()))
                .toList();

        return StudentMyCurrentStudyResponse.from(currentStudyHistories);
    }
}
