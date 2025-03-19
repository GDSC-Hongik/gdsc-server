package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.studyv2.dao.AssignmentHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.AttendanceV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.*;
import com.gdschongik.gdsc.domain.studyv2.dto.dto.StudyCommonDto;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyApplicableResponse;
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
        StudyHistoryV2 studyHistory = studyHistoryV2Repository
                .findByStudentAndStudy(member, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        List<AttendanceV2> attendances = attendanceV2Repository.findFetchByMemberAndStudy(member, study);
        List<AssignmentHistoryV2> assignmentHistories =
                assignmentHistoryV2Repository.findByMemberAndStudy(member, study);
        LocalDateTime now = LocalDateTime.now();

        return StudyDashboardResponse.of(study, studyHistory, attendances, assignmentHistories, now);
    }

    @Transactional(readOnly = true)
    public StudyApplicableResponse getAllApplicableStudies() {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDateTime now = LocalDateTime.now();

        List<StudyHistoryV2> studyHistories = studyHistoryV2Repository.findAllByStudent(currentMember).stream()
                .filter(studyHistory -> studyHistory.getStudy().isApplicable(now))
                .toList();

        List<StudyV2> applicableStudies = studyV2Repository.findAll().stream()
                .filter(study -> study.isApplicable(now))
                .toList();

        return StudyApplicableResponse.of(studyHistories, applicableStudies);
    }

    @Transactional(readOnly = true)
    public List<StudyCommonDto> getMyCurrentStudies() {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDateTime now = LocalDateTime.now();

        Recruitment recruitment = recruitmentRepository
                .findCurrentRecruitment(now)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));

        // TODO : StudySimpleDTO 로 대체
        return studyHistoryV2Repository.findAllByStudent(currentMember).stream()
                .filter(studyHistory -> studyHistory.getStudy().getSemester().equals(recruitment.getSemester()))
                .map(studyHistory -> StudyCommonDto.from(studyHistory.getStudy()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudyTodoResponse> getMyStudyTodos(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study =
                studyV2Repository.findFetchById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        StudyHistoryV2 studyHistory = studyHistoryV2Repository
                .findByStudentAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        List<AttendanceV2> attendances = attendanceV2Repository.findFetchByMemberAndStudy(currentMember, study);
        List<AssignmentHistoryV2> assignmentHistories =
                assignmentHistoryV2Repository.findByMemberAndStudy(currentMember, study);

        LocalDateTime now = LocalDateTime.now();
        List<StudyTodoResponse> response = new ArrayList<>();

        response.addAll(getAttendanceTodos(study, attendances, now));
        response.addAll(getAssignmentTodos(study, studyHistory, assignmentHistories, now));

        return response;
    }

    @Transactional(readOnly = true)
    public List<StudyTodoResponse> getMyStudiesTodos() {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDateTime now = LocalDateTime.now();

        Recruitment recruitment = recruitmentRepository
                .findCurrentRecruitment(now)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));

        List<StudyV2> currentStudies = studyHistoryV2Repository.findAllByStudent(currentMember).stream()
                .map(StudyHistoryV2::getStudy)
                .filter(study -> study.getSemester().equals(recruitment.getSemester()))
                .toList();

        List<StudyTodoResponse> response = new ArrayList<>();

        currentStudies.forEach(study -> {
            StudyHistoryV2 studyHistory = studyHistoryV2Repository
                    .findByStudentAndStudy(currentMember, study)
                    .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
            List<AttendanceV2> attendances = attendanceV2Repository.findFetchByMemberAndStudy(currentMember, study);
            List<AssignmentHistoryV2> assignmentHistories =
                    assignmentHistoryV2Repository.findByMemberAndStudy(currentMember, study);

            response.addAll(getAttendanceTodos(study, attendances, now));
            response.addAll(getAssignmentTodos(study, studyHistory, assignmentHistories, now));
        });
        return response;
    }

    private List<StudyTodoResponse> getAttendanceTodos(
            StudyV2 study, List<AttendanceV2> attendances, LocalDateTime now) {
        return study.getStudySessions().stream()
                .filter(studySession -> studySession.isAttendable(now))
                .map(studySession -> StudyTodoResponse.attendanceType(study, studySession, attendances, now))
                .toList();
    }

    private List<StudyTodoResponse> getAssignmentTodos(
            StudyV2 study,
            StudyHistoryV2 studyHistory,
            List<AssignmentHistoryV2> assignmentHistories,
            LocalDateTime now) {
        return study.getStudySessions().stream()
                .filter(studySession -> studySession.isAssignmentSubmittable(now))
                .map(studySession ->
                        StudyTodoResponse.assignmentType(study, studyHistory, studySession, assignmentHistories, now))
                .toList();
    }
}
