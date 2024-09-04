package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.AttendanceRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyAnnouncementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAnnouncement;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.response.CommonStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyAnnouncementResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonStudyService {

    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyAnnouncementRepository studyAnnouncementRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final MemberUtil memberUtil;
    private final StudyValidator studyValidator;

    @Transactional(readOnly = true)
    public CommonStudyResponse getStudyInformation(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        return CommonStudyResponse.from(study);
    }

    @Transactional(readOnly = true)
    public List<StudyAnnouncementResponse> getStudyAnnouncements(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        final Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        Optional<StudyHistory> studyHistory = studyHistoryRepository.findByStudentAndStudyId(currentMember, studyId);

        studyValidator.validateStudyMentorOrStudent(currentMember, study, studyHistory);

        final List<StudyAnnouncement> studyAnnouncements =
                studyAnnouncementRepository.findAllByStudyIdOrderByCreatedAtDesc(studyId);

        return studyAnnouncements.stream().map(StudyAnnouncementResponse::from).toList();
    }

    /**
     * 이벤트 핸들러에서 사용되므로, `@Transactional` 을 사용하지 않습니다.
     */
    public void deleteAttendanceByStudyHistory(Long studyHistoryId) {
        StudyHistory studyHistory = studyHistoryRepository
                .findById(studyHistoryId)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));

        attendanceRepository.deleteByStudyHistory(studyHistory);
    }

    /**
     * 이벤트 핸들러에서 사용되므로, `@Transactional` 을 사용하지 않습니다.
     */
    public void deleteAssignmentHistoryByStudyHistory(Long studyHistoryId) {
        StudyHistory studyHistory = studyHistoryRepository
                .findById(studyHistoryId)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));

        assignmentHistoryRepository.deleteByStudyHistory(studyHistory);
    }
}
