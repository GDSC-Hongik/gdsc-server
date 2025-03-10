package com.gdschongik.gdsc.domain.studyv2.application;

import com.gdschongik.gdsc.domain.studyv2.dao.StudyAnnouncementV2Repository;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import com.gdschongik.gdsc.domain.studyv2.dto.response.StudyAnnouncementResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonStudyServiceV2 {

    private final StudyAnnouncementV2Repository studyAnnouncementV2Repository;

    @Transactional(readOnly = true)
    public StudyAnnouncementResponse getStudyAnnouncements(Long studyId) {
        List<StudyAnnouncementV2> studyAnnouncements =
                studyAnnouncementV2Repository.findAllByStudyIdOrderByCreatedAtDesc(studyId);

        return StudyAnnouncementResponse.from(studyAnnouncements);
    }
}
