package com.gdschongik.gdsc.domain.studyv2.dao;

import com.gdschongik.gdsc.domain.studyv2.domain.StudyAnnouncementV2;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyAnnouncementV2Repository
        extends JpaRepository<StudyAnnouncementV2, Long>, StudyAnnouncementV2CustomRepository {

    List<StudyAnnouncementV2> findAllByStudyIdOrderByCreatedAtDesc(Long studyId);
}
