package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import java.util.List;

public interface StudyAnnouncementV2CustomRepository {

    List<StudyAnnouncementV2> findAllByStudyIdsOrderByCreatedAtDesc(List<Long> studyIds);
}
