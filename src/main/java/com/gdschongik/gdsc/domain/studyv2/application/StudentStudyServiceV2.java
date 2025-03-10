package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.studyv2.dao.AssignmentHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.AssignmentHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.AttendanceV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyHistoryV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudentStudyMyCurrentResponse;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyDashboardResponse;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyTodoResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final RecruitmentRepository recruitmentRepository;

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

    @Transactional(readOnly = true)
    public StudentStudyMyCurrentResponse getMyCurrentStudies() {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDateTime now = LocalDateTime.now();

        Recruitment recruitment = recruitmentRepository
                .findCurrentRecruitment(now)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));

        List<StudyHistoryV2> currentStudyHistories = studyHistoryV2Repository.findAllByStudent(currentMember).stream()
                .filter(studyHistory -> studyHistory.getStudy().getSemester().equals(recruitment.getSemester()))
                .toList();

        return StudentStudyMyCurrentResponse.from(currentStudyHistories);
    }

    @Transactional(readOnly = true)
    public List<StudyTodoResponse> getMyStudyTodos(Long studyId) {
        Member member = memberUtil.getCurrentMember();
        StudyV2 study = studyV2Repository.findFetchById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        List<AttendanceV2> attendances = attendanceV2Repository.findFetchByMemberAndStudy(member, study);
        List<AssignmentHistoryV2> assignmentHistories = assignmentHistoryV2Repository.findByMemberAndStudy(member, study);

        LocalDateTime now = LocalDateTime.now();
        List<StudyTodoResponse> response = new ArrayList<>();

        // 출석체크
        study.getStudySessions().stream()
                .filter(studySession -> studySession.getLessonPeriod().isWithin(now))
                .forEach(studySession -> response.add(StudyTodoResponse.attendanceType(studySession, study.getType(), attendances, now)));

        // 과제
        assignmentHistories.stream()
                .filter(assignmentHistory -> assignmentHistory.getStudySession().getAssignmentPeriod().isWithin(now))
                .forEach(assignmentHistory -> response.add(StudyTodoResponse.assignmentType(assignmentHistory, now)));

        return response;
    }
}
