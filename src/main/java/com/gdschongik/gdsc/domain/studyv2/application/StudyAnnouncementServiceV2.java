package com.gdschongik.gdsc.domain.studyv2.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.recruitment.dao.RecruitmentRepository;
import com.gdschongik.gdsc.domain.recruitment.domain.Recruitment;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyAnnouncementV2Repository;
import com.gdschongik.gdsc.domain.studyv2.dao.StudyHistoryV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyAnnouncementResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyAnnouncementServiceV2 {

    private final MemberUtil memberUtil;
    private final RecruitmentRepository recruitmentRepository;
    private final StudyAnnouncementV2Repository studyAnnouncementV2Repository;
    private final StudyHistoryV2Repository studyHistoryV2Repository;

    @Transactional(readOnly = true)
    public List<StudyAnnouncementResponse> getStudyAnnouncements(@Nullable Long studyId) {
        List<StudyAnnouncementV2> studyAnnouncements =
                studyAnnouncementV2Repository.findAllByStudyIdOrderByCreatedAtDesc(studyId);

        return studyAnnouncements.stream().map(StudyAnnouncementResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyAnnouncementResponse> getStudiesAnnouncements() {
        Member currentMember = memberUtil.getCurrentMember();
        LocalDateTime now = LocalDateTime.now();

        Recruitment recruitment = recruitmentRepository
                .findCurrentRecruitment(now)
                .orElseThrow(() -> new CustomException(RECRUITMENT_NOT_FOUND));

        List<Long> currentStudyHistories = studyHistoryV2Repository.findAllByStudent(currentMember).stream()
                .filter(studyHistory -> studyHistory.getStudy().getSemester().equals(recruitment.getSemester()))
                .map(studyHistoryV2 -> studyHistoryV2.getStudy().getId())
                .toList();

        List<StudyAnnouncementV2> studyAnnouncements =
                studyAnnouncementV2Repository.findAllByStudyIdsOrderByCreatedAtDesc(currentStudyHistories);

        return studyAnnouncements.stream().map(StudyAnnouncementResponse::from).toList();
    }
}
