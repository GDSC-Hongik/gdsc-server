package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyAnnouncementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAnnouncement;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyValidator;
import com.gdschongik.gdsc.domain.study.dto.request.StudyAnnouncementRequest;
import com.gdschongik.gdsc.domain.study.dto.response.MentorStudyResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudyStudentResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import com.gdschongik.gdsc.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyService {

    private final MemberUtil memberUtil;
    private final StudyRepository studyRepository;
    private final StudyAnnouncementRepository studyAnnouncementRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyValidator studyValidator;

    @Transactional(readOnly = true)
    public List<MentorStudyResponse> getStudiesInCharge() {
        Member currentMember = memberUtil.getCurrentMember();
        List<Study> myStudies = studyRepository.findAllByMentor(currentMember);
        return myStudies.stream().map(MentorStudyResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyStudentResponse> getStudyStudents(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study =
                studyRepository.findById(studyId).orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        studyValidator.validateStudyMentor(currentMember, study);
        List<StudyHistory> studyHistories = studyHistoryRepository.findByStudyId(studyId);

        return studyHistories.stream().map(StudyStudentResponse::from).toList();
    }

    @Transactional
    public void createStudyAnnouncement(Long studyId, StudyAnnouncementRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        final Study study = studyRepository.getById(studyId);

        studyValidator.validateStudyMentor(currentMember, study);

        StudyAnnouncement studyAnnouncement =
                StudyAnnouncement.createStudyAnnouncement(study, request.title(), request.link());
        studyAnnouncementRepository.save(studyAnnouncement);

        log.info("[MentorStudyService] 스터디 공지 생성: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void updateStudyAnnouncement(Long studyAnnouncementId, StudyAnnouncementRequest request) {
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
}
