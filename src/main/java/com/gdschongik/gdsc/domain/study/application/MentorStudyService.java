package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_NOT_FOUND;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.AttendanceRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyAchievementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyAnnouncementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.*;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAnnouncement;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.request.StudyAnnouncementCreateUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudyCurriculumCreateRequest;
import com.gdschongik.gdsc.domain.study.dto.request.StudyUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyTodoResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyService {

    private final MemberUtil memberUtil;
    private final StudyValidator studyValidator;
    private final StudyDetailValidator studyDetailValidator;
    private final StudyRepository studyRepository;
    private final StudyAnnouncementRepository studyAnnouncementRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyAchievementRepository studyAchievementRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;

    @Transactional(readOnly = true)
    public List<StudyResponse> getStudiesInCharge() {
        Member currentMember = memberUtil.getCurrentMember();
        List<Study> myStudies = studyRepository.findAllByMentor(currentMember);
        return myStudies.stream().map(StudyResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Page<StudyStudentResponse> getStudyStudents(Long studyId, Pageable pageable) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study =
                studyRepository.findById(studyId).orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        studyValidator.validateStudyMentor(currentMember, study);

        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyId(studyId);
        Page<StudyHistory> studyHistories = studyHistoryRepository.findByStudyId(studyId, pageable);
        List<Long> studentIds = studyHistories.getContent().stream()
                .map(studyHistory -> studyHistory.getStudent().getId())
                .toList();

        List<StudyAchievement> studyAchievements =
                studyAchievementRepository.findByStudyIdAndMemberIds(studyId, studentIds);
        List<Attendance> attendances = attendanceRepository.findByStudyIdAndMemberIds(studyId, studentIds);
        List<AssignmentHistory> assignmentHistories =
                assignmentHistoryRepository.findByStudyIdAndMemberIds(studyId, studentIds);

        List<StudyStudentResponse> response = new ArrayList<>();
        studyHistories.getContent().forEach(studyHistory -> {
            List<StudyAchievement> currentStudyAchievements = studyAchievements.stream()
                    .filter(studyAchievement -> studyAchievement
                            .getStudent()
                            .getId()
                            .equals(studyHistory.getStudent().getId()))
                    .toList();
            List<Attendance> currentAttendances = attendances.stream()
                    .filter(attendance -> attendance
                            .getStudent()
                            .getId()
                            .equals(studyHistory.getStudent().getId()))
                    .toList();
            List<AssignmentHistory> currentAssignmentHistories = assignmentHistories.stream()
                    .filter(assignmentHistory -> assignmentHistory
                            .getMember()
                            .getId()
                            .equals(studyHistory.getStudent().getId()))
                    .toList();

            List<StudyTodoResponse> studyTodos = new ArrayList<>();
            studyDetails.forEach(studyDetail -> {
                studyTodos.add(StudyTodoResponse.createAttendanceType(
                        studyDetail, LocalDate.now(), isAttended(currentAttendances, studyDetail)));
                studyTodos.add(StudyTodoResponse.createAssignmentType(
                        studyDetail, getSubmittedAssignment(currentAssignmentHistories, studyDetail)));
            });

            response.add(StudyStudentResponse.of(studyHistory, currentStudyAchievements, studyTodos));
        });

        return new PageImpl<>(response, pageable, studyHistories.getTotalElements());
    }

    private boolean isAttended(List<Attendance> attendances, StudyDetail studyDetail) {
        return attendances.stream()
                .anyMatch(attendance -> attendance.getStudyDetail().getId().equals(studyDetail.getId()));
    }

    private AssignmentHistory getSubmittedAssignment(
            List<AssignmentHistory> assignmentHistories, StudyDetail studyDetail) {
        return assignmentHistories.stream()
                .filter(assignmentHistory ->
                        assignmentHistory.getStudyDetail().getId().equals(studyDetail.getId()))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void createStudyAnnouncement(Long studyId, StudyAnnouncementCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        final Study study = studyRepository.getById(studyId);

        studyValidator.validateStudyMentor(currentMember, study);

        StudyAnnouncement studyAnnouncement =
                StudyAnnouncement.createStudyAnnouncement(study, request.title(), request.link());
        studyAnnouncementRepository.save(studyAnnouncement);

        log.info("[MentorStudyService] 스터디 공지 생성: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void updateStudyAnnouncement(Long studyAnnouncementId, StudyAnnouncementCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        final StudyAnnouncement studyAnnouncement = studyAnnouncementRepository.getById(studyAnnouncementId);
        Study study = studyAnnouncement.getStudy();

        studyValidator.validateStudyMentor(currentMember, study);

        studyAnnouncement.update(request.title(), request.link());
        studyAnnouncementRepository.save(studyAnnouncement);

        log.info("[MentorStudyService] 스터디 공지 수정 완료: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void deleteStudyAnnouncement(Long studyAnnouncementId) {
        Member currentMember = memberUtil.getCurrentMember();
        final StudyAnnouncement studyAnnouncement = studyAnnouncementRepository.getById(studyAnnouncementId);
        Study study = studyAnnouncement.getStudy();

        studyValidator.validateStudyMentor(currentMember, study);

        studyAnnouncementRepository.delete(studyAnnouncement);

        log.info("[MentorStudyService] 스터디 공지 삭제 완료: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void updateStudy(Long studyId, StudyUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        studyValidator.validateStudyMentor(currentMember, study);

        List<StudyDetail> studyDetails = studyDetailRepository.findAllByStudyIdOrderByWeekAsc(studyId);
        // StudyDetail ID를 추출하여 Set으로 저장
        Set<Long> studyDetailIds = studyDetails.stream().map(StudyDetail::getId).collect(Collectors.toSet());

        // 요청된 StudyCurriculumCreateRequest의 StudyDetail ID를 추출하여 Set으로 저장
        Set<Long> requestIds = request.studyCurriculums().stream()
                .map(StudyCurriculumCreateRequest::studyDetailId)
                .collect(Collectors.toSet());

        studyDetailValidator.validateUpdateStudyDetail(studyDetailIds, requestIds);

        study.update(request.notionLink(), request.introduction());
        studyRepository.save(study);
        log.info("[MentorStudyService] 스터디 기본 정보 수정 완료: studyId={}", studyId);

        updateAllStudyDetailCurriculum(studyDetails, request.studyCurriculums());
    }

    private void updateAllStudyDetailCurriculum(
            List<StudyDetail> studyDetails, List<StudyCurriculumCreateRequest> studyCurriculums) {
        for (StudyDetail studyDetail : studyDetails) {
            Long id = studyDetail.getId();
            StudyCurriculumCreateRequest matchingCurriculum = studyCurriculums.stream()
                    .filter(curriculum -> curriculum.studyDetailId().equals(id))
                    .findFirst()
                    .get();

            studyDetail.updateCurriculum(
                    studyDetail.getStudy().getStartTime(),
                    matchingCurriculum.title(),
                    matchingCurriculum.description(),
                    matchingCurriculum.difficulty(),
                    matchingCurriculum.status());
        }
        studyDetailRepository.saveAll(studyDetails);
        log.info("[MentorStudyService] 스터디 상세정보 커리큘럼 작성 완료: studyDetailId={}", studyDetails);
    }
}
