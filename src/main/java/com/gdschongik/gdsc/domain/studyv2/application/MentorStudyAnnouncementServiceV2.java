package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.common.constant.UrlConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dto.request.StudyAnnouncementCreateUpdateRequest;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyAnnouncementV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
import com.gdschongik.gdsc.domain.studyv2.domain.service.StudyValidatorV2;
import com.gdschongik.gdsc.domain.studyv2.dto.request.NotionWebhookRequest;
import com.gdschongik.gdsc.domain.studyv2.dto.request.StudyAnnouncementCreateRequest;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorStudyAnnouncementServiceV2 {

    private final MemberUtil memberUtil;
    private final StudyValidatorV2 studyValidatorV2;
    private final StudyV2Repository studyV2Repository;
    private final StudyAnnouncementV2Repository studyAnnouncementV2Repository;

    @Transactional
    public void createStudyAnnouncement(StudyAnnouncementCreateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyV2 study =
                studyV2Repository.findById(request.studyId()).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        studyValidatorV2.validateStudyMentor(currentMember, study);

        StudyAnnouncementV2 studyAnnouncement = StudyAnnouncementV2.create(request.title(), request.link(), study);
        studyAnnouncementV2Repository.save(studyAnnouncement);

        log.info("[MentorStudyAnnouncementServiceV2] 스터디 공지 생성: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void updateStudyAnnouncement(Long studyAnnouncementId, StudyAnnouncementCreateUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyAnnouncementV2 studyAnnouncement = studyAnnouncementV2Repository
                .findById(studyAnnouncementId)
                .orElseThrow(() -> new CustomException(STUDY_ANNOUNCEMENT_NOT_FOUND));
        StudyV2 study = studyAnnouncement.getStudy();

        studyValidatorV2.validateStudyMentor(currentMember, study);

        studyAnnouncement.update(request.title(), request.link());
        studyAnnouncementV2Repository.save(studyAnnouncement);

        log.info("[MentorStudyAnnouncementServiceV2] 스터디 공지 수정 완료: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void deleteStudyAnnouncement(Long studyAnnouncementId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyAnnouncementV2 studyAnnouncement = studyAnnouncementV2Repository
                .findById(studyAnnouncementId)
                .orElseThrow(() -> new CustomException(STUDY_ANNOUNCEMENT_NOT_FOUND));
        StudyV2 study = studyAnnouncement.getStudy();

        studyValidatorV2.validateStudyMentor(currentMember, study);

        studyAnnouncementV2Repository.delete(studyAnnouncement);

        log.info("[MentorStudyAnnouncementServiceV2] 스터디 공지 삭제 완료: studyAnnouncementId={}", studyAnnouncement.getId());
    }

    @Transactional
    public void createStudyAnnouncementByWebhook(NotionWebhookRequest request) {
        String studyName = request.getStudyName();
        Semester semester = request.getSemester();

        StudyV2 study = studyV2Repository
                .findByTitleAndSemester(studyName, semester)
                .orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        String announcementTitle = request.getTitle();
        String fullLink = STUDY_ANNOUNCEMENT_DOMAIN + request.getCleanUrl();

        StudyAnnouncementV2 studyAnnouncement = StudyAnnouncementV2.create(announcementTitle, fullLink, study);
        studyAnnouncementV2Repository.save(studyAnnouncement);

        log.info(
                "[MentorStudyAnnouncementServiceV2] 노션 웹훅으로 스터디 공지 생성: studyAnnouncementId={}, title={}, link={}",
                studyAnnouncement.getId(),
                announcementTitle,
                fullLink);
    }
}
