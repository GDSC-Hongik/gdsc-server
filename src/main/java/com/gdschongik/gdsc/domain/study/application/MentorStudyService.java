package com.gdschongik.gdsc.domain.study.application;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.StudyNotificationRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyNotification;
import com.gdschongik.gdsc.domain.study.dto.request.StudyNotificationRequest;
import com.gdschongik.gdsc.domain.study.dto.response.MentorStudyResponse;
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
    private final StudyNotificationRepository studyNotificationRepository;

    @Transactional(readOnly = true)
    public List<MentorStudyResponse> getStudiesInCharge() {
        Member currentMember = memberUtil.getCurrentMember();
        List<Study> myStudies = studyRepository.findAllByMentor(currentMember);
        return myStudies.stream().map(MentorStudyResponse::from).toList();
    }

    @Transactional
    public void createStudyNotification(Long studyId, StudyNotificationRequest request){
        Member currentMember = memberUtil.getCurrentMember();
        final Study study = studyRepository.getById(studyId);

        // TODO 멘토 or 어드민인지 검증 로직 추가
        StudyNotification studyNotification = StudyNotification.createStudyNotification(study, request.title(), request.link());
        studyNotificationRepository.save(studyNotification);

        log.info("[MentorStudyService] 스터디 공지 생성: studyNotificationId={}", studyNotification.getId());
    }

    @Transactional
    public void updateStudyNotification(Long updateStudyNotificationId, StudyNotificationRequest request){
        Member currentMember = memberUtil.getCurrentMember();
        final StudyNotification studyNotification = studyNotificationRepository.getById(updateStudyNotificationId);

        // TODO 멘토 or 어드민인지 검증 로직 추가
        studyNotification.update(request.title(), request.link());
        studyNotificationRepository.save(studyNotification);

        log.info("[MentorStudyService] 스터디 공지 수정 완료: studyNotificationId={}", studyNotification.getId());
    }

    @Transactional
    public void deleteStudyNotification(Long updateStudyNotificationId){
        Member currentMember = memberUtil.getCurrentMember();
        final StudyNotification studyNotification = studyNotificationRepository.getById(updateStudyNotificationId);

        // TODO 멘토 or 어드민인지 검증 로직 추가
        studyNotificationRepository.save(studyNotification);

        log.info("[MentorStudyService] 스터디 공지 삭제 완료: studyNotificationId={}", studyNotification.getId());
    }
}
