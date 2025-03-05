package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.studyv2.dao.AssignmentHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyDashboardResponse;
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
    private final StudyV2Repository studyV2Repository;
    private final AttendanceV2Repository attendanceV2Repository;
    private final AssignmentHistoryV2Repository assignmentHistoryV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;
    private final StudyHistoryValidatorV2 studyHistoryValidatorV2;

    @Transactional(readOnly = true)
    public StudyDashboardResponse getMyStudyDashboard(Long studyId) {
        Member member = memberUtil.getCurrentMember();
        StudyV2 study =
                studyV2Repository.findFetchById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        List<AttendanceV2> attendances = attendanceV2Repository.findFetchByMemberAndStudy(member, study);
        List<AssignmentHistoryV2> assignmentHistories =
                assignmentHistoryV2Repository.findByMemberAndStudy(member, study);
        LocalDateTime now = LocalDateTime.now();

        return StudyDashboardResponse.of(study, attendances, assignmentHistories, now);
    }

    @Transactional
    public void applyStudy(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        List<StudyHistoryV2> studyHistories = studyHistoryV2Repository.findAllByStudent(currentMember);
        LocalDateTime now = LocalDateTime.now();

        studyHistoryValidatorV2.validateApplyStudy(study, studyHistories, now);

        StudyHistoryV2 studyHistory = StudyHistoryV2.create(currentMember, study);
        studyHistoryV2Repository.save(studyHistory);

        log.info("[StudentStudyService] 스터디 수강신청: studyHistoryId={}", studyHistory.getId());
    }
}
