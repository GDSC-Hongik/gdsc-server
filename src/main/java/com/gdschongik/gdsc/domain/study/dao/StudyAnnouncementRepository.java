package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_ANNOUNCEMENT_NOT_FOUND;

import com.gdschongik.gdsc.domain.study.domain.StudyAnnouncement;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyAnnouncementRepository extends JpaRepository<StudyAnnouncement, Long> {

    default StudyAnnouncement getById(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(STUDY_ANNOUNCEMENT_NOT_FOUND));
    }

    List<StudyAnnouncement> findAllByStudyIdOOrderByCreatedAtDesc(Long studyId);
}
