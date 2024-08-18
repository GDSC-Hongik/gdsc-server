package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_NOTIFICATION_NOT_FOUND;

import com.gdschongik.gdsc.domain.study.domain.StudyNotification;
import com.gdschongik.gdsc.global.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyNotificationRepository extends JpaRepository<StudyNotification, Long> {

    default StudyNotification getById(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(STUDY_NOTIFICATION_NOT_FOUND));
    }
}
